package fr.thedarven.utils;

import org.bukkit.DyeColor;

public class CodeColor {
	
	// Prefix to glass
	public static int codeColorPS(String color){
		int result = 0;
		switch (color)
		{
		  case "0":
			    result = 15;
			    break;
		  case "1":
			  	result = 11;
			    break;
		  case "2":
			  	result = 13;
			    break;
		  case "3":
			  	result = 9;
			    break;
		  case "4":
			  	result = 14;
			    break;
		  case "5":
			  	result = 10;
			    break;
		  case "6":
			  	result = 1;
			    break;
		  case "7":
			  	result = 8;
			    break;
		  case "8":
			  	result = 7;
			    break;
		  case "9":
			  	result = 11;
			    break;
		  case "a":
			  	result = 5;
			    break;
		  case "b":
			  	result = 3;
			    break;
		  case "d":
			  	result = 2;
			    break;
		  case "e":
			  	result = 4;
			  	break;
		  case "f":
			  	result = 0;
			  	break;
		  default:
			  	result = 0;
		}
		return result;	
	}
	
	// Banner to prefix
		public static String codeColorBP(int color){
			
			String result = null;
			switch (color)
			{
			  case 0: //blanc
				    result = "f";
				    break;
			  case 1: //orange
				  	result = "6";
				    break;
			  case 2: //rose
				  	result = "d";
				    break;
			  case 3: //aqua
				  	result = "b";
				    break;
			  case 4: //jaune
				  	result = "e";
				    break;
			  case 5: //vert
				  	result = "a";
				    break;
			  case 7: //gris foncé
				  	result = "8";
				    break;
			  case 8: //gris
				  	result = "7";
				    break;
			  case 9: //aqua foncé
				  	result = "3";
				    break;
			  case 10: //violet
				  	result = "5";
				  	break;
			  case 11: //bleu
				  	result = "9";
				  	break;
			  case 13: //vert foncé
				  	result = "2";
				    break;
			  case 14: //rouge foncé
				  	result = "4";
				  	break;
			  case 15: //noir
				  	result = "0";
				  	break;
			  default:
				  	result = "0";
			}
			return result;
		}
	
	// Prefix to banner
	public static int codeColorPB(String color){
		int result = 0;
		switch (color)
		{
		  case "0": //noir
			    result = 0;
			    break;
		  case "2": //vert
			  	result = 2;
			    break;
		  case "3": //cyan
			  	result = 6;
			    break;
		  case "4": //rouge
			  	result = 1;
			    break;
		  case "5": //violet
			  	result = 5;
			    break;
		  case "6": //orange
			  	result = 14;
			    break;
		  case "7": //gris
			  	result = 7;
			    break;
		  case "8": //gris foncé
			  	result = 8;
			    break;
		  case "9": //bleu
			  	result = 4;
			    break;
		  case "a": //lime
			  	result = 10;
			    break;
		  case "b": //cyan clair
			  	result = 12;
			    break;
		  case "d": //rose
			  	result = 13;
			    break;
		  case "e": //jaune
			  	result = 11;
			  	break;
		  case "f": //blanc
			  	result = 15;
			  	break;
		  default:
			  	result = 0;
		}
		return result;	
	}
	
	// Create team banner
	public static DyeColor codeColorBD(int nbr){
		DyeColor a = DyeColor.WHITE;
		switch (nbr)
		{
		  case 0:
			    a = DyeColor.BLACK;
			    break;
		  case 1:
			    a = DyeColor.RED;
			    break;
		  case 2:
			    a = DyeColor.GREEN;
			    break;
		  case 3:
			    a = DyeColor.BLUE;
			    break;
		  case 4:
			    a = DyeColor.PURPLE;
			    break;
		  case 5:
			    a = DyeColor.CYAN;
			    break;
		  case 6:
			    a = DyeColor.SILVER;
			    break;
		  case 7:
			    a = DyeColor.GRAY;
			    break;
		  case 8:
			    a = DyeColor.LIME;
			    break;
		  case 9:
			    a = DyeColor.YELLOW;
			    break;
		  case 10:
			    a = DyeColor.LIGHT_BLUE;
			    break;
		  case 11:
			    a = DyeColor.MAGENTA;
			    break;
		  case 12:
			    a = DyeColor.ORANGE;
			    break;
		  case 13:
			  a = DyeColor.WHITE;
			  	break;
		  default:
			  a = DyeColor.WHITE;
		}
		return a;
	}
}
