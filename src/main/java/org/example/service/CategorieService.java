package org.example.service;

import org.example.dao.AbstractFacade;
import org.example.entities.Categorie;

public class CategorieService extends AbstractFacade<Categorie> {
    public CategorieService() {
        super(Categorie.class);
    }
}
