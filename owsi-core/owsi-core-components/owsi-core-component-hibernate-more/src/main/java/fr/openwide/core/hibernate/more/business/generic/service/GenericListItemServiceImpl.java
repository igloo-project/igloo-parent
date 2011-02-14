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

package fr.openwide.core.hibernate.more.business.generic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.openwide.core.hibernate.more.business.generic.dao.GenericListItemDao;
import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;
import fr.openwide.core.hibernate.more.business.generic.model.GenericListItemBinding;

@Service("genericListItemService")
public class GenericListItemServiceImpl implements GenericListItemService {
	
	@SuppressWarnings("rawtypes")
	private static final GenericListItemBinding GENERIC_LIST_ITEM_BINDING =
			new GenericListItemBinding();
	
	protected GenericListItemDao genericListItemDao;
	
	@Autowired
	public GenericListItemServiceImpl(@Qualifier("genericListItemDao") GenericListItemDao genericListItemDao) {
		super();
		this.genericListItemDao = genericListItemDao;
	}
	
	@Override
	public <E extends GenericListItem<?>> E getById(Class<E> clazz, Integer id) {
		return genericListItemDao.getById(clazz, id);
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
	public <E extends GenericListItem<? super E>> List<E> list(Class<E> clazz) {
		return genericListItemDao.list(clazz);
	}
	
	@Override
	public <E extends GenericListItem<? super E>> List<E> listEnabled(Class<E> clazz) {
		
		
		return genericListItemDao.listByField(clazz, GENERIC_LIST_ITEM_BINDING.enabled().getPath(), true);
	}

	@Override
	public <E extends GenericListItem<?>> long count(Class<E> clazz) {
		return genericListItemDao.count(clazz);
	}

}
