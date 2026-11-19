package vn.iotstar.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;

public final class SessionUtil {
    private SessionUtil() {}

    /** User đang đăng nhập, hoặc null nếu chưa đăng nhập (không tạo session mới). */
    public static User currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
    }
}
