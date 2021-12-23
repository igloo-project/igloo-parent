package org.iglooproject.jpa.security.business.user.model;

import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.SortableField;
import org.iglooproject.functional.Joiners;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.spring.notification.model.INotificationRecipient;
import org.iglooproject.spring.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;

@MappedSuperclass
@Bindable
public abstract class GenericSimpleUser<U extends GenericSimpleUser<U, G>, G extends GenericUserGroup<G, U>>
		extends GenericUser<U, G>
		implements ISimpleUser, INotificationRecipient {

	private static final long serialVersionUID = 4869548461178261021L;

	public static final String FIRST_NAME = "firstName";
	public static final String FIRST_NAME_SORT = "firstNameSort";

	public static final String LAST_NAME = "lastName";
	public static final String LAST_NAME_SORT = "lastNameSort";

	public static final String EMAIL = "email";

	@Column(nullable = false)
	@Field(name = FIRST_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	@Field(name = FIRST_NAME_SORT, normalizer = @Normalizer(definition = HibernateSearchNormalizer.TEXT))
	@SortableField(forField = FIRST_NAME_SORT)
	private String firstName;

	@Column(nullable = false)
	@Field(name = LAST_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	@Field(name = LAST_NAME_SORT, normalizer = @Normalizer(definition = HibernateSearchNormalizer.TEXT))
	@SortableField(forField = LAST_NAME_SORT)
	private String lastName;

	@Column
	@Field(name = EMAIL, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT))
	@SuppressWarnings("squid:S1845") // attribute name differs only by case on purpose
	private String email;

	public GenericSimpleUser() {
		super();
	}

	public GenericSimpleUser(String username, String firstName, String lastName, String passwordHash) {
		super(username, passwordHash);
		setFirstName(firstName);
		setLastName(lastName);
	}

	/*
	 * Works around a bindgen bug, where bindgen seems unable to substitute a concrete type to the "G" type parameter if we don't override this method here.
	 */
	@Override
	public SortedSet<G> getGroups() {
		return super.getGroups(); // NOSONAR
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

	@Transient
	@Override
	public String getFullName() {
		return Joiners.onSpace()
			.join(
				StringUtils.emptyTextToNull(getFirstName()),
				StringUtils.emptyTextToNull(getLastName())
			);
	}

	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int compareTo(U user) {
		if (this.equals(user)) {
			return 0;
		}
		
		if (GenericEntity.STRING_COLLATOR_FRENCH.compare(this.getLastName(), user.getLastName()) == 0) {
			return STRING_COLLATOR_FRENCH.compare(this.getFirstName(), user.getFirstName());
		}
		return STRING_COLLATOR_FRENCH.compare(this.getLastName(), user.getLastName());
	}

	@Override
	@Transient
	@JsonIgnore
	public boolean isNotificationEnabled() {
		// implémentation par défaut ; dépend de l'état de l'utilisateur
		return isEnabled();
	}

}
