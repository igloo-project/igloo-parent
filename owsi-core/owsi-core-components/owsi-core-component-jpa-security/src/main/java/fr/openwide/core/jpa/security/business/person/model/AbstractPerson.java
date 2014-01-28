package fr.openwide.core.jpa.security.business.person.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.bindgen.Bindable;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.springframework.security.acls.model.Permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryType;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.spring.notification.model.INotificationRecipient;

@MappedSuperclass
@Bindable
@NaturalIdCache
public abstract class AbstractPerson<P extends AbstractPerson<P>> extends GenericEntity<Long, P>
		implements IPerson, INotificationRecipient {

	private static final long serialVersionUID = 1803671157183603979L;
	
	public static final String FIRST_NAME_SORT_FIELD_NAME = "firstNameSort";
	public static final String LAST_NAME_SORT_FIELD_NAME = "lastNameSort";
	
	@Id
	@GeneratedValue
	@DocumentId
	private Long id;
	
	@Column(nullable = false)
	@NaturalId(mutable = true)
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	private String userName;
	
	@Column(nullable = false)
	@Fields({
			@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
			@Field(name = FIRST_NAME_SORT_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	private String firstName;
	
	@Column(nullable = false)
	@Fields({
			@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
			@Field(name = LAST_NAME_SORT_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	private String lastName;
	
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	private String email;
	
	private String phoneNumber;
	
	private String gsmNumber;
	
	private String faxNumber;
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private String passwordHash = "*NO PASSWORD*";
	
	@Field
	private boolean active = true;
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	@Column(nullable = false)
	private Date creationDate = new Date();
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	@Column(nullable = false)
	private Date lastUpdateDate = new Date();
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private Date lastLoginDate;
	
	/**
	 * preferred locale for user, can be null
	 */
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private Locale locale;
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	@ManyToMany
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@OrderBy("name")
	private Set<Authority> authorities = new LinkedHashSet<Authority>();
	
	public AbstractPerson() {
	}
	
	public AbstractPerson(String userName, String firstName, String lastName, String passwordHash) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.passwordHash = passwordHash;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	@Transient
	@Override
	public String getFullName() {
		StringBuilder builder = new StringBuilder();
		if(firstName != null) {
			builder.append(firstName);
			builder.append(" ");
		}
		if(lastName != null && !lastName.equals(firstName)) {
			builder.append(lastName);
		}
		return builder.toString().trim();
	}

	@Override
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}
	
	public void removeAuthority(Authority authority) {
		this.authorities.remove(authority);
	}

	@Override
	public String getNameForToString() {
		return getUserName();
	}

	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setGsmNumber(String gsmNumber) {
		this.gsmNumber = gsmNumber;
	}

	public String getGsmNumber() {
		return gsmNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	@Override
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = CloneUtils.clone(lastLoginDate);
	}

	public Date getLastLoginDate() {
		return CloneUtils.clone(lastLoginDate);
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = CloneUtils.clone(creationDate);
	}

	public Date getCreationDate() {
		return CloneUtils.clone(creationDate);
	}
	
	public Date getLastUpdateDate() {
		return CloneUtils.clone(lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = CloneUtils.clone(lastUpdateDate);
	}

	/**
	 * Fournit la locale préférée de l'utilisation. Il faut utiliser
	 * {@link CoreConfigurer##toAvailableLocale(Locale)} si la locale
	 * préférée de l'utilisateur doit être exploitée pour choisir des traductions.
	 * Cette méthode permet de mapper une locale quelconque (incluant null) sur
	 * une locale qui sera obligatoirement reconnue pas le système (de manière à
	 * avoir un fonctionnement prédictible). 
	 * @return une locale, possiblement null
	 */
	@Override
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	@Override
	@QueryType(PropertyType.NONE)
	public Set<? extends Permission> getPermissions() {
		return Sets.newHashSetWithExpectedSize(0);
	}

	@Override
	public int compareTo(P person) {
		if(this.equals(person)) {
			return 0;
		}
		
		if(DEFAULT_STRING_COLLATOR.compare(this.getLastName(), person.getLastName()) == 0) {
			return DEFAULT_STRING_COLLATOR.compare(this.getFirstName(), person.getFirstName());
		}
		return DEFAULT_STRING_COLLATOR.compare(this.getLastName(), person.getLastName());
	}

	@Override
	public String getDisplayName() {
		return getFullName();
	}
	
}