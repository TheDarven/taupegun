package fr.thedarven.utils.languages;

import java.util.HashMap;
import java.util.Map;

public class LanguageBuilder {
	
	public static String DEFAULT_LANGUAGE = "fr_FR";
	private static Map<String, LanguageBuilder> allElements = new HashMap<>();
	
	private String name;
	private Map<String, LanguageElement> content;
	
	public LanguageBuilder(String name) {
		this.name = name;
		content = new HashMap<String, LanguageElement>();
		
		if(allElements.get(name) == null)
			allElements.put(name, this);
	}
	
	/**
	 * Pour avoir la valeur associ�e � un nom de traduction, une cl� et une langue
	 * 
	 * @param name Le nom de la traduction
	 * @param valueTitle La cl� de la valeur
	 * @param language La langue
	 * @param defaultValue Si a true, on retour la valeur par d�fault dans le cas o� la valeur n'existe pas
	 * @return La valeur associ�e
	 */
	public static String getContent(String name, String valueTitle, String language, boolean defaultValue){
		LanguageBuilder element = allElements.get(name);
		if(element == null)
			return null;
		
		return element.getInsideContent(language, valueTitle, defaultValue);
	}
	
	/**
	 * Pour avoir le LanguageBuilder associ� � un nom de traduction
	 * 
	 * @param name Le nom de traduction
	 * @return La LanguageBuilder associ�
	 */
	public static LanguageBuilder getLanguageBuilder(String name) {
		LanguageBuilder element = allElements.get(name);
		if(element == null)
			element = new LanguageBuilder(name);
		return element;
	}
	
	/**
	 * Pour ajouter une traduction
	 * 
	 * @param language La langue
	 * @param valueTitle La cl� de la valeur
	 * @param valueContent La valeur
	 */
	public void addTranslation(String language, String valueTitle, String valueContent) {
		LanguageElement element = this.content.get(language);
		if(element == null) {
			content.put(language, new LanguageElement(this.name));	
			element = this.content.get(language);
		}
		
		if(element != null) {
			element.addElement(valueTitle, valueContent, false);
		}
	}
	
	/**
	 * Pour ajouter une traduction
	 * 
	 * @param language La langue
	 * @param valueTitle La cl� de la valeur
	 * @param valueContent La valeur
	 * @param replace Si la traduction existe d�j�, savoir si on doit la remplacer
	 */
	public void addTranslation(String language, String valueTitle, String valueContent, boolean replace) {
		LanguageElement element = this.content.get(language);
		if(element == null) {
			content.put(language, new LanguageElement(this.name));	
			element = this.content.get(language);
		}
		
		if(element != null) {
			element.addElement(valueTitle, valueContent, replace);
		}
	}
	
	/**
	 * Pour avoir le valeur associ�e � un language et � une langue
	 * 
	 * @param language La langue
	 * @param valueTitle La cl� de la valeur
	 * @param defaultValue La valeur
	 * @return
	 */
	private String getInsideContent(String language, String valueTitle, boolean defaultValue) {
		LanguageElement element = content.get(language);
		String returnString = null;
		if(element != null) {
			returnString = element.getElement(valueTitle);
			if(returnString != null && !returnString.equals(""))
				return returnString;
		}
		if(defaultValue) {
			element = this.content.get(DEFAULT_LANGUAGE);
			if(element != null)
				return element.getElement(valueTitle);	
		}
		return null;
	}

}
