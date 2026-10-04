package org.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "employes")
public class Employe extends User {

    private String matricule;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    public Employe() {}

    public Employe(String email, String password, String matricule) {
        super(email, password);
        this.matricule = matricule;
    }

    public Employe(String email, String password, String matricule, Service service) {
        super(email, password);
        this.matricule = matricule;
        this.service = service;
    }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }

    @Override
    public String toString() {
        return "Employe{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", matricule='" + matricule + '\'' +
                ", service=" + (service != null ? service.getNom() : null) +
                '}';
    }
}
