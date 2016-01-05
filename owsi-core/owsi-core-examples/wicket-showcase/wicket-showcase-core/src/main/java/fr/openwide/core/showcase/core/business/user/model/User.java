package fr.openwide.core.showcase.core.business.user.model;

import java.util.Collections;
import java.util.SortedSet;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.annotations.SortNatural;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;

import com.google.common.collect.Sets;

import fr.openwide.core.commons.util.collections.CollectionUtils;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;
import fr.openwide.core.jpa.security.business.person.model.GenericSimpleUser;

@Entity(name="user_")
@Bindable
@Indexed
@Cacheable
public class User extends GenericSimpleUser<User, UserGroup> {
	private static final long serialVersionUID = 7809996026983881824L;
	
	@Column(nullable = false)
	private Integer position;
	
	@ElementCollection
	@SortNatural
	private SortedSet<String> tags = Sets.newTreeSet();
	
	public Integer getPosition() {
		return position;
	}
	
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	public SortedSet<String> getTags() {
		return Collections.unmodifiableSortedSet(tags);
	}
	
	public void setTags(SortedSet<String> tags) {
		CollectionUtils.replaceAll(this.tags, tags);
	}
	
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	@SortableField
	public String getSortName() {
		StringBuilder builder = new StringBuilder();
		if(getLastName() != null) {
			builder.append(getLastName());
			builder.append(" ");
		}
		if(getFirstName() != null) {
			builder.append(getFirstName());
		}
		return builder.toString().trim();
	}
}
