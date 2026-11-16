package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = { "/verify-otp", "/resend-otp" })
public class VerifyOtpController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String VIEW = "/WEB-INF/views/verify-otp.jsp";

    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Gửi lại mã chỉ nhận POST; mở bằng GET thì đưa về trang nhập
        if ("/resend-otp".equals(req.getServletPath())) {
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
            return;
        }
        String username = pendingUser(req);
        if (username == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        showForm(req, resp, username);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = pendingUser(req);
        if (username == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        if ("/resend-otp".equals(req.getServletPath())) {
            String error = service.resendOtp(username);
            if (error == null) {
                req.setAttribute("info", "Đã gửi lại mã OTP, vui lòng kiểm tra email");
            } else {
                req.setAttribute("alert", error);
            }
            showForm(req, resp, username);
            return;
        }

        String otp = req.getParameter("otp");
        if (otp == null || !otp.trim().matches("\\d{6}")) {
            req.setAttribute("alert", "Mã OTP gồm đúng 6 chữ số");
            showForm(req, resp, username);
            return;
        }

        String error = service.verifyOtp(username, otp.trim());
        if (error == null) {
            req.getSession().removeAttribute("pendingUser");
            resp.sendRedirect(req.getContextPath() + "/login?activated=1");
            return;
        }
        req.setAttribute("alert", error);
        showForm(req, resp, username);
    }

    private String pendingUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (String) session.getAttribute("pendingUser");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, String username)
            throws ServletException, IOException {
        User u = service.get(username);
        req.setAttribute("maskedEmail", u == null ? "" : mask(u.getEmail()));
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    // abcdef@gmail.com -> ab****@gmail.com
    private String mask(String email) {
        if (email == null || !email.contains("@")) return "";
        int at = email.indexOf('@');
        String name = email.substring(0, at);
        String shown = name.length() <= 2 ? name.substring(0, Math.max(1, name.length()))
                                          : name.substring(0, 2);
        return shown + "****" + email.substring(at);
    }
}