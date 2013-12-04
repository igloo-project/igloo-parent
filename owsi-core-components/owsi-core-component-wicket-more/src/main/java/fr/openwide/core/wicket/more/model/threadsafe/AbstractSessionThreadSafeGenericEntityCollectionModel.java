package fr.openwide.core.wicket.more.model.threadsafe;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.util.HibernateUtils;

public abstract class AbstractSessionThreadSafeGenericEntityCollectionModel
		<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>, C extends Collection<E>>
		extends SessionThreadSafeDerivedSerializableStateLoadableDetachableModel<C, AbstractSessionThreadSafeGenericEntityCollectionModel<K, E, C>.SerializableState> {

	private static final long serialVersionUID = -1768835911782930879L;

	@SpringBean
	private IEntityService entityService;
	
	private final Class<E> clazz;
	
	protected AbstractSessionThreadSafeGenericEntityCollectionModel(Class<E> clazz) {
		super();
		Injector.get().inject(this);
		
		this.clazz = clazz;
		setObject(null); // Sets to an empty collection
	}

	protected abstract C createEntityCollection();
	
	protected K toId(E entity) {
		return entity.getId();
	}
	
	protected E toEntity(K id) {
		return entityService.getEntity(clazz, id);
	}

	@Override
	protected C load(SerializableState serializableState) {
		C entityCollection = createEntityCollection();
		
		// Never, ever return null
		if (serializableState == null) {
			return entityCollection;
		}
		
		List<K> idList = serializableState.idList;
		List<E> unsavedEntityList = serializableState.unsavedEntityList;
		
		for (int i = 0 ; i < serializableState.idList.size() ; ++i) {
			K id = idList.get(i);
			E unsavedEntity = unsavedEntityList.get(i);
			
			assert id != null || unsavedEntity != null;
			assert id == null || unsavedEntity == null;
			
			if (unsavedEntity != null) {
				entityCollection.add(unsavedEntity);
			} else {
				entityCollection.add(toEntity(id));
			}
		}
		
		return entityCollection;
	}
	
	@Override
	protected C wrap(C object) {
		C entityCollection = createEntityCollection();
		
		// Never, ever return null
		if (object == null) {
			return entityCollection;
		}
		
		entityCollection.addAll(object);
		
		return entityCollection;
	}
	
	@Override
	protected SerializableState makeSerializable(C currentObject) {
		return new SerializableState(currentObject);
	}
	
	protected class SerializableState implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private final List<K> idList = Lists.newArrayList();
		private final List<E> unsavedEntityList = Lists.newArrayList();
		
		public SerializableState(Collection<? extends E> entityCollection) {
			if (entityCollection != null) {
				// On sauvegarde les éventuelles modifications apportées à entityCollection
				idList.clear();
				unsavedEntityList.clear();
				
				for (E entity : entityCollection) {
					if (entity.isNew()) {
						unsavedEntityList.add(HibernateUtils.unwrap(entity));
						idList.add(null);
					} else {
						unsavedEntityList.add(null);
						idList.add(toId(entity));
					}
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (!(obj instanceof AbstractSessionThreadSafeGenericEntityCollectionModel.SerializableState)) {
				return false;
			}
			SerializableState other = (SerializableState) obj;
			
			return new EqualsBuilder()
					.append(idList, other.idList)
					.append(unsavedEntityList, other.unsavedEntityList)
					.build();
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
					.append(idList)
					.append(unsavedEntityList)
					.toHashCode();
		}
	}

}
