package fr.thedarven.database.repository;

import fr.thedarven.TaupeGun;

public abstract class Repository<T> {

    protected TaupeGun main;

    public Repository(TaupeGun main) {
        this.main = main;
    }

    public abstract void createTable();

    public abstract T update();

    public abstract T save();

}
