package fr.openwide.core.export.excel;

/**
 * Classe contenant toutes les informations nécessaires sur l'affichage d'une colonne
 */
public class ColumnInformation {

	String headerKey;

	private boolean hidden;

	public ColumnInformation(String headerKey) {
		this.headerKey = headerKey;
	}

	public ColumnInformation(String headerKey, boolean hidden) {
		this(headerKey);
		this.hidden = hidden;
	}

	public String getHeaderKey() {
		return headerKey;
	}

	public void setHeaderKey(String headerKey) {
		this.headerKey = headerKey;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
