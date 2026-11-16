package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String REGISTER = "/WEB-INF/views/register.jsp";
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }
        req.getRequestDispatcher(REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String email = trim(req.getParameter("email"));
        String fullname = trim(req.getParameter("fullname"));
        String phone = trim(req.getParameter("phone"));

        String alert = null;
        if (username.isEmpty() || password == null || password.isEmpty() || email.isEmpty()) {
            alert = "Vui lòng nhập tài khoản, email và mật khẩu";
        } else if (service.checkExistEmail(email)) {
            alert = "Email đã tồn tại!";
        } else if (service.checkExistUsername(username)) {
            alert = "Tài khoản đã tồn tại!";
        } else if (!phone.isEmpty() && service.checkExistPhone(phone)) {
            alert = "Số điện thoại đã tồn tại!";
        }
        if (alert != null) {
            req.setAttribute("alert", alert);
            req.getRequestDispatcher(REGISTER).forward(req, resp);
            return;
        }

        // phone rỗng -> null để không lưu chuỗi "" vào DB (bỏ phần này nếu cột phone là NOT NULL)
        boolean ok = service.register(username, password, email, fullname,
                                      phone.isEmpty() ? null : phone);
        if (ok) {
            req.getSession().setAttribute("pendingUser", username);
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
            return;
        }
        req.setAttribute("alert", "Đăng ký không thành công, vui lòng thử lại");
        req.getRequestDispatcher(REGISTER).forward(req, resp);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}