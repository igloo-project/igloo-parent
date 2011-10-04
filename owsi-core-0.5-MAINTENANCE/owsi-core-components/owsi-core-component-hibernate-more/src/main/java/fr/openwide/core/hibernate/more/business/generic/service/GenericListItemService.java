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

import fr.openwide.core.hibernate.business.generic.service.TransactionalAspectAwareService;
import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;

public interface GenericListItemService extends TransactionalAspectAwareService {
	
	<E extends GenericListItem<?>> E getById(Class<E> clazz, Integer id);

	<E extends GenericListItem<?>> void create(E entity);

	<E extends GenericListItem<?>> void update(E entity);

	<E extends GenericListItem<?>> void delete(E entity);

	<E extends GenericListItem<?>> List<E> list(Class<? extends E> clazz);

	<E extends GenericListItem<?>> long count(Class<? extends E> clazz);

	<E extends GenericListItem<? super E>> List<E> listEnabled(Class<? extends E> clazz);

}
