package fr.openwide.core.wicket.more.console.common.converter;

import java.util.Locale;

import org.apache.wicket.util.convert.converter.AbstractConverter;

public class JavaPackageReadOnlyConverter extends AbstractConverter<String> {

	private static final long serialVersionUID = 3524442690158084089L;
	
	private static final String JAVA_PACKAGE_SEPARATOR_REGEXP = "\\.";
	
	private static final String JAVA_PACKAGE_SEPARATOR = ".";
	
	/* Nombre de packages dont le nom sera affiché en entier */
	private static final int DEFAULT_PACKAGES_TO_DISPLAY = 5;
	
	@Override
	public String convertToObject(String value, Locale locale) {
		throw new IllegalStateException("Conversion en objet non supportée");
	}
	
	@Override
	public String convertToString(String value, Locale locale) {
		String[] packages = value.split(JAVA_PACKAGE_SEPARATOR_REGEXP);
		StringBuilder displayStringBuilder = new StringBuilder();
		int packagesCount = packages.length;
		for (int cpt=0; cpt < packagesCount; cpt++) {
			if (cpt > 0) {
				displayStringBuilder.append(JAVA_PACKAGE_SEPARATOR);
			}
			String p = packages[cpt];
			if (cpt < packagesCount - DEFAULT_PACKAGES_TO_DISPLAY) {
				displayStringBuilder.append(p, 0, 1);
			} else {
				displayStringBuilder.append(p);
			}
		}
		return displayStringBuilder.toString();
	}
	
	@Override
	protected Class<String> getTargetType() {
		return String.class;
	}

}
