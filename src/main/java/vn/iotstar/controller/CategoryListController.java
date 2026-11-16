package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

@WebServlet(urlPatterns = { "/admin/category/list" })
public class CategoryListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final CategoryService cateService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String keyword = req.getParameter("keyword");
        List<Category> cateList = (keyword == null || keyword.isBlank())
                ? cateService.getAll()
                : cateService.search(keyword.trim());
        req.setAttribute("cateList", cateList);
        req.setAttribute("keyword", keyword);
        req.getRequestDispatcher("/WEB-INF/views/admin/category/list-category.jsp").forward(req, resp);
    }
}
