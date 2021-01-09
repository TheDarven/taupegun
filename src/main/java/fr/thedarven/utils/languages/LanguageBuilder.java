package fr.thedarven.utils.languages;

import fr.thedarven.TaupeGun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	 * Pour avoir la valeur associée à un nom de traduction, une clé et une langue
	 * 
	 * @param name Le nom de la traduction
	 * @param valueTitle La clé de la valeur
	 * @param language La langue
	 * @param defaultValue Si a true, on retour la valeur par défault dans le cas où la valeur n'existe pas
	 * @return La valeur associée
	 */
	public static String getContent(String name, String valueTitle, String language, boolean defaultValue){
		LanguageBuilder element = allElements.get(name);
		if(element == null)
			return null;
		
		return element.getInsideContent(language, valueTitle, defaultValue);
	}

	/**
	 * Pour avoir la valeur associée à un nom de traduction et une clé
	 *
	 * @param name Le nom de la traduction
	 * @param valueTitle La clé de la valeur
	 * @param defaultValue Si a true, on retour la valeur par défault dans le cas où la valeur n'existe pas
	 * @return La valeur associée
	 */
	public static String getContent(String name, String valueTitle, boolean defaultValue){
		LanguageBuilder element = allElements.get(name);
		if(element == null)
			return null;

		return element.getInsideContent(TaupeGun.getInstance().getInventoryRegister().language.getSelectedLanguage(), valueTitle, defaultValue);
	}
	
	/**
	 * Pour avoir dans toutes les langues la valeur associée à un titre
	 * 
	 * @param name Le nom de la traduction
	 * @param valueTitle La clé de la valeur
	 * @return La liste des valeurs associées à la clé
	 */
	public static List<String> getAllContent(String name, String valueTitle){
		LanguageBuilder element = allElements.get(name);
		if(element == null)
			return new ArrayList<String>();
		
		return element.getInsideAllContent(valueTitle);
	}
	
	/**
	 * Pour avoir le LanguageBuilder associé à un nom de traduction
	 * 
	 * @param name Le nom de traduction
	 * @return La LanguageBuilder associé
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
	 * @param valueTitle La clé de la valeur
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
	 * @param valueTitle La clé de la valeur
	 * @param valueContent La valeur
	 * @param replace Si la traduction existe déjà, savoir si on doit la remplacer
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
	 * Pour avoir le valeur associée à un titre et à une langue
	 * 
	 * @param language La langue
	 * @param valueTitle Le titre de la valeur
	 * @param defaultValue La valeur
	 * @return La valeur associée, null si elle n'existe pas
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
	
	/**
	 * Pour avoir l'entièreté des traductions d'une valeur
	 * 
	 * @param valueTitle Le titre de la valeur
	 * @return La liste des valeurs associées à la clé
	 */
	private List<String> getInsideAllContent(String valueTitle) {
		List<String> allTranslations = new ArrayList<>();
		for(Entry<String, LanguageElement> translation: content.entrySet()) {
			String translationValue = translation.getValue().getElement(valueTitle);
			if(translationValue != null)
				allTranslations.add(translationValue);
		}
		return allTranslations;
	}

}
