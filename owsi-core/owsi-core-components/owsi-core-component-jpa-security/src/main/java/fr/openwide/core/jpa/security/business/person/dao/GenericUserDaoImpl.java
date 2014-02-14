package fr.openwide.core.jpa.security.business.person.dao;

import java.util.List;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.BeanPath;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.security.business.person.model.GenericUser_;
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
		
		JPQLQuery query = new JPAQuery(getEntityManager());
		
		query.from(qUser)
				.where(qUser.active.isTrue())
				.orderBy(qUser.userName.asc());
		
		return query.list(qUser.userName);
	}
	
	@Override
	public Long countActive() {
		return countByField(GenericUser_.active, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public U getByUserNameCaseInsensitive(String userName) {
		if (userName == null) {
			return null;
		}
		
		QGenericUser qUser = new QGenericUser(getEntityPath());
		
		JPQLQuery query = new JPAQuery(getEntityManager()).from(qUser);
		query.where(qUser.userName.lower().eq(userName.toLowerCase()));
		return (U) query.singleResult(qUser);
	}

}