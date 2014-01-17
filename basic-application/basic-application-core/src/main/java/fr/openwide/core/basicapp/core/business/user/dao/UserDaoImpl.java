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

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.security.business.person.dao.AbstractPersonDaoImpl;
import fr.openwide.core.spring.util.StringUtils;

@Repository("personDao")
public class UserDaoImpl extends AbstractPersonDaoImpl<User> implements IUserDao {

	public UserDaoImpl() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> searchByNameActive(String name, Boolean active, Integer limit, Integer offset)
			throws ParseException {
		FullTextQuery fullTextQuery = getSearchByNameActiveQuery(name, active);
		fullTextQuery.setSort(new Sort(new SortField(User.LAST_NAME_SORT_FIELD_NAME, SortField.STRING),
				new SortField(User.FIRST_NAME_SORT_FIELD_NAME, SortField.STRING)));
		
		if (offset != null) {
			fullTextQuery.setFirstResult(offset);
		}
		
		if (limit != null) {
			fullTextQuery.setMaxResults(limit);
		}
			
		return (List<User>) fullTextQuery.getResultList();
	}

	@Override
	public int countByNameActive(String name, Boolean active) throws ParseException {
		FullTextQuery fullTextQuery = getSearchByNameActiveQuery(name, active);
		
		return fullTextQuery.getResultSize();
	}
	
	private FullTextQuery getSearchByNameActiveQuery(String name, Boolean active) throws ParseException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		
		QueryBuilder userQueryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(User.class).get();
		
		BooleanJunction<?> booleanJunction = userQueryBuilder.bool();
		
		if (StringUtils.hasText(name) || active != null) {
			if (StringUtils.hasText(name)) {
				booleanJunction.must(userQueryBuilder.keyword().fuzzy().withPrefixLength(1)
						.onField(Bindings.user().firstName().getPath())
						.andField(Bindings.user().lastName().getPath())
						.andField(Bindings.user().userName().getPath())
						.matching(name).createQuery());
			}
			
			if (active != null) {
				booleanJunction.must(userQueryBuilder.keyword().onField(Bindings.user().active().getPath()).matching(active).createQuery());
			}
		} else {
			booleanJunction.must(userQueryBuilder.all().createQuery());
		}
		return fullTextEntityManager.createFullTextQuery(booleanJunction.createQuery(), User.class);
	}
}
