package com.flight.manager.main;

import com.flight.manager.config.JPAUtil;
import jakarta.persistence.EntityManager;

public class TestConnection {
    public static void main(String[] args) {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            System.out.println("Connection to H2 successful!");
            em.close();
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        } finally {
            JPAUtil.close();
        }
    }
}
