package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.SessionUtil;

/** Tất cả video của user đang đăng nhập, phân trang (/video?page=2). */
@WebServlet(urlPatterns = { "/video" })
public class VideoListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String VIEW = "/WEB-INF/views/video/list-video.jsp";
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtil.currentUser(req);

        long total = videoService.countByUser(user.getId());
        int size = Constant.VIDEO_PAGE_SIZE;
        int totalPages = (int) Math.max(1, (total + size - 1) / size);

        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(req.getParameter("page"));
        } catch (NumberFormatException ignored) { // thiếu hoặc không phải số: về trang 1
        }
        currentPage = Math.min(Math.max(currentPage, 1), totalPages); // ?page=999 hoặc ?page=-1 vẫn hợp lệ

        List<Video> videos = videoService.getByUser(user.getId(), currentPage);
        req.setAttribute("videos", videos);
        req.setAttribute("total", total);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
