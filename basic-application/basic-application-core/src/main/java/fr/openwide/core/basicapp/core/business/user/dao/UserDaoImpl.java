package fr.openwide.core.basicapp.core.business.user.dao;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;

import fr.openwide.core.basicapp.core.business.user.model.QUser;
import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.model.UserSearchParameters;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.security.business.person.dao.GenericUserDaoImpl;
import fr.openwide.core.spring.util.StringUtils;

@Repository("personDao")
public class UserDaoImpl extends GenericUserDaoImpl<User> implements IUserDao {

	private final QUser qUser = QUser.user;

	public UserDaoImpl() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends User> List<U> search(Class<U> clazz, UserSearchParameters searchParams, Integer limit, Integer offset)
			throws ParseException {
		FullTextQuery fullTextQuery = getSearchQuery(clazz, searchParams);
		fullTextQuery.setSort(new Sort(new SortField(User.LAST_NAME_SORT_FIELD_NAME, SortField.STRING),
				new SortField(User.FIRST_NAME_SORT_FIELD_NAME, SortField.STRING)));
		
		if (offset != null) {
			fullTextQuery.setFirstResult(offset);
		}
		
		if (limit != null) {
			fullTextQuery.setMaxResults(limit);
		}
			
		return (List<U>) fullTextQuery.getResultList();
	}

	@Override
	public <U extends User> int count(Class<U> clazz, UserSearchParameters searchParams) throws ParseException {
		FullTextQuery fullTextQuery = getSearchQuery(clazz, searchParams);
		
		return fullTextQuery.getResultSize();
	}
	
	private <U extends User> FullTextQuery getSearchQuery(Class<U> clazz, UserSearchParameters searchParams) throws ParseException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		
		QueryBuilder userQueryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(User.class).get();
		
		BooleanJunction<?> booleanJunction = userQueryBuilder.bool().must(userQueryBuilder.all().createQuery());
		
		String name = searchParams.getName();
		if (StringUtils.hasText(name)) {
			booleanJunction.must(userQueryBuilder.keyword().fuzzy().withPrefixLength(1)
					.onField(Bindings.user().firstName().getPath())
					.andField(Bindings.user().lastName().getPath())
					.andField(Bindings.user().userName().getPath())
					.matching(name).createQuery());
		}
		
		UserGroup group = searchParams.getGroup();
		if (group != null) {
			booleanJunction.must(userQueryBuilder.keyword().onField(Bindings.user().groups().getPath()).matching(group).createQuery());
		}
		
		Boolean active = searchParams.getActive();
		if (active != null) {
			booleanJunction.must(userQueryBuilder.keyword().onField(Bindings.user().active().getPath()).matching(active).createQuery());
		}
		
		return fullTextEntityManager.createFullTextQuery(booleanJunction.createQuery(), clazz);
	}

	@Override
	public User getByEmailCaseInsensitive(String email) {
		return new JPAQuery(getEntityManager())
				.from(qUser)
				.where(qUser.email.lower().eq(StringUtils.lowerCase(email)))
				.singleResult(qUser);
	}
}
