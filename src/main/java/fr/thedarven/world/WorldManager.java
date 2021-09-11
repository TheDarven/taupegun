package fr.thedarven.world;

import fr.thedarven.TaupeGun;
import fr.thedarven.models.Manager;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.Objects;
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
        return Bukkit.getWorlds().size() >= 2
                ? Bukkit.getWorlds().get(1)
                : null;
    }

    public World getWorldEnd() {
        return Bukkit.getWorlds().size() >= 3
                ? Bukkit.getWorlds().get(2)
                : null;
    }

    public void initWorldsRules(){
        World world = getWorld();
        World worldNether = getWorldNether();
        World worldEnd = getWorldEnd();

        if (Objects.nonNull(world)) {
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

        if (Objects.nonNull(worldNether)) {
            worldNether.setGameRuleValue("spectatorsGenerateChunks", "false");
            worldNether.setGameRuleValue("naturalRegeneration", "false");
            worldNether.setGameRuleValue("announceAdvancements", "false");
            worldNether.setDifficulty(Difficulty.HARD);
        }

        if (Objects.nonNull(worldEnd)) {
            worldEnd.setGameRuleValue("naturalRegeneration", "false");
            worldEnd.setGameRuleValue("spectatorsGenerateChunks", "false");
            worldEnd.setGameRuleValue("announceAdvancements", "false");
            worldEnd.setDifficulty(Difficulty.HARD);
        }
    }

    public void buildLobby(){
        World world = getWorld();
        if (Objects.isNull(world))
            return;

        for (int x = -15; x < 16; x++){
            for (int z = -15; z < 16; z++){

                Random r = new Random();
                byte data = (byte) r.nextInt(15);

                Block block = world.getBlockAt(x, 200, z);
                block.setType(Material.STAINED_GLASS);
                block.setData(data);

                if (x == -15 || x == 15 || z == -15 || z == 15) {
                    for (int y = 201; y < 204; y++){
                        Block wallBlock = world.getBlockAt(x, y, z);
                        wallBlock.setType(Material.STAINED_GLASS_PANE);
                        wallBlock.setData((byte) 0);
                    }
                }
            }
        }
    }

    public void destroyLobby(){
        World world = getWorld();
        if (Objects.isNull(world))
            return;

        for (int x = -15; x <= 15; x++){
            for (int y = 200; y <= 203; y++){
                for (int z = -15; z <= 15; z++){
                    world.getBlockAt(x, y, z).setType(Material.AIR);
                }
            }
        }
    }

}
