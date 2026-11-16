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

@WebServlet(urlPatterns = { "/forgot-password", "/reset-password" })
public class ForgotPasswordController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String FORGOT = "/WEB-INF/views/forgot-password.jsp";
    private static final String RESET = "/WEB-INF/views/reset-password.jsp";
    private static final String SESSION_EMAIL = "resetEmail";

    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("/reset-password".equals(req.getServletPath())) {
            String email = sessionEmail(req);
            if (email == null) {
                resp.sendRedirect(req.getContextPath() + "/forgot-password");
                return;
            }
            showReset(req, resp, email);
            return;
        }
        req.getRequestDispatcher(FORGOT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        if ("/reset-password".equals(req.getServletPath())) {
            handleReset(req, resp);
        } else {
            handleRequest(req, resp);
        }
    }

    // Bước 1: nhập email, gửi mã
    private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = trim(req.getParameter("email"));
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            req.setAttribute("alert", "Email không hợp lệ");
            req.getRequestDispatcher(FORGOT).forward(req, resp);
            return;
        }

        String error = service.requestPasswordReset(email);
        if (error != null) {
            req.setAttribute("alert", error);
            req.getRequestDispatcher(FORGOT).forward(req, resp);
            return;
        }
        req.getSession().setAttribute(SESSION_EMAIL, email);
        resp.sendRedirect(req.getContextPath() + "/reset-password");
    }

    // Bước 2: nhập OTP + mật khẩu mới (hoặc bấm gửi lại mã)
    private void handleReset(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = sessionEmail(req);
        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        if ("1".equals(req.getParameter("resend"))) {
            String error = service.requestPasswordReset(email);
            if (error == null) {
                req.setAttribute("info", "Nếu email hợp lệ, mã mới đã được gửi, vui lòng kiểm tra hộp thư");
            } else {
                req.setAttribute("alert", error);
            }
            showReset(req, resp, email);
            return;
        }

        String otp = trim(req.getParameter("otp"));
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        String alert = null;
        if (!otp.matches("\\d{6}")) {
            alert = "Mã OTP gồm đúng 6 chữ số";
        } else if (password == null || password.length() < 6 || password.length() > 72) {
            alert = "Mật khẩu mới phải từ 6 đến 72 ký tự";
        } else if (!password.equals(confirm)) {
            alert = "Mật khẩu nhập lại không khớp";
        }
        if (alert == null) {
            alert = service.resetPassword(email, otp, password);
        }

        if (alert != null) {
            req.setAttribute("alert", alert);
            showReset(req, resp, email);
            return;
        }
        req.getSession().removeAttribute(SESSION_EMAIL);
        resp.sendRedirect(req.getContextPath() + "/login?reset=1");
    }

    private String sessionEmail(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute(SESSION_EMAIL);
    }

    private void showReset(HttpServletRequest req, HttpServletResponse resp, String email)
            throws ServletException, IOException {
        req.setAttribute("maskedEmail", mask(email));
        req.getRequestDispatcher(RESET).forward(req, resp);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    // abcdef@gmail.com -> ab****@gmail.com
    private static String mask(String email) {
        int at = email.indexOf('@');
        if (at <= 0) return "";
        String name = email.substring(0, at);
        String shown = name.length() <= 2 ? name.substring(0, 1) : name.substring(0, 2);
        return shown + "****" + email.substring(at);
    }
}