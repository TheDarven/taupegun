package fr.thedarven.main.metier;

public class NumericHelper {

	public int min;
	public int max;
	public int value;
	public int pas;
	public String afterName;
	public int diviseur;
	public int morePas;
	public boolean showDisabled;
	
	public NumericHelper(int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur, boolean pShowDisabled) {
		min = pMin;
		max = pMax;
		value = pValue;
		pas = pPas;
		afterName = pAfterName;
		diviseur = pDiviseur;
		morePas = pMorePas;
		showDisabled = pShowDisabled;
	}
	
}
