package fr.openwide.core.jpa.security.business.person.dao;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.BeanPath;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.security.business.person.model.GenericUser;
import fr.openwide.core.jpa.security.business.person.model.GenericUser_;
import fr.openwide.core.jpa.security.business.person.model.QGenericUser;

public abstract class GenericUserDaoImpl<U extends GenericUser<U, ?>>
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
		
		// obtention d'un mapper querydsl branché à l'implémentation concrète
		BeanPath<U> path = new BeanPath<U>(getObjectClass(), QGenericUser.genericUser.getMetadata());
		QGenericUser qUser = new QGenericUser(path);
		
		JPQLQuery query = new JPAQuery(getEntityManager()).from(qUser);
		query.where(qUser.userName.lower().eq(userName.toLowerCase()));
		return (U) query.singleResult(qUser);
	}

}