package fr.thedarven.utils;

import java.util.List;

public class UtilsClass {

	public static boolean startsWith(String text, List<String> testString) {
		for(String test: testString) {
			if(text.startsWith(test))
				return true;
		}
		return false;
	}
	
}
