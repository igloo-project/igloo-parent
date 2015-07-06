/*
 * Copyright (C) 2009-2011 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.openwide.core.jpa.more.business.generic.service;

import java.util.Comparator;
import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.path.PathBuilder;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.dao.IGenericListItemDao;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.QGenericListItem;

@Service("genericListItemService")
public class GenericListItemServiceImpl implements IGenericListItemService {
	
	protected final IGenericListItemDao genericListItemDao;
	
	@Autowired
	public GenericListItemServiceImpl(@Qualifier("genericListItemDao") IGenericListItemDao genericListItemDao) {
		super();
		this.genericListItemDao = genericListItemDao;
	}
	
	@Override
	public <E extends GenericListItem<?>> E getById(Class<E> clazz, Long id) {
		return genericListItemDao.getById(clazz, id);
	}
	
	protected <E extends GenericListItem<?>> E getByNaturalId(Class<E> clazz, Object naturalId) {
		return genericListItemDao.getByNaturalId(clazz, naturalId);
	}
	
	/**
	 * @deprecated Utiliser QueryDSL.
	 */
	@Deprecated
	protected <E extends GenericListItem<?>, V extends Comparable<?>>  E getByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue) {
		return genericListItemDao.getByField(clazz, attribute, fieldValue);
	}
	
	@Override
	public <E extends GenericListItem<?>> void create(E entity) {
		genericListItemDao.save(entity);
	}

	@Override
	public <E extends GenericListItem<?>> void update(E entity) {
		genericListItemDao.update(entity);
	}

	@Override
	public <E extends GenericListItem<?>> void delete(E entity) {
		genericListItemDao.delete(entity);
	}

	@Override
	public <E extends GenericListItem<?>> List<E> list(Class<E> clazz) {
		return list(clazz, null);
	}
	
	@Override
	public <E extends GenericListItem<?>> List<E> list(Class<E> clazz, Comparator<? super E> comparator) {
		return genericListItemDao.list(clazz, EnabledFilter.ALL, comparator);
	}
	
	@Override
	public <E extends GenericListItem<?>> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		return genericListItemDao.list(clazz, enabledFilter, comparator);
	}
	
	@Override
	public <E extends GenericListItem<?>> List<E> listEnabled(Class<E> clazz) {
		return listEnabled(clazz, null);
	}
	
	@Override
	public <E extends GenericListItem<?>> List<E> listEnabled(Class<E> clazz, Comparator<? super E> comparator) {
		return genericListItemDao.list(clazz, EnabledFilter.ENABLED_ONLY, comparator);
	}

	@Override
	public <E extends GenericListItem<?>> long count(Class<E> clazz) {
		return genericListItemDao.count(clazz);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getByLabel(Class<E> clazz, String label) throws NonUniqueResultException {
		PathBuilder<E> qEntity = new PathBuilder<E>(clazz, "rootAlias");
		QGenericListItem qEntityAsGenericListItem = new QGenericListItem(qEntity);
		return genericListItemDao.getByField(qEntity, qEntityAsGenericListItem.label, label);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getByShortLabel(Class<E> clazz, String shortLabel) throws NonUniqueResultException {
		PathBuilder<E> qEntity = new PathBuilder<E>(clazz, "rootAlias");
		QGenericListItem qEntityAsGenericListItem = new QGenericListItem(qEntity);
		return genericListItemDao.getByField(qEntity, qEntityAsGenericListItem.shortLabel, shortLabel);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getByLabelIgnoreCase(Class<E> clazz, String label) throws NonUniqueResultException {
		PathBuilder<E> qEntity = new PathBuilder<E>(clazz, "rootAlias");
		QGenericListItem qEntityAsGenericListItem = new QGenericListItem(qEntity);
		return genericListItemDao.getByFieldIgnoreCase(qEntity, qEntityAsGenericListItem.label, label);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getByShortLabelIgnoreCase(Class<E> clazz, String shortLabel) throws NonUniqueResultException {
		PathBuilder<E> qEntity = new PathBuilder<E>(clazz, "rootAlias");
		QGenericListItem qEntityAsGenericListItem = new QGenericListItem(qEntity);
		return genericListItemDao.getByFieldIgnoreCase(qEntity, qEntityAsGenericListItem.shortLabel, shortLabel);
	}
	
	@Override
	public <E extends GenericListItem<?>, V extends Comparable<?>> E getByField(EntityPath<E> entityPath,
			SimpleExpression<V> field, V fieldValue) throws NonUniqueResultException {
		return genericListItemDao.getByField(entityPath, field, fieldValue);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getByFieldIgnoreCase(EntityPath<E> entityPath, StringExpression field,
			String fieldValue) throws NonUniqueResultException {
		return genericListItemDao.getByFieldIgnoreCase(entityPath, field, fieldValue);
	}
	
	@Override
	public <E extends GenericListItem<?>> List<E> searchAutocomplete(String searchPattern, Class<E> clazz, int limit, int offset)
			throws ServiceException {
		return genericListItemDao.searchAutocomplete(searchPattern, clazz, limit, offset);
	}

}
