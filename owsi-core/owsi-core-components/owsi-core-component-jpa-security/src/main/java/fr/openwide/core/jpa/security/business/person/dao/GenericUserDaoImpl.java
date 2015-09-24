package fr.openwide.core.jpa.security.business.person.dao;

import java.util.List;

import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.security.business.person.model.QGenericUser;

public abstract class GenericUserDaoImpl<U extends GenericUser<?, ?>>
		extends GenericEntityDaoImpl<Long, U>
		implements IGenericUserDao<U> {
	
	public GenericUserDaoImpl() {
		super();
	}
	
	@Override
	public void save(U person) {
		super.save(person);
		
		flush();
	}
	
	protected BeanPath<U> getEntityPath() {
		// obtention d'un mapper querydsl branché à l'implémentation concrète
		return new BeanPath<U>(getObjectClass(), QGenericUser.genericUser.getMetadata());
	}
	
	@Override
	public List<String> listActiveUserNames() {
		QGenericUser qUser = new QGenericUser(getEntityPath());
		
		JPQLQuery<String> query = new JPAQuery<String>(getEntityManager());
		
		query.select(qUser.userName)
				.where(qUser.active.isTrue())
				.orderBy(qUser.userName.asc());
		
		return query.fetch();
	}
	
	@Override
	public Long countActive() {
		PathBuilder<U> qEntity = new PathBuilder<U>(getObjectClass(), "rootAlias");
		QGenericUser qEntityAsGenericUser = new QGenericUser(qEntity);
		return countByField(qEntity, qEntityAsGenericUser.active, true);
	}

	@Override
	public U getByUserNameCaseInsensitive(String userName) {
		if (userName == null) {
			return null;
		}
		
		QGenericUser qUser = new QGenericUser(getEntityPath());
		
		JPQLQuery<U> query = new JPAQuery<U>(getEntityManager()).from(qUser);
		query.where(qUser.userName.lower().eq(userName.toLowerCase()));
		return query.fetchOne();
	}

}