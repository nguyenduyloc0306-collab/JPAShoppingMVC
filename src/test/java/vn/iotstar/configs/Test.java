package vn.iotstar.configs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Video;

/** Run As > Java Application để test cấu hình JPA (tự tạo bảng Videos nếu chưa có). */
public class Test {
    public static void main(String[] args) {
        EntityManager enma = JPAConfig.getEntityManager();
        EntityTransaction trans = enma.getTransaction();

        Category cate = new Category();
        cate.setCatename("Iphone");
        cate.setIcon("abc.jpg");

        Video video = new Video();
        video.setVideoId("v" + System.currentTimeMillis());
        video.setTitle("test");
        video.setCategory(cate);

        try {
            trans.begin();
            enma.persist(cate);
            enma.persist(video);
            trans.commit();
            System.out.println("OK: đã thêm category id = " + cate.getCateid());
        } catch (Exception e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            enma.close();
        }
        System.exit(0);
    }
}
