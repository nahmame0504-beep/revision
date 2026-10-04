package org.example.test;

import org.example.entities.Categorie;
import org.example.entities.Produit;
import org.example.service.CategorieService;
import org.example.service.ProduitService;
import org.example.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestProduit {
    public static void main(String[] args) throws Exception {
        try {
            CategorieService categorieService = new CategorieService();
            ProduitService produitService = new ProduitService();

            // 1. Création catégorie
            Categorie cat = new Categorie("Informatique");
            categorieService.create(cat);

            // 2. Création de produits
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse("2026-01-15");
            Date date2 = sdf.parse("2026-02-20");
            Date date3 = sdf.parse("2026-03-10");

            Produit p1 = new Produit("Lenovo ThinkPad", 12000, date1, cat);
            Produit p2 = new Produit("Dell XPS 15", 18000, date2, cat);
            Produit p3 = new Produit("Souris Logitech", 350, date3, cat);

            produitService.create(p1);
            produitService.create(p2);
            produitService.create(p3);

            System.out.println("Produits crees avec succes !");

            // 3. Test NamedQuery : Produit.findByCategorie
            System.out.println("\n--- Test NamedQuery: Produit.findByCategorie ---");
            List<Produit> parCat = produitService.findByCategorie(cat);
            parCat.forEach(p -> System.out.println("  " + p));

            // 4. Test NamedQuery : Produit.findBetweenDates
            System.out.println("\n--- Test NamedQuery: Produit.findBetweenDates ---");
            List<Produit> entreDates = produitService.findBetweenDates(sdf.parse("2026-01-01"), sdf.parse("2026-02-25"));
            entreDates.forEach(p -> System.out.println("  " + p));

            // 5. Test NamedNativeQuery : Produit.findByPrixMin
            System.out.println("\n--- Test NamedNativeQuery: Produit.findByPrixMin (>= 10000) ---");
            List<Produit> prixMin = produitService.findByPrixMin(10000);
            prixMin.forEach(p -> System.out.println("  " + p));

            // 6. Test Criteria API (Recherche dynamique multi-critères)
            System.out.println("\n--- Test Criteria API: search(cat, 1000.0, 15000.0, 'think') ---");
            List<Produit> resultatCriteria = produitService.search(cat, 1000.0, 15000.0, "think");
            resultatCriteria.forEach(p -> System.out.println("  " + p));

            // 7. Test Pagination
            System.out.println("\n--- Test Pagination: Page 1 (taille 2) ---");
            List<Produit> page1 = produitService.findPage(1, 2);
            page1.forEach(p -> System.out.println("  " + p));

            System.out.println("\n--- Test Pagination: Page 2 (taille 2) ---");
            List<Produit> page2 = produitService.findPage(2, 2);
            page2.forEach(p -> System.out.println("  " + p));
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
