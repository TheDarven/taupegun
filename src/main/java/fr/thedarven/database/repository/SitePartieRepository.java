package fr.thedarven.database.repository;

import fr.thedarven.TaupeGun;
import fr.thedarven.database.model.SitePartie;

public class SitePartieRepository extends Repository<SitePartie> {

    public SitePartieRepository(TaupeGun main) {
        super(main);
    }

    @Override
    public void createTable() {

    }

    @Override
    public SitePartie update() {
        return null;
    }

    @Override
    public SitePartie save() {
        return null;
    }

}
