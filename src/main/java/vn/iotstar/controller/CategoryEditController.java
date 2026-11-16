package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.UploadUtil;

@WebServlet(urlPatterns = { "/admin/category/edit" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 25)
public class CategoryEditController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String VIEW = "/WEB-INF/views/admin/category/edit-category.jsp";
    private final CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category = null;
        try {
            category = cateService.get(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException ignored) {
        }
        if (category == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
            return;
        }
        req.setAttribute("category", category);
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id;
        try {
            id = Integer.parseInt(req.getParameter("cateid"));
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
            return;
        }
        String name = req.getParameter("catename");
        if (name == null || name.isBlank()) {
            req.setAttribute("alert", "Tên danh mục không được rỗng");
            req.setAttribute("category", cateService.get(id));
            req.getRequestDispatcher(VIEW).forward(req, resp);
            return;
        }
        Category category = new Category();
        category.setCateid(id);
        category.setCatename(name.trim());
        category.setIcon(UploadUtil.saveImage(req.getPart("icon"))); // null = giữ ảnh cũ

        cateService.edit(category);
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}
