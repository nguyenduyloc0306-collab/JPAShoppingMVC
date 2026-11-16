package vn.iotstar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = "/image") // /image?fname=abc.png
public class DownloadImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fname = req.getParameter("fname");
        if (fname == null || fname.isBlank()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path file = Paths.get(Constant.DIR, Paths.get(fname).getFileName().toString());
        if (!Files.exists(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String mime = getServletContext().getMimeType(file.getFileName().toString());
        resp.setContentType(mime != null ? mime : "image/jpeg");
        Files.copy(file, resp.getOutputStream());
    }
}
