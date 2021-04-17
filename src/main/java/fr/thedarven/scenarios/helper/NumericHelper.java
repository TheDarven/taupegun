package fr.thedarven.scenarios.helper;

public class NumericHelper {

	final public int min;
	final public int max;
	final public int value;
	final public int pas;
	final public String afterName;
	final public int divider;
	final public int morePas;
	final public boolean showDisabled;
	final public double getterFactor;

	/**
	 *
	 * @param min Valeur minimum
	 * @param max Valeur maximum
	 * @param value Valeur par défaut
	 * @param pas Le pas d'incrémentation
	 * @param morePas Le nombre de pallier de pas d'incrémentation à afficher : 0, 1 ou 2
	 * @param afterName Tag après la valeur dans le nom de l'item
	 * @param divider Diviseur par lequel la valeur est divisée avant d'être affiché
	 * @param showDisplayed Afficher "désactiver" si la valeur est à 0
	 * @param getterFactor Multiplicateur qui multiplie la valeur dans les getters de comparaison
	 */
	public NumericHelper(int min, int max, int value, int pas, int morePas, String afterName, int divider, boolean showDisabled, double getterFactor) {
		this.min = min;
		this.max = max;
		this.value = value;
		this.pas = pas;
		this.afterName = afterName;
		this.divider = divider;
		this.morePas = morePas;
		this.showDisabled = showDisabled;
		this.getterFactor = getterFactor;
	}
	
}
