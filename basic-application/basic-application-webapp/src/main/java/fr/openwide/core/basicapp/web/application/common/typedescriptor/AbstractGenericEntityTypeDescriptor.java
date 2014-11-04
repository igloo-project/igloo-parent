package fr.openwide.core.basicapp.web.application.common.typedescriptor;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.util.HibernateUtils;

public abstract class AbstractGenericEntityTypeDescriptor<T extends AbstractGenericEntityTypeDescriptor<?, E>, E extends GenericEntity<?, ?>> implements Serializable {

	private static final long serialVersionUID = 4551490933539106231L;

	private static final Map<Class<?>, AbstractGenericEntityTypeDescriptor<?, ?>> ALL = Maps.newHashMap();

	@SuppressWarnings("unchecked")
	protected static final <T extends AbstractGenericEntityTypeDescriptor<?, E>, E extends GenericEntity<?, ?>> T get(E entity) {
		if (entity == null) {
			return null;
		}
		
		AbstractGenericEntityTypeDescriptor<?, ?> specificTypeDescriptor = ALL.get(HibernateUtils.getClass(entity));
		if (specificTypeDescriptor != null) {
			return (T) specificTypeDescriptor;
		}
		
		for (AbstractGenericEntityTypeDescriptor<?, ?> typeDescriptor : ALL.values()) {
			if (typeDescriptor.getEntityClass().isInstance(entity)) {
				return (T) typeDescriptor;
			}
		}
		
		throw new IllegalStateException("Unknown type for entity " + entity);
	}

	private final Class<E> clazz;

	private final String name;

	protected AbstractGenericEntityTypeDescriptor(Class<E> clazz, String name) {
		ALL.put(clazz, this);
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
