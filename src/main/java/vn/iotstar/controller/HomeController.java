package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = { "/user/home", "/admin/home", "/manager/home" })
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        String view = "/WEB-INF/views/user/home.jsp";
        if (path.equals("/user/home")) {
            req.setAttribute("latestVideos", videoService.getLatest(Constant.HOME_VIDEO_LIMIT));
        }
        if (path.startsWith("/admin")) {
            view = "/WEB-INF/views/admin/home.jsp";
        } else if (path.startsWith("/manager")) {
            view = "/WEB-INF/views/manager/home.jsp";
        }
        req.getRequestDispatcher(view).forward(req, resp);
    }
}
