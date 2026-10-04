package org.example.service;

import org.example.dao.AbstractFacade;
import org.example.entities.Client;

public class ClientService extends AbstractFacade<Client> {
    public ClientService() {
        super(Client.class);
    }
}
