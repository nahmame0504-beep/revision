package org.example.service;

import org.example.dao.AbstractFacade;
import org.example.entities.LigneCommandeProduit;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

public class LigneCommandeService extends AbstractFacade<LigneCommandeProduit> {
    public LigneCommandeService() {
        super(LigneCommandeProduit.class);
    }

    /**
     * Calcul du Chiffre d'Affaires Global en HQL (Étape 10)
     */
    public double getChiffreAffairesTotal() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Double total = session.createQuery(
                    "SELECT SUM(l.quantite * l.prixVente) FROM LigneCommandeProduit l", Double.class
            ).getSingleResult();
            return total != null ? total : 0.0;
        } finally {
            session.close();
        }
    }
}
