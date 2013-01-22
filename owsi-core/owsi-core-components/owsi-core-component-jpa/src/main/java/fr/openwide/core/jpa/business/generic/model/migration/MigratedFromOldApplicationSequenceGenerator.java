package fr.openwide.core.jpa.business.generic.model.migration;

import java.io.Serializable;

import org.hibernate.engine.spi.SessionImplementor;

import fr.openwide.core.jpa.util.PostgreSQLSequenceGenerator;

/**
 * Sequence generator used to define the new id from the id used in an old application.
 * 
 * <pre>
 * {@code
 * @Id
 * @GeneratedValue(generator = "Ticket_id")
 * @GenericGenerator(name = "Ticket_id", strategy = MigratedFromOldApplicationSequenceGenerator.CLASS_NAME)
 * @DocumentId
 * private Long id;
 * }
 * </pre>
 */
public class MigratedFromOldApplicationSequenceGenerator extends PostgreSQLSequenceGenerator {
	
	public static final String CLASS_NAME = "fr.openwide.core.jpa.business.generic.model.migration.MigratedFromOldApplicationSequenceGenerator";
	
	@Override
	public Serializable generate(SessionImplementor session, Object obj) {
		if (obj instanceof MigratedFromOldApplicationGenericEntity) {
			Serializable oldApplicationId = ((MigratedFromOldApplicationGenericEntity<?, ?>) obj).getOldApplicationId();
			if (oldApplicationId != null) {
				return oldApplicationId;
			}
		}
		
		return super.generate(session, obj);
	}

}
