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

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.proxy.HibernateProxy;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IEntityService;

public class GenericEntityModel<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> extends LoadableDetachableModel<E> {
	
	private static final long serialVersionUID = 1L;

	@SpringBean
	private IEntityService entityService;
	
	private Class<E> clazz;
	
	private transient boolean attached = false;

	private K id;

	private E transientEntity;
	
	public GenericEntityModel(E entity) {
		super(null);
		Injector.get().inject(this);
		
		setObject(entity);
	}

	@Override
	protected E load() {
		E result = null;
		if (id != null) {
			result = entityService.getEntity(clazz, id);
		} else {
			result = transientEntity;
		}
		attached = true;
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setObject(E entity) {
		E persistentObject = getPersistentObject(entity);
		
		if (persistentObject != null) {
			clazz = (Class<E>) persistentObject.getClass();
			
			if (entity.getId() != null) {
				id = entity.getId();
				transientEntity = null;
			} else {
				id = null;
				transientEntity = entity;
			}
		} else {
			clazz = null;
			id = null;
			transientEntity = null;
		}
		
		super.setObject(persistentObject);
		attached = true;
	}
	
	protected K getId() {
		return id;
	}

	@Override
	public void detach() {
		if (!attached) {
			return;
		}
		super.detach();
		attached = false;
	}
	
	@SuppressWarnings("unchecked")
	private E getPersistentObject(E potentiallyProxyfiedObject) {
		if (potentiallyProxyfiedObject instanceof HibernateProxy) {
			return (E) ((HibernateProxy) potentiallyProxyfiedObject).getHibernateLazyInitializer().getImplementation();
		} else {
			return potentiallyProxyfiedObject;
		}
	}

}
