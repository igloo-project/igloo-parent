package fr.openwide.core.wicket.more.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IEntityService;

public abstract class AbstractGenericEntityCollectionModel
		<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>, C extends Collection<E>>
		extends LoadableDetachableModel<C> {

	private static final long serialVersionUID = -1768835911782930879L;

	@SpringBean
	private IEntityService entityService;

	private final List<K> idList = Lists.newArrayList();

	private final List<E> unsavedEntityList = Lists.newArrayList();
	
	private final Class<E> clazz;
	
	private transient C entityCollection = null;
	
	protected AbstractGenericEntityCollectionModel(Class<E> clazz) {
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
	protected C load() {
		entityCollection = createEntityCollection();
		
		for (int i = 0 ; i < idList.size() ; ++i) {
			K id = idList.get(i);
			E unsavedEntity = unsavedEntityList.get(i);
			
			assert id != null || unsavedEntity != null;
			
			if (unsavedEntity != null) {
				entityCollection.add(unsavedEntity);
			} else {
				entityCollection.add(toEntity(id));
			}
		}
		
		return entityCollection;
	}
	
	/**
	 * ATTENTION : si l'utilisateur appelle <code>setObject(null)</code>, un appel à <code>getObjet()</code>
	 * ne renverra pas <code>null</code>, mais <em>une collection vide</em>.
	 */
	@Override
	public void setObject(C object) {
		entityCollection = createEntityCollection();
		if (object != null) {
			entityCollection.addAll(object);
		}
		super.setObject(entityCollection);
	};
	
	@Override
	protected void onDetach() {
		if (entityCollection != null) {
			// On sauvegarde les éventuelles modifications apportées à entityCollection
			idList.clear();
			unsavedEntityList.clear();
			
			for (E entity : entityCollection) {
				if (entity.isNew()) {
					unsavedEntityList.add(entity);
					idList.add(null);
				} else {
					unsavedEntityList.add(null);
					idList.add(toId(entity));
				}
			}
			
			entityCollection.clear();
		}
	}

}
