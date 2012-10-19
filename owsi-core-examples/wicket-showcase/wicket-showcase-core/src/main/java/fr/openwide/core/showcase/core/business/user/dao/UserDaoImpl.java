package fr.openwide.core.showcase.core.business.user.dao;

import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.jpa.security.business.person.dao.AbstractPersonDaoImpl;
import fr.openwide.core.showcase.core.business.user.model.User;
import fr.openwide.core.showcase.core.business.user.model.UserBinding;

@Repository("userDao")
public class UserDaoImpl extends AbstractPersonDaoImpl<User> implements IUserDao {
	
	private static final UserBinding USER = new UserBinding();
	
	private static final String[] AUTOCOMPLETE_FIELDS = new String[] {
		USER.firstName().getPath(),
		USER.lastName().getPath()
	};
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	@Override
	public List<User> searchAutocomplete(String searchPattern) throws ServiceException {
		return hibernateSearchService.searchAutocomplete(User.class, AUTOCOMPLETE_FIELDS, searchPattern);
	}

	private FullTextQuery getSearchByNameActiveQuery(String name, Boolean active) throws ParseException {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		
		QueryBuilder userQueryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(User.class).get();
		
		BooleanJunction<?> booleanJunction = userQueryBuilder.bool();
		
		if (StringUtils.hasText(name) || active != null) {
			if (StringUtils.hasText(name)) {
				booleanJunction.must(userQueryBuilder.keyword().fuzzy().withPrefixLength(1)
						.onField(USER.firstName().getPath())
						.andField(USER.lastName().getPath())
						.andField(USER.userName().getPath())
						.matching(name).createQuery());
			}
			
			if (active != null) {
				booleanJunction.must(userQueryBuilder.keyword().onField(USER.active().getPath()).matching(active).createQuery());
			}
		} else {
			booleanJunction.must(userQueryBuilder.all().createQuery());
		}
		return fullTextEntityManager.createFullTextQuery(booleanJunction.createQuery(), User.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> searchByNameActive(String name, Boolean active, Integer limit, Integer offset)
			throws ParseException {
		FullTextQuery fullTextQuery = getSearchByNameActiveQuery(name, active);
		fullTextQuery.setSort(new Sort(new SortField(USER.sortName().getPath(), SortField.STRING)));
		
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

}
