package org.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "ligne_commande_produit")
public class LigneCommandeProduit {

    @EmbeddedId
    private CommandeProduitPK pk;

    private int quantite;
    private double prixVente;

    @ManyToOne
    @JoinColumn(name = "produit", insertable = false, updatable = false)
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "commande", insertable = false, updatable = false)
    private Commande commande;

    public LigneCommandeProduit() {}

    public LigneCommandeProduit(Commande commande, Produit produit, int quantite, double prixVente) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
        this.prixVente = prixVente;
        this.pk = new CommandeProduitPK(commande.getId(), produit.getId());
    }

    public CommandeProduitPK getPk() {
        return pk;
    }

    public void setPk(CommandeProduitPK pk) {
        this.pk = pk;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        if (this.pk == null) {
            this.pk = new CommandeProduitPK();
        }
        if (produit != null) {
            this.pk.setProduit(produit.getId());
        }
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
        if (this.pk == null) {
            this.pk = new CommandeProduitPK();
        }
        if (commande != null) {
            this.pk.setCommande(commande.getId());
        }
    }

    public double getMontant() {
        return quantite * prixVente;
    }

    @Override
    public String toString() {
        return "LigneCommandeProduit{" +
                "pk=" + pk +
                ", quantite=" + quantite +
                ", prixVente=" + prixVente +
                ", montant=" + getMontant() +
                '}';
    }
}
