package org.example.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "produits")
@NamedQueries({
    @NamedQuery(
        name = "Produit.findByCategorie",
        query = "FROM Produit p WHERE p.categorie = :categorie"
    ),
    @NamedQuery(
        name = "Produit.findBetweenDates",
        query = "FROM Produit p WHERE p.dateAchat BETWEEN :d1 AND :d2"
    )
})
@NamedNativeQuery(
    name = "Produit.findByPrixMin",
    query = "SELECT * FROM produits WHERE prix >= :minPrix",
    resultClass = Produit.class
)
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 150)
    private String nom;

    private double prix;

    @Temporal(TemporalType.DATE)
    private Date dateAchat;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<LigneCommandeProduit> lignesCommande = new ArrayList<>();

    public Produit() {}

    public Produit(String nom, double prix, Date dateAchat, Categorie categorie) {
        this.nom = nom;
        this.prix = prix;
        this.dateAchat = dateAchat;
        this.categorie = categorie;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public Date getDateAchat() { return dateAchat; }
    public void setDateAchat(Date dateAchat) { this.dateAchat = dateAchat; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public List<LigneCommandeProduit> getLignesCommande() { return lignesCommande; }
    public void setLignesCommande(List<LigneCommandeProduit> lignesCommande) { this.lignesCommande = lignesCommande; }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", dateAchat=" + dateAchat +
                ", categorie=" + (categorie != null ? categorie.getNom() : null) +
                '}';
    }
}
