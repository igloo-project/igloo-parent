package fr.openwide.core.jpa.hibernate.model.naming;

import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl;
import org.hibernate.boot.model.source.spi.AttributePath;

public class ImplicitNamingStrategyLegacyJpaComponentPathImpl extends ImplicitNamingStrategyLegacyJpaImpl {
	
	private static final long serialVersionUID = 6365899992934883501L;
	
	public static final ImplicitNamingStrategyLegacyJpaComponentPathImpl INSTANCE = new ImplicitNamingStrategyLegacyJpaComponentPathImpl();
	
	@Override
	protected String transformAttributePath(AttributePath attributePath) {
		final StringBuilder sb = new StringBuilder();
		process( attributePath, sb );
		return sb.toString();
	}

	public static void process(AttributePath attributePath, StringBuilder sb) {
		if ( attributePath.getParent() != null ) {
			process( attributePath.getParent(), sb );
			if ( !"".equals( attributePath.getParent().getProperty() ) ) {
				sb.append( "_" );
			}
		}

		String property = attributePath.getProperty();
		property = property.replace( "<", "" );
		property = property.replace( ">", "" );

		sb.append( property );
	}
	
}
