package fr.openwide.core.jpa.more.util.init.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.JpaDaoSupport;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;

@Repository("importDataDao")
public class ImportDataDaoImpl extends JpaDaoSupport implements IImportDataDao {
	
	public ImportDataDaoImpl() {
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<?, ?>> E getById(Class<E> clazz, K id) {
		return super.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GenericEntity<?, ?>> void create(E entity) {
		super.save(entity);
	}

}
