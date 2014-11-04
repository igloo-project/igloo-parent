package fr.openwide.core.basicapp.web.application.common.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public abstract class AbstractGenericEntityTypeDescriptor<T extends AbstractGenericEntityTypeDescriptor<?, E>, E extends GenericEntity<?, ?>> implements Serializable {

	private static final long serialVersionUID = 4551490933539106231L;

	private static final Collection<AbstractGenericEntityTypeDescriptor<?, ?>> ALL = Lists.newArrayList();

	@SuppressWarnings("unchecked")
	public static final <T extends AbstractGenericEntityTypeDescriptor<?, E>, E extends GenericEntity<?, ?>> T get(E entity) {
		if (entity == null) {
			return null;
		}
		
		for (AbstractGenericEntityTypeDescriptor<?, ?> type : ALL) {
			if (type.getEntityClass().isInstance(entity)) {
				return (T) type;
			}
		}
		
		throw new IllegalStateException("Unknown type for entity " + entity);
	}

	private final Class<E> clazz;

	private final String name;

	protected AbstractGenericEntityTypeDescriptor(Class<E> clazz, String name) {
		ALL.add(this);
		this.clazz = checkNotNull(clazz);
		this.name = checkNotNull(name);
	}

	public Class<E> getEntityClass() {
		return clazz;
	}

	public String getName() {
		return name;
	}
	
	public String businessResourceKey(String suffix) {
		return resourceKey("business", suffix);
	}

	public String resourceKey(String prefix, String suffix) {
		return Joiner.on(".").skipNulls().join(prefix, name, suffix);
	}

	protected abstract Object readResolve();

}
