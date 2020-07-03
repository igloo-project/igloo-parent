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

package org.iglooproject.test.business.person.dao;

import java.util.List;

import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.iglooproject.test.business.person.model.Person;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.SimpleExpression;

@Repository("personDao")
public class PersonDaoImpl extends GenericEntityDaoImpl<Long, Person> implements IPersonDao {

	public PersonDaoImpl() {
		super();
	}

	@Override
	public <T extends Person, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			SimpleExpression<V> field, V fieldValue) {
		return super.getByField(entityPath, field, fieldValue);
	}

	@Override
	public <T extends Person> List<T> list(EntityPath<T> entityPath) {
		return super.list(entityPath);
	}

	@Override
	public <T extends Person, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath,
			SimpleExpression<V> field, V fieldValue, OrderSpecifier<?> orderSpecifier) {
		return super.listByField(entityPath, field, fieldValue, orderSpecifier);
	}

	@Override
	public Long count(EntityPath<? extends Person> entityPath) {
		return super.count(entityPath);
	}

	@Override
	public <V extends Comparable<?>> Long countByField(EntityPath<? extends Person> entityPath,
			SimpleExpression<V> field, V fieldValue) {
		return super.countByField(entityPath, field, fieldValue);
	}
}
