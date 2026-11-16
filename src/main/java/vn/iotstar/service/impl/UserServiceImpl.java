package vn.iotstar.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.OtpUtil;
import vn.iotstar.util.PasswordUtil;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDaoImpl();
    private static final int OTP_TTL_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;
    private static final int RESEND_COOLDOWN_SECONDS = 60;

    @Override
    public User login(String username, String password) {
        User user = this.get(username);
        if (user == null) return null;

        String stored = user.getPassWord();

        if (PasswordUtil.isHashed(stored)) {
            return PasswordUtil.verify(password, stored) ? user : null;
        }

        // Tài khoản cũ còn mật khẩu thô: đúng thì băm và ghi đè ngay
        if (stored != null && MessageDigest.isEqual(
                stored.getBytes(StandardCharsets.UTF_8),
                password.getBytes(StandardCharsets.UTF_8))) {
            String hashed = PasswordUtil.hash(password);
            userDao.updatePassword(username, hashed);
            user.setPassWord(hashed);
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public void insert(User user) {
        if (!PasswordUtil.isHashed(user.getPassWord())) {
            user.setPassWord(PasswordUtil.hash(user.getPassWord()));
        }
        userDao.insert(user);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username)) {
            return false;
        }
        String otp = OtpUtil.generate();

        User user = new User();
        user.setEmail(email);
        user.setUserName(username);
        user.setFullName(fullname);
        user.setPassWord(PasswordUtil.hash(password));
        user.setAvatar(null);
        user.setRoleid(5);
        user.setPhone(phone);
        user.setCreatedDate(LocalDate.now());
        user.setOtpHash(OtpUtil.hash(otp, username));
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_TTL_MINUTES));
        userDao.insert(user);

        sendMail(email, "Mã xác thực tài khoản",
                 "Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong " + OTP_TTL_MINUTES + " phút.");
        return true;
    }

    @Override
    public String verifyOtp(String username, String otp) {
        User u = userDao.get(username);
        if (u == null)    return "Tài khoản không tồn tại";
        if (u.isActive()) return "Tài khoản đã được kích hoạt";
        if (u.getOtpAttempts() >= MAX_ATTEMPTS)
            return "Nhập sai quá nhiều lần, vui lòng gửi lại mã mới";
        if (u.getOtpExpiry() == null || u.getOtpExpiry().isBefore(LocalDateTime.now()))
            return "Mã OTP đã hết hạn, vui lòng gửi lại";

        if (u.getOtpHash() == null || !OtpUtil.hash(otp, username).equals(u.getOtpHash())) {
            userDao.increaseOtpAttempts(username);
            return "Mã OTP không đúng";
        }
        userDao.activate(username);
        return null;
    }

    @Override
    public String resendOtp(String username) {
        User u = userDao.get(username);
        if (u == null || u.isActive()) return "Yêu cầu không hợp lệ";

        String otp = OtpUtil.generate();
        userDao.updateOtp(username, OtpUtil.hash(otp, username),
                          LocalDateTime.now().plusMinutes(OTP_TTL_MINUTES));
        return sendMail(u.getEmail(), "Mã xác thực tài khoản",
                "Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong " + OTP_TTL_MINUTES + " phút.")
                ? null : "Không gửi được email, vui lòng thử lại sau";
    }

    // ===== Quên mật khẩu =====

    @Override
    public String requestPasswordReset(String email) {
        User u = userDao.getByEmail(email);
        // Email không tồn tại hoặc tài khoản chưa kích hoạt: không gửi gì, vẫn trả "thành công"
        // để người ngoài không dò được email nào có trong hệ thống
        if (u == null || !u.isActive()) return null;

        // Chống spam: vừa gửi chưa quá 60 giây thì bỏ qua yêu cầu này
        LocalDateTime exp = u.getResetExpiry();
        if (exp != null && exp.minusMinutes(OTP_TTL_MINUTES)
                             .plusSeconds(RESEND_COOLDOWN_SECONDS)
                             .isAfter(LocalDateTime.now())) {
            return null;
        }

        String otp = OtpUtil.generate();
        userDao.updateResetOtp(u.getUserName(),
                OtpUtil.hash(otp, "reset:" + u.getUserName()),
                LocalDateTime.now().plusMinutes(OTP_TTL_MINUTES));

        return sendMail(u.getEmail(), "Mã đặt lại mật khẩu",
                "Mã đặt lại mật khẩu của bạn là: " + otp
              + "\nMã có hiệu lực trong " + OTP_TTL_MINUTES + " phút."
              + "\nNếu bạn không yêu cầu, hãy bỏ qua email này.")
                ? null : "Không gửi được email, vui lòng thử lại sau";
    }

    @Override
    public String resetPassword(String email, String otp, String newPassword) {
        final String GENERIC = "Mã OTP không đúng hoặc đã hết hạn";

        User u = userDao.getByEmail(email);
        if (u == null || !u.isActive()) return GENERIC;

        if (u.getResetAttempts() >= MAX_ATTEMPTS)
            return "Nhập sai quá nhiều lần, vui lòng yêu cầu mã mới";
        if (u.getResetHash() == null || u.getResetExpiry() == null
                || u.getResetExpiry().isBefore(LocalDateTime.now()))
            return GENERIC;

        String given = OtpUtil.hash(otp, "reset:" + u.getUserName());
        boolean ok = MessageDigest.isEqual(
                given.getBytes(StandardCharsets.UTF_8),
                u.getResetHash().getBytes(StandardCharsets.UTF_8));
        if (!ok) {
            userDao.increaseResetAttempts(u.getUserName());
            return GENERIC;
        }

        userDao.resetPassword(u.getUserName(), PasswordUtil.hash(newPassword));
        return null;
    }

    private boolean sendMail(String to, String subject, String body) {
        try {
            EmailUtil.send(to, subject, body);
            return true;
        } catch (Exception e) {
            e.printStackTrace();   // không in mã OTP ra log
            return false;
        }
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.checkExistPhone(phone);
    }
}