package org.example.test;

import org.example.entities.Client;
import org.example.entities.Employe;
import org.example.entities.Service;
import org.example.service.ClientService;
import org.example.service.EmployeService;
import org.example.service.ServiceMetierService;
import org.example.util.HibernateUtil;

import java.util.List;

public class TestHeritage {
    public static void main(String[] args) {
        try {
            ClientService clientService = new ClientService();
            EmployeService employeService = new EmployeService();
            ServiceMetierService serviceMetierService = new ServiceMetierService();

            // 1. Création d'un Service
            Service depIT = new Service("Département IT");
            serviceMetierService.create(depIT);

            // 2. Création d'un Employé
            Employe emp = new Employe("ali.dupont" + System.currentTimeMillis() + "@company.com", "pass123", "EMP-9001", depIT);
            employeService.create(emp);
            System.out.println("Employe cree : ID=" + emp.getId() + ", Matricule=" + emp.getMatricule());

            // 3. Création d'un Client
            Client client = new Client("sara.alami" + System.currentTimeMillis() + "@gmail.com", "secret456", "Alami", "Sara");
            clientService.create(client);
            System.out.println("Client cree : ID=" + client.getId() + ", Nom=" + client.getNom());

            // 4. Liste des clients
            List<Client> clients = clientService.findAll();
            System.out.println("\n--- Liste des clients en base ---");
            clients.forEach(c -> System.out.println("  " + c));

            // 5. Liste des employés
            List<Employe> employes = employeService.findAll();
            System.out.println("\n--- Liste des employés en base ---");
            employes.forEach(e -> System.out.println("  " + e));
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
