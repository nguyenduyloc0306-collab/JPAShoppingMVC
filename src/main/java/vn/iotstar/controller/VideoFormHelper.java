package vn.iotstar.controller;

import jakarta.servlet.http.HttpServletRequest;
import vn.iotstar.entity.Video;
import vn.iotstar.service.CategoryService;
import vn.iotstar.util.VideoUtil;

/** Đọc và kiểm tra dữ liệu form video dùng chung cho thêm và sửa. */
final class VideoFormHelper {
    static final String POSTER_ERROR = "Poster phải là ảnh (png, jpg, jpeg, gif, webp)";
    static final String POSTER_TOO_BIG = "Ảnh poster tối đa 5MB";
    static final String VIDEO_ERROR = "File video không hợp lệ (chỉ hỗ trợ mp4, webm, ogg, mov)";
    static final String VIDEO_TOO_BIG = "File video vượt quá dung lượng cho phép";

    private VideoFormHelper() {}

    static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    /** Gọi sau khi đã gọi req.getPart(...) (form multipart) để các tham số văn bản đã được đọc. */
    static Video readFields(HttpServletRequest req, CategoryService categoryService) {
        Video v = new Video();
        v.setTitle(trim(req.getParameter("title")));
        v.setUrl(trim(req.getParameter("url")));
        v.setDescription(trim(req.getParameter("description")));
        v.setActive(req.getParameter("active") != null);
        String cid = trim(req.getParameter("cateid"));
        if (!cid.isEmpty()) {
            try {
                v.setCategory(categoryService.get(Integer.parseInt(cid))); // null nếu không tồn tại
            } catch (NumberFormatException ignored) {
            }
        }
        return v;
    }

    static String validate(Video v) {
        return validate(v, false);
    }

    /** Trả thông báo lỗi, hoặc null nếu hợp lệ. */
    static String validate(Video v, boolean hasUploadedVideo) {
        if (v.getTitle().isEmpty()) {
            return "Vui lòng nhập tiêu đề video";
        }
        if (v.getTitle().length() > 255) {
            return "Tiêu đề tối đa 255 ký tự";
        }

        String url = trim(v.getUrl());
        if (!hasUploadedVideo) {
            // Không upload file -> bắt buộc phải có link hợp lệ
            if (url.isEmpty()) {
                return "Vui lòng nhập link video hoặc chọn file video";
            }
            if (url.length() > 500) {
                return "Link video tối đa 500 ký tự";
            }
            if (!VideoUtil.isSupported(url)) {
                return "Link không hợp lệ. Chỉ hỗ trợ link YouTube hoặc link trực tiếp tới file .mp4, .webm, .ogg";
            }
        } else if (!url.isEmpty() && url.length() > 500) {
            return "Link video tối đa 500 ký tự";
        }

        if (v.getDescription().length() > 5000) {
            return "Mô tả tối đa 5000 ký tự";
        }
        return null;
    }
}