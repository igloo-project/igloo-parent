package fr.openwide.core.jpa.hibernate.jpa;

import java.util.Map;

import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;

import com.google.common.collect.ImmutableMap;

import fr.openwide.core.jpa.hibernate.dialect.PerTableSequenceStyleGenerator;

/**
 * <p>This {@link IdentifierGeneratorStrategyProvider} remaps
 * {@link SequenceStyleGenerator} to our own
 * {@link PerTableSequenceStyleGenerator}</p>
 * 
 * @see PerTableSequenceStyleGenerator
 * @author lalmeras
 */
public class PerTableSequenceStrategyProvider implements IdentifierGeneratorStrategyProvider {

	@Override
	public Map<String, Class<?>> getStrategies() {
		return ImmutableMap.<String, Class<?>> builder()
				// overriding default SequenceStyleGenerator; our SequenceStyleGenerator is configured to
				// always generate a sequence by table
				.put(SequenceStyleGenerator.class.getName(), PerTableSequenceStyleGenerator.class).build();
	}

}
