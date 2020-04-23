package fr.thedarven.utils;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;

public class BiomeEdit { 
	public static void changeBiome(String Biome){
		try {
			String cheminNms = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            Class<?> classBiome = Class.forName(cheminNms + ".BiomeBase");
            Field terrainPlaine = classBiome.getDeclaredField(Biome);
            terrainPlaine.setAccessible(true);
            Object biomePlaine = terrainPlaine.get(null);

            Field terrainBiomes = classBiome.getDeclaredField("biomes");
            terrainBiomes.setAccessible(true);
            Object[] biomes = (Object[]) terrainBiomes.get(null);

            for(int i = 0;i<biomes.length;i++){
            	if(biomes[i].getClass().getSimpleName().equals("BiomeOcean") || biomes[i].getClass().getSimpleName().equals("BiomeFrozenOcean") || biomes[i].getClass().getSimpleName().equals("BiomeDeepOcean")){
            		biomes[i] = biomePlaine;
            	}
            }
            terrainBiomes.set(null, biomes);
		}catch (Exception e) {
			System.out.println(e);
		}
	}  
}