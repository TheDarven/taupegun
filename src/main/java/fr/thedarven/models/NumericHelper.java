package fr.thedarven.models;

public class NumericHelper {

	public int min;
	public int max;
	public int value;
	public int pas;
	public String afterName;
	public int diviseur;
	public int morePas;
	public boolean showDisabled;
	public double getterFactor;

	/**
	 *
	 * @param pMin Valeur minimum
	 * @param pMax Valeur maximum
	 * @param pValue Valeur par défaut
	 * @param pPas Le pas d'incrémentation
	 * @param pMorePas Le nombre de pallier de pas d'incrémentation à afficher : 0, 1 ou 2
	 * @param pAfterName Tag après la valeur dans le nom de l'item
	 * @param pDiviseur Diviseur par lequel la valeur est divisée avant d'être affiché
	 * @param pShowDisabled Afficher "désactiver" si la valeur est à 0
	 * @param getterFactor Multiplicateur qui multiplie la valeur dans les getters de comparaison
	 */
	public NumericHelper(int pMin, int pMax, int pValue, int pPas, int pMorePas, String pAfterName, int pDiviseur, boolean pShowDisabled, double getterFactor) {
		this.min = pMin;
		this.max = pMax;
		this.value = pValue;
		this.pas = pPas;
		this.afterName = pAfterName;
		this.diviseur = pDiviseur;
		this.morePas = pMorePas;
		this.showDisabled = pShowDisabled;
		this.getterFactor = getterFactor;
	}
	
}
