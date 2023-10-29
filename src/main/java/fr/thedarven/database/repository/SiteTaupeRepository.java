package fr.thedarven.database.repository;

import fr.thedarven.TaupeGun;
import fr.thedarven.database.model.SiteTaupe;

public class SiteTaupeRepository extends Repository<SiteTaupe> {

    public SiteTaupeRepository(TaupeGun main) {
        super(main);
    }

    @Override
    public void createTable() {

    }

    @Override
    public SiteTaupe update() {
        return null;
    }

    @Override
    public SiteTaupe save() {
        return null;
    }

}
