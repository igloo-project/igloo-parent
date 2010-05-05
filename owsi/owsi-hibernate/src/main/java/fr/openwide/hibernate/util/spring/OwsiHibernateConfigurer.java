package fr.openwide.hibernate.util.spring;


public class OwsiHibernateConfigurer extends CustomPropertyPlaceholderConfigurer {
	
	private static final String HIBERNATE_CONFIGURATION_TYPE = "configurationType";
	static final String CONFIGURATION_TYPE_DEVELOPMENT = "development";
	
	public boolean isConfigurationTypeDevelopment() {
		return CONFIGURATION_TYPE_DEVELOPMENT.equals(getPropertyAsString(HIBERNATE_CONFIGURATION_TYPE));
	}
}
