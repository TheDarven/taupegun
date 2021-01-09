package fr.thedarven.events.commands;

import fr.thedarven.events.commands.moles.*;
import fr.thedarven.events.commands.operators.*;
import fr.thedarven.TaupeGun;
import fr.thedarven.main.metier.Manager;

public class CommandManager extends Manager {

    public CommandManager(TaupeGun main) {
        super(main);
        loadCommands();
    }

    public void loadCommands(){
        // Operator's commands
        main.getCommand("revive").setExecutor(new ReviveCommand(this.main));
        main.getCommand("heal").setExecutor(new HealCommand(this.main));
        main.getCommand("g").setExecutor(new GCommand(this.main));
        main.getCommand("say").setExecutor(new SayCommand());
        main.getCommand("playerkill").setExecutor(new PlayerkillCommand(this.main));
        main.getCommand("timer").setExecutor(new TimerCommand(this.main));
        main.getCommand("rules").setExecutor(new ScenariosCommand());
        main.getCommand("scenarios").setExecutor(new ScenariosCommand());
        main.getCommand("start").setExecutor(new StartCommand(this.main));

        // User's commands
        main.getCommand("players").setExecutor(new PlayersCommand(this.main));
        main.getCommand("taupelist").setExecutor(new TaupelistCommand(this.main));
        main.getCommand("item").setExecutor(new ItemCommand(this.main));

        // Mole's commands
        main.getCommand("claim").setExecutor(new ClaimCommand(this.main));
        main.getCommand("reveal").setExecutor(new RevealCommand(this.main));
        main.getCommand("t").setExecutor(new TCommand());

        // Supermole's commands
        main.getCommand("superreveal").setExecutor(new SuperrevealCommand(this.main));
        main.getCommand("supert").setExecutor(new SupertCommand());

        main.getCommand("debug").setExecutor(new DebugCommand());
    }
}
