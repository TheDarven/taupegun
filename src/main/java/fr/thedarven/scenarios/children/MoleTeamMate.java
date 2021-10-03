package fr.thedarven.scenarios.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenarios.builders.InventoryGUI;
import fr.thedarven.scenarios.builders.OptionBoolean;
import fr.thedarven.teams.graph.MoleCreationGraph;
import fr.thedarven.teams.graph.MoleCreationNoMateGraph;
import fr.thedarven.teams.graph.MoleCreationWithMateGraph;
import org.bukkit.Material;

public class MoleTeamMate extends OptionBoolean {

    public MoleTeamMate(TaupeGun main, InventoryGUI pParent) {
        super(main, "Avec coéquipiers", "Active la possibilité d'être dans la même équipe de taupe que des coéquipiers.",
                "MENU_CONFIGURATION_MOLE_MATE", Material.POTATO_ITEM, pParent, false);
    }

    public MoleCreationGraph getMoleCreationGraph() {
        return this.value
                ? new MoleCreationWithMateGraph(this.main)
                : new MoleCreationNoMateGraph(this.main);
    }
}
