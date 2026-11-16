package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(urlPatterns = { "/home", "/admin/home", "/manager/home" })
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        String view = "/WEB-INF/views/home.jsp";
        if (path.startsWith("/admin")) {
            view = "/WEB-INF/views/admin/home.jsp";
        } else if (path.startsWith("/manager")) {
            view = "/WEB-INF/views/manager/home.jsp";
        }
        req.getRequestDispatcher(view).forward(req, resp);
    }
}
