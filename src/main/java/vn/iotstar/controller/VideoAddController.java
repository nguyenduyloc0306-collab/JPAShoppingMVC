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

@WebServlet(urlPatterns = { "/video/add" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 5,        // 5MB ghi ra đĩa tạm
    maxFileSize = 1024L * 1024 * 200,           // Tối đa 200MB / 1 file
    maxRequestSize = 1024L * 1024 * 210         // Tối đa 210MB / toàn bộ request (video + poster)
)
public class VideoAddController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String VIEW = "/WEB-INF/views/video/form-video.jsp";
    private final VideoService videoService = new VideoServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Video blank = new Video();
        blank.setActive(true);
        showForm(req, resp, blank, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = SessionUtil.currentUser(req);

        Part posterPart = null;
        Part videoPart = null;
        try {
            posterPart = req.getPart("poster");
            videoPart = req.getPart("videoFile");
        } catch (IllegalStateException e) {
            Video blank = new Video();
            blank.setActive(true);
            showForm(req, resp, blank, "File tải lên vượt quá dung lượng cho phép (tối đa 200MB).");
            return;
        }

        Video video = VideoFormHelper.readFields(req, categoryService);

        boolean hasVideoFile = videoPart != null && videoPart.getSize() > 0;
        String typedUrl = video.getUrl() != null ? video.getUrl().trim() : "";

        // Kiểm tra: phải có ít nhất link hoặc file video
        if (!hasVideoFile && typedUrl.isEmpty()) {
            showForm(req, resp, video, "Vui lòng nhập link video hoặc chọn file video.");
            return;
        }

        // Nếu người dùng nhập link thì validate link như cũ
        String error = null;
        if (!typedUrl.isEmpty()) {
            error = VideoFormHelper.validate(video);
        }

        // Lưu file video nếu có chọn file (ưu tiên file nếu người dùng vừa nhập link vừa chọn file, hoặc tùy quy ước của bạn)
        if (error == null && hasVideoFile) {
            String savedVideoName = saveVideoFile(videoPart); // xem hàm gợi ý bên dưới
            if (savedVideoName == null) {
                error = "File video không hợp lệ hoặc lưu thất bại.";
            } else {
                // Lưu tên/đường dẫn file vào cột url (hoặc lưu theo chuẩn servlet phát video của bạn)
                video.setUrl(savedVideoName);
            }
        }

        // Lưu poster nếu có
        if (error == null && posterPart != null && posterPart.getSize() > 0) {
            String poster = UploadUtil.saveImage(posterPart);
            if (poster == null) {
                error = VideoFormHelper.POSTER_ERROR;
            } else {
                video.setPoster(poster);
            }
        }

        if (error != null) {
            showForm(req, resp, video, error);
            return;
        }

        videoService.insert(video, user.getId());
        resp.sendRedirect(req.getContextPath() + "/video?msg=added");
    }

    private String saveVideoFile(Part part) throws IOException {
        String submitted = part.getSubmittedFileName();
        if (submitted == null || submitted.isBlank()) return null;

        String lower = submitted.toLowerCase();
        if (!lower.endsWith(".mp4") && !lower.endsWith(".webm") && !lower.endsWith(".ogg") && !lower.endsWith(".mov")) {
            return null;
        }

        String contentType = part.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            return null;
        }

        String ext = lower.substring(lower.lastIndexOf('.'));
        String fileName = java.util.UUID.randomUUID().toString() + ext;

        // Đổi thư mục lưu trữ cho khớp cấu hình máy bạn (vd: giống cách UploadUtil lưu ảnh)
        java.nio.file.Path dir = java.nio.file.Paths.get("C:/uploads/videos");
        java.nio.file.Files.createDirectories(dir);
        part.write(dir.resolve(fileName).toString());
        return fileName;
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Video video, String alert)
            throws ServletException, IOException {
        req.setAttribute("video", video);
        req.setAttribute("categories", categoryService.getAll());
        req.setAttribute("alert", alert);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }
}
