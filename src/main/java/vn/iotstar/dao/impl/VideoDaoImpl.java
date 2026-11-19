package vn.iotstar.dao.impl;

import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.configs.JPAConfig;
import vn.iotstar.dao.VideoDao;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;

public class VideoDaoImpl implements VideoDao {

    // JOIN FETCH để JSP đọc được user/category sau khi EntityManager đã đóng, và tránh N+1
    private static final String SELECT = "SELECT v FROM Video v LEFT JOIN FETCH v.user LEFT JOIN FETCH v.category ";
    // videoId là tiêu chí phụ để thứ tự (và phân trang) luôn ổn định khi trùng thời gian tạo
    private static final String ORDER = " ORDER BY v.createdDate DESC, v.videoId";

    private void inTransaction(Consumer<EntityManager> work) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            work.accept(em);
            trans.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Video video, int userId) {
        inTransaction(em -> {
            video.setUser(em.getReference(User.class, userId));
            if (video.getCategory() != null) {
                video.setCategory(em.getReference(Category.class, video.getCategory().getCateid()));
            }
            em.persist(video);
        });
    }

    @Override
    public void update(Video video) {
        inTransaction(em -> em.merge(video));
    }

    @Override
    public void delete(String id) {
        inTransaction(em -> {
            Video video = em.find(Video.class, id);
            if (video != null) {
                em.remove(video);
            }
        });
    }

    @Override
    public Video get(String id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            List<Video> list = em.createQuery(SELECT + "WHERE v.videoId = :id", Video.class)
                    .setParameter("id", id)
                    .getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findLatestActive(int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Video> q = em.createQuery(SELECT + "WHERE v.active = true" + ORDER, Video.class);
            q.setMaxResults(limit);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findByUser(int userId, int page, int size) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Video> q = em.createQuery(SELECT + "WHERE v.user.id = :uid" + ORDER, Video.class);
            q.setParameter("uid", userId);
            q.setFirstResult((Math.max(page, 1) - 1) * size);
            q.setMaxResults(size);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByUser(int userId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(v) FROM Video v WHERE v.user.id = :uid", Long.class)
                    .setParameter("uid", userId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public void increaseViews(String id) {
        // Cập nhật nguyên tử trong DB, không đọc-rồi-ghi nên không mất lượt khi nhiều người xem cùng lúc
        inTransaction(em -> em.createQuery("UPDATE Video v SET v.views = v.views + 1 WHERE v.videoId = :id")
                .setParameter("id", id)
                .executeUpdate());
    }
}
