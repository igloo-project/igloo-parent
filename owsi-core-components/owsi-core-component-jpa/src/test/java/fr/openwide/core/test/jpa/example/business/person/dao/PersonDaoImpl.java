/*
 * Copyright (C) 2009-2010 Open Wide
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

package fr.openwide.core.test.jpa.example.business.person.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.ComparableExpressionBase;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.test.jpa.example.business.person.model.Person;

@Repository("personDao")
public class PersonDaoImpl extends GenericEntityDaoImpl<Long, Person> implements PersonDao {

	public PersonDaoImpl() {
		super();
	}

	@Override
	public <T extends Person, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue) {
		return super.getByField(entityPath, field, fieldValue);
	}

	@Override
	public <T extends Person> List<T> list(EntityPath<T> entityPath) {
		return super.list(entityPath);
	}

	@Override
	public <T extends Person, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath,
			ComparableExpressionBase<V> field, V fieldValue, OrderSpecifier<?> orderSpecifier) {
		return super.listByField(entityPath, field, fieldValue, orderSpecifier);
	}

	@Override
	public <V extends Comparable<?>> Long count(EntityPath<? extends Person> entityPath) {
		return super.count(entityPath);
	}

	@Override
	public <V extends Comparable<?>> Long countByField(EntityPath<? extends Person> entityPath,
			ComparableExpressionBase<V> field, V fieldValue) {
		return super.countByField(entityPath, field, fieldValue);
	}
}
