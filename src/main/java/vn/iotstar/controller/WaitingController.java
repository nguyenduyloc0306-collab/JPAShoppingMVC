package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.util.Constant;

/** Điều hướng theo vai trò sau khi đăng nhập. */
@WebServlet(urlPatterns = "/waiting")
public class WaitingController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            User u = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
            String ctx = req.getContextPath();
            if (u.getRoleid() == 1) {
                resp.sendRedirect(ctx + "/admin/home");
            } else if (u.getRoleid() == 2) {
                resp.sendRedirect(ctx + "/manager/home");
            } else {
                resp.sendRedirect(ctx + "/user/home");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }   
}
