package org.iglooproject.basicapp.core.business.user.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.search.annotations.Indexed;

@Indexed
@Bindable
@Cacheable
@Entity
public class BasicUser extends User {

	private static final long serialVersionUID = 7202814609595947705L;

}
