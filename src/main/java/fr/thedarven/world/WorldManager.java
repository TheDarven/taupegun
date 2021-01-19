package fr.thedarven.world;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.Random;

public class WorldManager extends Manager {

    public WorldManager(TaupeGun main) {
        super(main);
        initWorldsRules();
    }

    public World getWorld() {
        return Bukkit.getWorlds().get(0);
    }

    public World getWorldNether() {
        return Bukkit.getWorlds().get(1);
    }

    public World getWorldEnd() {
        return Bukkit.getWorlds().get(2);
    }

    public void initWorldsRules(){
        World world = getWorld();
        World worldNether = getWorldNether();
        World worldEnd = getWorldEnd();

        if(world != null) {
            world.setTime(6000);
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("spectatorsGenerateChunks", "true");
            world.setGameRuleValue("naturalRegeneration", "false");
            world.setGameRuleValue("announceAdvancements", "false");
            world.setDifficulty(Difficulty.HARD);
            world.setSpawnLocation(0, 64, 0);
            world.setStorm(false);
            world.setThundering(false);

            WorldBorder border = world.getWorldBorder();
            border.setDamageAmount(1.0);
            border.setCenter(0.0, 0.0);
            border.setWarningDistance(20);
            border.setSize(this.main.getScenariosManager().wallSizeBefore.getDiameter());
        }

        if(worldNether != null) {
            worldNether.setGameRuleValue("spectatorsGenerateChunks", "false");
            worldNether.setGameRuleValue("naturalRegeneration", "false");
            worldNether.setGameRuleValue("announceAdvancements", "false");
            worldNether.setDifficulty(Difficulty.HARD);
        }

        if(worldEnd != null) {
            worldEnd.setGameRuleValue("naturalRegeneration", "false");
            worldEnd.setGameRuleValue("spectatorsGenerateChunks", "false");
            worldEnd.setGameRuleValue("announceAdvancements", "false");
            worldEnd.setDifficulty(Difficulty.HARD);
        }
    }

    public void buildLobby(){
        World world = getWorld();
        if(world == null)
            return;

        for(int xCord = -15; xCord < 16; xCord++){
            for(int cordZ = -15; cordZ < 16; cordZ++){

                Random r = new Random();
                byte data = (byte) r.nextInt(15);

                Block block = world.getBlockAt(xCord, 200, cordZ);
                block.setType(Material.STAINED_GLASS);
                block.setData(data);

                if(xCord == -15 || xCord == 15 || cordZ == -15 || cordZ == 15){
                    for(int cordY = 201; cordY < 204; cordY++){
                        Block wallBlock = world.getBlockAt(xCord, cordY, cordZ);
                        wallBlock.setType(Material.STAINED_GLASS_PANE);
                        wallBlock.setData((byte)0);
                    }
                }
            }
        }
    }

    public void destroyLobby(){
        World world = getWorld();
        if(world == null)
            return;

        for(int x = -15; x <= 15; x++){
            for (int y = 200; y <= 203; y++){
                for (int z = -15; z <= 15; z++){
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

}
