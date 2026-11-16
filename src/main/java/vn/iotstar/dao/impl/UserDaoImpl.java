package vn.iotstar.dao.impl;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.configs.JPAConfig;
import vn.iotstar.dao.UserDao;
import vn.iotstar.entity.User;

public class UserDaoImpl implements UserDao {

    @Override
    public void insert(User user) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(user);
            trans.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (trans.isActive())
                trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public User get(String username) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class);
            q.setParameter("username", username);
            List<User> list = q.getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean checkExistEmail(String email) {
        return exists("email", email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return exists("userName", username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return exists("phone", phone);
    }

    private boolean exists(String field, String value) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery("SELECT COUNT(u) FROM User u WHERE u." + field + " = :v", Long.class);
            q.setParameter("v", value);
            return q.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public void updateOtp(String username, String otpHash, LocalDateTime expiry) {
        executeUpdate(
                "UPDATE User u SET u.otpHash = :h, u.otpExpiry = :e, u.otpAttempts = 0 "
                        + "WHERE u.userName = :un",
                "h", otpHash, "e", expiry, "un", username);
    }

    @Override
    public void increaseOtpAttempts(String username) {
        executeUpdate(
                "UPDATE User u SET u.otpAttempts = u.otpAttempts + 1 WHERE u.userName = :un",
                "un", username);
    }

    @Override
    public void activate(String username) {
        executeUpdate(
                "UPDATE User u SET u.active = true, u.otpHash = null, u.otpExpiry = null, "
                        + "u.otpAttempts = 0 WHERE u.userName = :un",
                "un", username);
    }

    // params: lần lượt tên tham số, giá trị, tên, giá trị...
    private void executeUpdate(String jpql, Object... params) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            jakarta.persistence.Query q = em.createQuery(jpql);
            for (int i = 0; i < params.length; i += 2) {
                q.setParameter((String) params[i], params[i + 1]);
            }
            q.executeUpdate();
            trans.commit();
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (trans.isActive())
                trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void updatePassword(String username, String passwordHash) {
        executeUpdate(
                "UPDATE User u SET u.passWord = :p WHERE u.userName = :un",
                "p", passwordHash, "un", username);
    }

    @Override
    public User getByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            q.setParameter("email", email);
            q.setMaxResults(1);
            List<User> list = q.getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void updateResetOtp(String username, String resetHash, LocalDateTime expiry) {
        executeUpdate(
                "UPDATE User u SET u.resetHash = :h, u.resetExpiry = :e, u.resetAttempts = 0 "
                        + "WHERE u.userName = :un",
                "h", resetHash, "e", expiry, "un", username);
    }

    @Override
    public void increaseResetAttempts(String username) {
        executeUpdate(
                "UPDATE User u SET u.resetAttempts = u.resetAttempts + 1 WHERE u.userName = :un",
                "un", username);
    }

    @Override
    public void resetPassword(String username, String passwordHash) {
        executeUpdate(
                "UPDATE User u SET u.passWord = :p, u.resetHash = null, u.resetExpiry = null, "
                        + "u.resetAttempts = 0 WHERE u.userName = :un",
                "p", passwordHash, "un", username);
    }
}
