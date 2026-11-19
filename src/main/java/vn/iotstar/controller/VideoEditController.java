package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.SessionUtil;
import vn.iotstar.util.UploadUtil;

/** /video/edit?id=... — id luôn lấy từ query string (cả khi POST) để vẫn đọc được khi upload vượt giới hạn. */
@WebServlet(urlPatterns = { "/video/edit" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class VideoEditController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String VIEW = "/WEB-INF/views/video/form-video.jsp";
    private final VideoService videoService = new VideoServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtil.currentUser(req);
        Video video = videoService.getOwned(req.getParameter("id"), user.getId());
        if (video == null) { // không tồn tại hoặc không phải video của mình
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }
        showForm(req, resp, video, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = SessionUtil.currentUser(req);

        Video current = videoService.getOwned(req.getParameter("id"), user.getId());
        if (current == null) {
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        Part part = null;
        try {
            part = req.getPart("poster");
        } catch (IllegalStateException e) { // file vượt giới hạn: dữ liệu vừa nhập không đọc được
            showForm(req, resp, current, VideoFormHelper.POSTER_TOO_BIG);
            return;
        }

        Video form = VideoFormHelper.readFields(req, categoryService);
        form.setVideoId(current.getVideoId());
        String error = VideoFormHelper.validate(form);
        String poster = null;
        if (error == null) {
            poster = UploadUtil.saveImage(part);
            if (poster == null && part != null && part.getSize() > 0) {
                error = VideoFormHelper.POSTER_ERROR;
            }
        }
        if (error != null) {
            form.setPoster(current.getPoster()); // chỉ để hiển thị lại ảnh hiện tại
            form.setUser(current.getUser());
            showForm(req, resp, form, error);
            return;
        }

        form.setPoster(poster); // null = giữ poster cũ
        if (!videoService.edit(form, user.getId())) {
            UploadUtil.deleteImage(poster); // không lưu được thì không để file mồ côi
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/video?msg=updated");
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Video video, String alert)
            throws ServletException, IOException {
        req.setAttribute("video", video);
        req.setAttribute("categories", categoryService.getAll());
        req.setAttribute("alert", alert);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
