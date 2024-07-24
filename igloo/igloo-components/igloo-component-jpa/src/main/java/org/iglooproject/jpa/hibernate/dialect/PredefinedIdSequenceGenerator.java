package org.iglooproject.jpa.hibernate.dialect;

import java.io.Serializable;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.iglooproject.jpa.business.generic.model.IPredefinedIdEntity;

/**
 * Sequence generator used to define the new id manually
 *
 * <pre>{@code
 * @Id
 * @GeneratedValue(generator = "Ticket_id")
 * @GenericGenerator(name = "Ticket_id", strategy = PredefinedIdSequenceGenerator.CLASS_NAME)
 * @DocumentId
 * private Long id;
 * }</pre>
 */
public class PredefinedIdSequenceGenerator extends PerTableSequenceStyleGenerator {

  public static final String CLASS_NAME =
      "org.iglooproject.jpa.hibernate.dialect.PredefinedIdSequenceGenerator";

  @Override
  public Object generate(SharedSessionContractImplementor session, Object obj) {
    if (obj instanceof IPredefinedIdEntity) {
      IPredefinedIdEntity<?> entity = (IPredefinedIdEntity<?>) obj;
      Serializable predefinedId = entity.getPredefinedId();
      if (predefinedId != null) {
        return predefinedId;
      }
    }

    return super.generate(session, obj);
  }
}
