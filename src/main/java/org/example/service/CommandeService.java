package org.example.service;

import org.example.dao.AbstractFacade;
import org.example.entities.Commande;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class CommandeService extends AbstractFacade<Commande> {
    public CommandeService() {
        super(Commande.class);
    }

    /**
     * Recherche une commande avec ses lignes et produits en évitant le problème N+1 (JOIN FETCH)
     */
    public Commande findWithLignes(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                    "SELECT DISTINCT c FROM Commande c " +
                    "LEFT JOIN FETCH c.lignesCommande l " +
                    "LEFT JOIN FETCH l.produit " +
                    "WHERE c.id = :id", Commande.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}
