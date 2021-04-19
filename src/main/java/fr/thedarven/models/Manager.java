package fr.thedarven.models;

import fr.thedarven.TaupeGun;

public abstract class Manager {

    protected TaupeGun main;

    public Manager(TaupeGun main){
        this.main = main;
    }

}
