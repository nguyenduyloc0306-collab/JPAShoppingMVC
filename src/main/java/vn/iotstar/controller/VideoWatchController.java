package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.SessionUtil;

/** Chi tiết và phát một video: /video/watch?id=... Ai cũng xem được video đang hiển thị; video ẩn chỉ chủ sở hữu xem được. */
@WebServlet(urlPatterns = { "/video/watch" })
public class VideoWatchController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String VIEW = "/WEB-INF/views/video/watch-video.jsp";
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Video video = (id == null || id.isBlank()) ? null : videoService.get(id);

        User user = SessionUtil.currentUser(req); // có thể null (khách)
        boolean owner = video != null && video.getUser() != null && user != null
                && video.getUser().getId() == user.getId();

        if (video == null || (!video.isActive() && !owner)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        videoService.increaseViews(id);
        video.setViews(video.getViews() + 1); // đã tăng trong DB; cộng ngay ở bản đang hiển thị để khỏi đọc lại

        req.setAttribute("video", video);
        req.setAttribute("owner", owner);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
