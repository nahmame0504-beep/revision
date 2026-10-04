package org.example.test;

import org.example.entities.Categorie;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class TestCategorie {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // 1. Recuperation et test de la connexion SQL (JDBC sous-jacente)
        session.doWork(connection -> {
            try {
                System.out.println("=================================================");
                System.out.println("Connexion SQL etablie avec succes !");
                System.out.println("URL SGBD    : " + connection.getMetaData().getURL());
                System.out.println("Utilisateur : " + connection.getMetaData().getUserName());
                System.out.println("Moteur SGBD : " + connection.getMetaData().getDatabaseProductName() + " " + connection.getMetaData().getDatabaseProductVersion());
                System.out.println("Catalogue   : " + connection.getCatalog());
                System.out.println("=================================================");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la recuperation de la connexion SQL : " + e.getMessage());
            }
        });

        // 2. Insertion JPA de la categorie
        Transaction tx = session.beginTransaction();

        Categorie c = new Categorie("Ordinateurs");
        session.save(c); // Hibernate prepare le SQL INSERT

        tx.commit(); // Validation en BDD
        session.close();

        System.out.println("Categorie inseree avec l'ID : " + c.getId());

        HibernateUtil.shutdown();
    }
}
