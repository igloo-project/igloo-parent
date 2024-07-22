package org.iglooproject.jpa.hibernate.jpa;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;
import org.iglooproject.jpa.hibernate.dialect.PredefinedIdSequenceGenerator;

/**
 * This {@link IdentifierGeneratorStrategyProvider} remaps {@link SequenceStyleGenerator} to our own
 * {@link PredefinedIdSequenceGenerator}
 *
 * @see PredefinedIdSequenceGenerator
 * @author jgonzalez
 */
public class PredefinedIdStrategyProvider implements IdentifierGeneratorStrategyProvider {

  @Override
  public Map<String, Class<?>> getStrategies() {
    return ImmutableMap.<String, Class<?>>builder()
        .put(SequenceStyleGenerator.class.getName(), PredefinedIdSequenceGenerator.class)
        .build();
  }
}
