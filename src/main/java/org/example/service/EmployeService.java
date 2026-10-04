package org.example.service;

import org.example.dao.AbstractFacade;
import org.example.entities.Employe;

public class EmployeService extends AbstractFacade<Employe> {
    public EmployeService() {
        super(Employe.class);
    }
}
