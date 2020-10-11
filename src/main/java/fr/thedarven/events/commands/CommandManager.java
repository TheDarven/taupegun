package fr.thedarven.events.commands;

import fr.thedarven.events.commands.moles.*;
import fr.thedarven.events.commands.operators.*;
import fr.thedarven.main.TaupeGun;
import fr.thedarven.main.metier.Manager;

public class CommandManager extends Manager {

    public CommandManager(TaupeGun main) {
        super(main);
        loadCommands();
    }

    public void loadCommands(){
        // Operator's commands
        main.getCommand("revive").setExecutor(new ReviveCommand());
        main.getCommand("heal").setExecutor(new HealCommand());
        main.getCommand("g").setExecutor(new GCommand());
        main.getCommand("say").setExecutor(new SayCommand());
        main.getCommand("playerkill").setExecutor(new PlayerkillCommand());
        main.getCommand("timer").setExecutor(new TimerCommand());
        main.getCommand("rules").setExecutor(new ScenariosCommand());
        main.getCommand("scenarios").setExecutor(new ScenariosCommand());
        main.getCommand("start").setExecutor(new StartCommand());

        // User's commands
        main.getCommand("players").setExecutor(new PlayersCommand());
        main.getCommand("taupelist").setExecutor(new TaupelistCommand());
        main.getCommand("item").setExecutor(new ItemCommand());

        // Mole's commands
        main.getCommand("claim").setExecutor(new ClaimCommand());
        main.getCommand("reveal").setExecutor(new RevealCommand());
        main.getCommand("t").setExecutor(new TCommand());

        // Supermole's commands
        main.getCommand("superreveal").setExecutor(new SuperrevealCommand());
        main.getCommand("supert").setExecutor(new SupertCommand());

        main.getCommand("debug").setExecutor(new DebugCommand());
    }
}
