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

package fr.openwide.core.wicket.more.markup.html.list;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

/**
 * Modèle pour l'utilisation dans les {@link ListView}, se basant sur les ids au
 * lieu des index de liste pour recharger les objets.
 * 
 * Sa mise en oeuvre impose la surcharge de {@link ListView#getListItemModel()}
 * ou de sa méthode appelante {@link ListView#newItem()}
 *
 * @param <T> type de l'entité à gérer
 * @param <K> type de l'identifiant de l'entité
 */
public class GenericEntityListItemModel<T extends GenericEntity<K, T>, K extends Serializable & Comparable<K>>
		extends LoadableDetachableModel<T> {
	
	private static final long serialVersionUID = 1L;
	
	private K id;
	
	private IModel<? extends List<T>> listModel;
	
	public GenericEntityListItemModel(IModel<? extends List<T>> listModel, K id) {
		this.listModel = listModel;
		this.id = id;
	}

	@Override
	protected T load() {
		for (T item : listModel.getObject()) {
			if (id.equals(item.getId())) {
				return item;
			}
		}
		
		return null;
	}

}
