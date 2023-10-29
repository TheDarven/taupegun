package fr.thedarven.scenario.children;

import fr.thedarven.TaupeGun;
import fr.thedarven.scenario.builders.InventoryGUI;
import fr.thedarven.scenario.builders.OptionBoolean;
import fr.thedarven.team.graph.MoleCreationGraph;
import fr.thedarven.team.graph.MoleCreationNoMateGraph;
import fr.thedarven.team.graph.MoleCreationWithMateGraph;
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
