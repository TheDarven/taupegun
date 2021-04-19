package fr.thedarven.events.commands;

import fr.thedarven.events.commands.moles.*;
import fr.thedarven.events.commands.operators.*;
import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;

public class CommandManager extends Manager {

    private StartCommand startCommand;

    public CommandManager(TaupeGun main) {
        super(main);
        loadCommands();
    }

    public void loadCommands(){
        // Operator's commands
        main.getCommand("revive").setExecutor(new ReviveCommand(this.main));
        main.getCommand("heal").setExecutor(new HealCommand(this.main));
        main.getCommand("g").setExecutor(new GCommand(this.main));
        main.getCommand("say").setExecutor(new SayCommand(this.main));
        main.getCommand("playerkill").setExecutor(new PlayerkillCommand(this.main));
        main.getCommand("timer").setExecutor(new TimerCommand(this.main));
        main.getCommand("rules").setExecutor(new ScenariosCommand(this.main));
        main.getCommand("scenarios").setExecutor(new ScenariosCommand(this.main));
        this.startCommand = new StartCommand(this.main);
        main.getCommand("start").setExecutor(this.startCommand);

        // User's commands
        main.getCommand("players").setExecutor(new PlayersCommand(this.main));
        main.getCommand("taupelist").setExecutor(new TaupelistCommand(this.main));
        main.getCommand("item").setExecutor(new ItemCommand(this.main));
        main.getCommand("coords").setExecutor(new CoordsCommand(this.main));

        // Mole's commands
        main.getCommand("claim").setExecutor(new ClaimCommand(this.main));
        main.getCommand("reveal").setExecutor(new RevealCommand(this.main));
        main.getCommand("t").setExecutor(new TCommand(this.main));

        // Supermole's commands
        main.getCommand("superreveal").setExecutor(new SuperrevealCommand(this.main));
        main.getCommand("supert").setExecutor(new SupertCommand(this.main));

        main.getCommand("debug").setExecutor(new DebugCommand(this.main));
    }

    public StartCommand getStartCommand() {
        return startCommand;
    }

}
