package org.example.service;

import org.example.dao.AbstractFacade;
import org.example.entities.Service;

public class ServiceMetierService extends AbstractFacade<Service> {
    public ServiceMetierService() {
        super(Service.class);
    }
}
