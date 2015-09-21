package fr.openwide.core.showcase.core.business.user.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;

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
	
	public Integer getPosition() {
		return position;
	}
	
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	@SortableField(forField = "sortName")
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
