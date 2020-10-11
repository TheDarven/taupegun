package fr.thedarven.main.metier;

import fr.thedarven.main.TaupeGun;

public abstract class Manager {

    protected TaupeGun main;

    public Manager(TaupeGun main){
        this.main = main;
    }

}
