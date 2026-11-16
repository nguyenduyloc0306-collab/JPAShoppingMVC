package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.impl.CategoryDaoImpl;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.util.UploadUtil;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public void insert(Category category) {
        categoryDao.insert(category);
    }

    @Override
    public void edit(Category newCategory) {
        Category old = categoryDao.get(newCategory.getCateid());
        if (old == null) {
            return;
        }
        old.setCatename(newCategory.getCatename());
        if (newCategory.getIcon() != null) {
            UploadUtil.deleteImage(old.getIcon()); // xóa ảnh cũ
            old.setIcon(newCategory.getIcon());
        }
        categoryDao.edit(old);
    }

    @Override
    public void delete(int id) {
        try {
            Category old = categoryDao.get(id);
            categoryDao.delete(id);
            if (old != null) {
                UploadUtil.deleteImage(old.getIcon());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Category get(int id) {
        return categoryDao.get(id);
    }

    @Override
    public Category get(String name) {
        return categoryDao.get(name);
    }

    @Override
    public List<Category> getAll() {
        return categoryDao.getAll();
    }

    @Override
    public List<Category> search(String keyword) {
        return categoryDao.search(keyword);
    }
}
