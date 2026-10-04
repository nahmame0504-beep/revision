package org.example.test;

import org.example.entities.*;
import org.example.service.*;
import org.example.util.HibernateUtil;

import java.util.Date;

public class TestCommande {
    public static void main(String[] args) {
        try {
            CategorieService categorieService = new CategorieService();
            ProduitService produitService = new ProduitService();
            ClientService clientService = new ClientService();
            CommandeService commandeService = new CommandeService();
            LigneCommandeService ligneService = new LigneCommandeService();

            // 1. Préparation Client & Produits
            Client client = new Client("yassine.bennani" + System.currentTimeMillis() + "@mail.com", "pass789", "Bennani", "Yassine");
            clientService.create(client);

            Categorie cat = new Categorie("Electronique");
            categorieService.create(cat);

            Produit p1 = new Produit("Ecran 4K LG", 3500.0, new Date(), cat);
            Produit p2 = new Produit("Clavier Mecanique", 800.0, new Date(), cat);
            produitService.create(p1);
            produitService.create(p2);

            // 2. Création d'une commande
            Commande cmd = new Commande(new Date(), client);
            commandeService.create(cmd);

            // 3. Ajout de lignes de commande avec clé primaire composite
            LigneCommandeProduit l1 = new LigneCommandeProduit(cmd, p1, 2, 3400.0);
            LigneCommandeProduit l2 = new LigneCommandeProduit(cmd, p2, 3, 750.0);
            ligneService.create(l1);
            ligneService.create(l2);

            System.out.println("Commande ID=" + cmd.getId() + " creee avec 2 lignes de produits.");
            System.out.println("   Ligne 1 : " + l1.getQuantite() + "x " + p1.getNom() + " @ " + l1.getPrixVente() + " = " + l1.getMontant() + " DH");
            System.out.println("   Ligne 2 : " + l2.getQuantite() + "x " + p2.getNom() + " @ " + l2.getPrixVente() + " = " + l2.getMontant() + " DH");

            // 4. Test JOIN FETCH (Prévention du problème N+1)
            System.out.println("\n--- Test JOIN FETCH Commande + Lignes ---");
            Commande cmdFetched = commandeService.findWithLignes(cmd.getId());
            System.out.println("Commande ID: " + cmdFetched.getId() + ", Date: " + cmdFetched.getDate());
            for (LigneCommandeProduit ligne : cmdFetched.getLignesCommande()) {
                System.out.println("  - Produit: " + (ligne.getProduit() != null ? ligne.getProduit().getNom() : "ID=" + ligne.getPk().getProduit())
                        + ", Qte: " + ligne.getQuantite() + ", Sous-total: " + ligne.getMontant() + " DH");
            }

            // 5. Calcul du Chiffre d'Affaires Global HQL (Étape 10)
            double caTotal = ligneService.getChiffreAffairesTotal();
            System.out.println("\nChiffre d'Affaires Total (HQL) : " + caTotal + " DH");
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
