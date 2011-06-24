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

package fr.openwide.core.wicket.more.model;

import java.io.Serializable;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.business.generic.service.EntityService;

public class GenericEntityModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> extends LoadableDetachableModel<E> {
	
	private static final long serialVersionUID = 1L;

	@SpringBean
	private EntityService entityService;
	
	private K id;
	
	private Class<E> clazz;
	
	@SuppressWarnings("unchecked")
	public GenericEntityModel(E entity) {
		super(entity);
		InjectorHolder.getInjector().inject(this);
		
		if (entity != null) {
			clazz = (Class<E>) Hibernate.getClass(entity);
			id = entity.getId();
		}
	}

	@Override
	protected E load() {
		E entity = null;
		if (clazz != null && id != null) {
			entity = entityService.getEntity(clazz, id);
		}
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setObject(E entity) {
		if (entity != null) {
			clazz = (Class<E>) Hibernate.getClass(entity);
			id = entity.getId();
		} else {
			id = null;
			clazz = null;
		}
		super.setObject(entity);
	}
	
	protected K getId() {
		return id;
	}

}
