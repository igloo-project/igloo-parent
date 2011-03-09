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

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;
import fr.openwide.core.hibernate.more.business.generic.service.GenericListItemService;

public class GenericListItemModel<E extends GenericListItem<E>> extends LoadableDetachableModel<E> {
	
	private static final long serialVersionUID = 2483082448801965501L;

	private Class<E> clazz;
	
	private Integer id;
	
	@SpringBean
	private GenericListItemService genericListItemService;
	
	@SuppressWarnings("unchecked")
	public GenericListItemModel(E listItem) {
		super(listItem);
		InjectorHolder.getInjector().inject(this);
		
		if (listItem != null) {
			clazz = (Class<E>) listItem.getClass();
			id = listItem.getId();
		}
	}

	@Override
	protected E load() {
		E listItem = null;
		if (clazz != null && id != null) {
			listItem = genericListItemService.getById(clazz, id);
		}
		return listItem;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setObject(E listItem) {
		if (listItem != null) {
			clazz = (Class<E>) listItem.getClass();
			id = listItem.getId();
		} else {
			id = null;
			clazz = null;
		}
		super.setObject(listItem);
	}

}