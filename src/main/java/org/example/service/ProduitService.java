package org.example.service;

import org.example.dao.AbstractFacade;
import org.example.entities.Categorie;
import org.example.entities.Produit;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProduitService extends AbstractFacade<Produit> {
    public ProduitService() {
        super(Produit.class);
    }

    public List<Produit> search(Categorie categorie, Double prixMin, Double prixMax, String motCle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Produit> cq = cb.createQuery(Produit.class);
            Root<Produit> root = cq.from(Produit.class);

            List<Predicate> predicates = new ArrayList<>();

            if (categorie != null) {
                predicates.add(cb.equal(root.get("categorie"), categorie));
            }
            if (prixMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("prix"), prixMin));
            }
            if (prixMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("prix"), prixMax));
            }
            if (motCle != null && !motCle.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nom")), "%" + motCle.toLowerCase() + "%"));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            return session.createQuery(cq).getResultList();
        } finally {
            session.close();
        }
    }

    public List<Produit> findByCategorie(Categorie categorie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createNamedQuery("Produit.findByCategorie", Produit.class)
                    .setParameter("categorie", categorie)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    public List<Produit> findBetweenDates(Date d1, Date d2) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createNamedQuery("Produit.findBetweenDates", Produit.class)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    public List<Produit> findByPrixMin(double minPrix) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createNamedQuery("Produit.findByPrixMin", Produit.class)
                    .setParameter("minPrix", minPrix)
                    .getResultList();
        } finally {
            session.close();
        }
    }

    public List<Produit> findPage(int page, int pageSize) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            int firstResult = (page - 1) * pageSize;
            return session.createQuery("FROM Produit p ORDER BY p.id ASC", Produit.class)
                    .setFirstResult(firstResult)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            session.close();
        }
    }
}
