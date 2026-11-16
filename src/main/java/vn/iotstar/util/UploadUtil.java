package vn.iotstar.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import jakarta.servlet.http.Part;

public class UploadUtil {
    private UploadUtil() {}

    /** Lưu file ảnh vào thư mục upload, trả về tên file mới; null nếu không có file hợp lệ. */
    public static String saveImage(Part part) throws IOException {
        if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null) {
            return null;
        }
        String original = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        int idx = original.lastIndexOf('.');
        String ext = idx >= 0 ? original.substring(idx + 1).toLowerCase() : "";
        if (!ext.matches("png|jpg|jpeg|gif|webp")) {
            return null;
        }
        Files.createDirectories(Paths.get(Constant.DIR));
        String name = System.currentTimeMillis() + "." + ext;
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, Paths.get(Constant.DIR, name), StandardCopyOption.REPLACE_EXISTING);
        }
        return name;
    }

    public static void deleteImage(String fileName) {
        if (fileName == null || fileName.isBlank() || fileName.startsWith("http")
                || fileName.equals(Constant.DEFAULT_ICON)) {
            return;
        }
        try {
            Path p = Paths.get(Constant.DIR, Paths.get(fileName).getFileName().toString());
            Files.deleteIfExists(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
