package vn.iotstar.dao;

import java.time.LocalDateTime;

import vn.iotstar.entity.User;

public interface UserDao {
    void insert(User user);
    User get(String username);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
    void updateOtp(String username, String otpHash, LocalDateTime expiry);
    void increaseOtpAttempts(String username);
    void activate(String username);
    void updatePassword(String username, String passwordHash);
    User getByEmail(String email);
    void updateResetOtp(String username, String resetHash, LocalDateTime expiry);
    void increaseResetAttempts(String username);
    void resetPassword(String username, String passwordHash);
}
