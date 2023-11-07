package org.iglooproject.jpa.security.business.user.model;

import java.util.SortedSet;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bindgen.Bindable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.iglooproject.functional.Joiners;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import igloo.hibernateconfig.api.HibernateSearchAnalyzer;
import igloo.hibernateconfig.api.HibernateSearchNormalizer;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

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
	@FullTextField(name = FIRST_NAME, analyzer = HibernateSearchAnalyzer.TEXT)
	@KeywordField(name = FIRST_NAME_SORT, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
	private String firstName;

	@Column(nullable = false)
	@FullTextField(name = LAST_NAME, analyzer = HibernateSearchAnalyzer.TEXT)
	@KeywordField(name = LAST_NAME_SORT, normalizer = HibernateSearchNormalizer.TEXT, sortable = Sortable.YES)
	private String lastName;

	@Column
	@FullTextField(name = EMAIL, analyzer = HibernateSearchAnalyzer.TEXT)
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
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof GenericUser)) {
			return false;
		}
		GenericSimpleUser<?, ?> other = (GenericSimpleUser<?, ?>) obj;
		return new EqualsBuilder()
			.append(getLastName(), other.getLastName())
			.append(getFirstName(), other.getFirstName())
			.appendSuper(super.equals(other))
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getLastName())
			.append(getFirstName())
			.appendSuper(super.hashCode())
			.toHashCode();
	}

	@Override
	public int compareTo(U user) {
		return new CompareToBuilder()
			.append(getLastName(), user.getLastName())
			.append(getFirstName(), user.getFirstName())
			.appendSuper(super.compareTo(user))
			.toComparison();
	}

	@Override
	@Transient
	@JsonIgnore
	public boolean isNotificationEnabled() {
		// implémentation par défaut ; dépend de l'état de l'utilisateur
		return isEnabled();
	}

}
