package fr.openwide.core.basicapp.core.business.user.dao;

import java.util.List;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.util.binding.Binding;
import fr.openwide.core.jpa.security.business.person.dao.AbstractPersonDaoImpl;
import fr.openwide.core.spring.util.StringUtils;

@Repository("personDao")
public class UserDaoImpl extends AbstractPersonDaoImpl<User> implements IUserDao {

	public UserDaoImpl() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> search(String searchTerm, Integer limit, Integer offset) {
		FullTextQuery query = getSearchQuery(searchTerm);
		
		query.setSort(new Sort(new SortField(Binding.user().userName().getPath(), SortField.STRING)));
		
		if (offset != null) {
			query.setFirstResult(offset);
		}
		if (limit != null) {
			query.setMaxResults(limit);
		}
		
		return (List<User>) query.getResultList();
	}
	
	@Override
	public int countSearch(String searchTerm) {
		return getSearchQuery(searchTerm).getResultSize();
	}
	
	private FullTextQuery getSearchQuery(String searchTerm) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
		
		QueryBuilder userQueryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(getObjectClass()).get();
		
		BooleanJunction<?> booleanJunction = userQueryBuilder.bool();
		
		if (StringUtils.hasText(searchTerm)) {
			booleanJunction.must(userQueryBuilder
					.keyword()
					.fuzzy().withPrefixLength(1).withThreshold(0.8F)
					.onField(Binding.user().userName().getPath())
					.andField(Binding.user().firstName().getPath())
					.andField(Binding.user().lastName().getPath())
					.andField(Binding.user().email().getPath())
					.matching(searchTerm)
					.createQuery());
		} else {
			booleanJunction.must(userQueryBuilder.all().createQuery());
		}
		
		return fullTextEntityManager.createFullTextQuery(booleanJunction.createQuery(), getObjectClass());
	}
}
