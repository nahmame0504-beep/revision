package org.example.test;

import org.example.entities.*;
import org.example.service.*;
import org.example.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestComplet {
    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("   DEMONSTRATION COMPLETE DU TP JPA 2.2 / HIBERNATE 5.6");
        System.out.println("================================================================");

        try {
            // Initialisation des services
            CategorieService categorieService = new CategorieService();
            ProduitService produitService = new ProduitService();
            ClientService clientService = new ClientService();
            EmployeService employeService = new EmployeService();
            ServiceMetierService serviceMetierService = new ServiceMetierService();
            CommandeService commandeService = new CommandeService();
            LigneCommandeService ligneCommandeService = new LigneCommandeService();

            // -----------------------------------------------------------------
            // Etape 4 & 5 : Generic DAO & Entite Categorie
            // -----------------------------------------------------------------
            System.out.println("\n--- [Etape 4 & 5] Test Generic DAO & Categorie ---");
            Categorie catInformatique = new Categorie("Informatique");
            Categorie catBureautique = new Categorie("Bureautique");
            categorieService.create(catInformatique);
            categorieService.create(catBureautique);
            System.out.println("Categories creees : " + catInformatique.getId() + ", " + catBureautique.getId());

            // -----------------------------------------------------------------
            // Etape 6 & 7 : Entite Produit, Named Queries & Criteria API
            // -----------------------------------------------------------------
            System.out.println("\n--- [Etape 6 & 7] Produits, Named Queries & Criteria API ---");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = sdf.parse("2026-01-10");
            Date d2 = sdf.parse("2026-02-15");
            Date d3 = sdf.parse("2026-03-20");

            Produit p1 = new Produit("Lenovo ThinkPad", 12000.0, d1, catInformatique);
            Produit p2 = new Produit("Dell XPS 15", 16000.0, d2, catInformatique);
            Produit p3 = new Produit("Souris Sans Fil HP", 250.0, d3, catBureautique);
            Produit p4 = new Produit("Ecran UltraWide 34", 5500.0, d2, catInformatique);

            produitService.create(p1);
            produitService.create(p2);
            produitService.create(p3);
            produitService.create(p4);
            System.out.println("4 Produits inseres avec succes.");

            // Test Named Query 1: findByCategorie
            System.out.println("\n-> NamedQuery [Produit.findByCategorie] (Informatique):");
            produitService.findByCategorie(catInformatique).forEach(p -> System.out.println("   * " + p));

            // Test Named Query 2: findBetweenDates
            System.out.println("\n-> NamedQuery [Produit.findBetweenDates] (2026-01-01 -> 2026-02-28):");
            produitService.findBetweenDates(sdf.parse("2026-01-01"), sdf.parse("2026-02-28")).forEach(p -> System.out.println("   * " + p));

            // Test NamedNativeQuery: findByPrixMin
            System.out.println("\n-> NamedNativeQuery [Produit.findByPrixMin] (>= 5000 DH):");
            produitService.findByPrixMin(5000.0).forEach(p -> System.out.println("   * " + p));

            // Test Criteria API (Moteur dynamique)
            System.out.println("\n-> Criteria API dynamique (Cat=Informatique, PrixMin=10000, MotCle='Dell'):");
            List<Produit> critList = produitService.search(catInformatique, 10000.0, null, "Dell");
            critList.forEach(p -> System.out.println("   * " + p));

            // -----------------------------------------------------------------
            // Etape 8 : Pagination & Heritage JOINED
            // -----------------------------------------------------------------
            System.out.println("\n--- [Etape 8] Pagination & Heritage JPA (@Inheritance JOINED) ---");
            System.out.println("-> Pagination : Page 1 (taille 2) :");
            produitService.findPage(1, 2).forEach(p -> System.out.println("   [P1] " + p.getNom() + " (" + p.getPrix() + " DH)"));
            System.out.println("-> Pagination : Page 2 (taille 2) :");
            produitService.findPage(2, 2).forEach(p -> System.out.println("   [P2] " + p.getNom() + " (" + p.getPrix() + " DH)"));

            // Heritage : User -> Client / Employe
            Service depDev = new Service("Departement Ingenierie Logicielle");
            serviceMetierService.create(depDev);

            Employe employe = new Employe("omar.dev" + System.currentTimeMillis() + "@enterprise.ma", "passAdmin", "EMP-2026-01", depDev);
            employeService.create(employe);

            Client client = new Client("karim.tazi" + System.currentTimeMillis() + "@gmail.com", "tazi2026", "Tazi", "Karim");
            clientService.create(client);

            System.out.println("Employe cree : " + employe);
            System.out.println("Client cree  : " + client);

            // -----------------------------------------------------------------
            // Etape 9 : Relation N:N avec Entite Associative et Cle Composee
            // -----------------------------------------------------------------
            System.out.println("\n--- [Etape 9] N:N avec Cle Composee (@EmbeddedId) ---");
            Commande cmd = new Commande(new Date(), client);
            commandeService.create(cmd);

            LigneCommandeProduit lp1 = new LigneCommandeProduit(cmd, p1, 2, 11500.0);
            LigneCommandeProduit lp2 = new LigneCommandeProduit(cmd, p4, 1, 5200.0);
            ligneCommandeService.create(lp1);
            ligneCommandeService.create(lp2);

            System.out.println("Commande #" + cmd.getId() + " passee par " + client.getPrenom() + " " + client.getNom());
            System.out.println("   Ligne 1 : " + lp1);
            System.out.println("   Ligne 2 : " + lp2);

            // Test JOIN FETCH
            System.out.println("\n-> Test JOIN FETCH (Prevention N+1) sur Commande #" + cmd.getId() + " :");
            Commande cmdWithLines = commandeService.findWithLignes(cmd.getId());
            for (LigneCommandeProduit l : cmdWithLines.getLignesCommande()) {
                System.out.println("   * Produit: " + l.getProduit().getNom() + " | Qte: " + l.getQuantite() + " | Montant: " + l.getMontant() + " DH");
            }

            // -----------------------------------------------------------------
            // Etape 10 : Calcul du Chiffre d'Affaires Global en HQL
            // -----------------------------------------------------------------
            System.out.println("\n--- [Etape 10] Chiffre d'Affaires Global (HQL) ---");
            double caTotal = ligneCommandeService.getChiffreAffairesTotal();
            System.out.println("CHIFFRE D'AFFAIRES GLOBAL DU SYSTEME : " + String.format("%.2f", caTotal) + " DH");

            System.out.println("\n================================================================");
            System.out.println("TOUTES LES ETAPES DU TP SONT VALIDEES AVEC SUCCES !");
            System.out.println("================================================================");

        } catch (Exception ex) {
            System.err.println("Erreur lors de l'execution : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
