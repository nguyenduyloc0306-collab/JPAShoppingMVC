package vn.iotstar.configs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConfig {
    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("jpa-hibernate-sqlserver");

    private JPAConfig() {}

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}
