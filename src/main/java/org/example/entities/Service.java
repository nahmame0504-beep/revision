package org.example.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nom;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<Employe> employes = new ArrayList<>();

    public Service() {}

    public Service(String nom) {
        this.nom = nom;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<Employe> getEmployes() { return employes; }
    public void setEmployes(List<Employe> employes) { this.employes = employes; }

    @Override
    public String toString() {
        return "Service{" + "id=" + id + ", nom='" + nom + '\'' + '}';
    }
}
