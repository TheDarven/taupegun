package fr.thedarven.database.repository;

import fr.thedarven.TaupeGun;
import fr.thedarven.database.model.SiteJoueur;

public class SiteJoueurRepository extends Repository<SiteJoueur> {

    public SiteJoueurRepository(TaupeGun main) {
        super(main);
    }

    @Override
    public void createTable() {

    }

    @Override
    public SiteJoueur update() {
        return null;
    }

    @Override
    public SiteJoueur save() {
        return null;
    }

}
