package fr.thedarven.database.dao;

import fr.thedarven.TaupeGun;

public abstract class DAO<T> {

    protected TaupeGun main;

    public DAO(TaupeGun main) {
        this.main = main;
    }

    public abstract void createTable();

    public abstract T update();

    public abstract T save();

}
