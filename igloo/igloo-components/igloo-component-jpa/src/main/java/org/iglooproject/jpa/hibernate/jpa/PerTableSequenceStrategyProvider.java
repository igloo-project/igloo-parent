package org.iglooproject.jpa.hibernate.jpa;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;
import org.iglooproject.jpa.hibernate.dialect.PerTableSequenceStyleGenerator;

/**
 * This {@link IdentifierGeneratorStrategyProvider} remaps {@link SequenceStyleGenerator} to our own
 * {@link PerTableSequenceStyleGenerator}
 *
 * @see PerTableSequenceStyleGenerator
 * @author lalmeras
 */
public class PerTableSequenceStrategyProvider implements IdentifierGeneratorStrategyProvider {

  @Override
  public Map<String, Class<?>> getStrategies() {
    return ImmutableMap.<String, Class<?>>builder()
        // overriding default SequenceStyleGenerator; our SequenceStyleGenerator is configured to
        // always generate a sequence by table
        .put(SequenceStyleGenerator.class.getName(), PerTableSequenceStyleGenerator.class)
        .build();
  }
}
