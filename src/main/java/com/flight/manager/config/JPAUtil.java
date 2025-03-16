package com.flight.manager.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    
    private static final String TEST_PU_NAME =  "flightTestPU";
    private static final String PU_NAME =  "flightPU";

    private static EntityManagerFactory emf;
    private static String currentPUName = JPAUtil.PU_NAME;

    public static void setTestMode() {
        currentPUName = JPAUtil.TEST_PU_NAME;
    }

    private static EntityManagerFactory getCurrentFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(currentPUName);
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getCurrentFactory().createEntityManager();
    }

    public static void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
