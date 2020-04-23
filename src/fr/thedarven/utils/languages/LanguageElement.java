package fr.thedarven.utils.languages;

import java.util.HashMap;
import java.util.Map;

public class LanguageElement {

	private String name;
	private Map<String, String> element;
	
	public LanguageElement(String name, Map<String, String> values) {
		this.name = name;
		this.element = values;
	}
	
	public LanguageElement(String name) {
		this.name = name;
		this.element = new HashMap<>();
	}
	
	public String getName() {
		return name;
	}

	public String getElement(String valueTitle) {
		if(element.containsKey(valueTitle))
			return element.get(valueTitle);
		return null;
	}
	
	public void addElement(String valueTitle, String valueContent) {
		element.put(valueTitle, valueContent);
	}
	
	public void addElement(String valueTitle, String valueContent, boolean replace) {
		if(replace || (!element.containsKey(valueTitle) || element.get(valueTitle).equals("")))
			element.put(valueTitle, valueContent == null ? "" : valueContent);
	}
	
}
