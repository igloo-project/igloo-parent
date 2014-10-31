package fr.openwide.core.basicapp.core.business.user.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;

import fr.openwide.core.basicapp.core.business.common.model.LocalizedGenericListItem;

@Indexed
@Bindable
@Cacheable
@Entity
public class UserPasswordSecretQuestion extends LocalizedGenericListItem<UserPasswordSecretQuestion> {

	private static final long serialVersionUID = 3044559851821472007L;

	public UserPasswordSecretQuestion() {
	}

}
