package fr.thedarven.scenario.configuration;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builder.ConfigurationInventory;
import fr.thedarven.scenario.builder.OptionBoolean;
import fr.thedarven.team.graph.MoleCreationGraph;
import fr.thedarven.team.graph.MoleCreationNoMateGraph;
import fr.thedarven.team.graph.MoleCreationWithMateGraph;
import org.bukkit.Material;

public class MoleTeamMate extends OptionBoolean {

    public MoleTeamMate(TaupeGun main, ConfigurationInventory pParent) {
        super(main, "Avec coéquipiers", "Active la possibilité d'être dans la même équipe de taupe que des coéquipiers.",
                "MENU_CONFIGURATION_MOLE_MATE", Material.POTATO_ITEM, pParent, false);
    }

    public MoleCreationGraph getMoleCreationGraph() {
        return this.value
                ? new MoleCreationWithMateGraph(this.main)
                : new MoleCreationNoMateGraph(this.main);
    }
}
