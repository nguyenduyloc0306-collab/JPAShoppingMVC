package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.SessionUtil;

/** Xóa bằng POST (không xóa qua link GET để tránh bị kích hoạt vô tình hoặc từ trang khác). */
@WebServlet(urlPatterns = { "/video/delete" })
public class VideoDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/video");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = SessionUtil.currentUser(req);
        boolean deleted = videoService.delete(req.getParameter("id"), user.getId());
        resp.sendRedirect(req.getContextPath() + "/video" + (deleted ? "?msg=deleted" : ""));
    }
}
