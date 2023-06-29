package org.iglooproject.basicapp.core.business.user.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;

import org.bindgen.Bindable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Indexed
@Bindable
@Cacheable
@Entity
public class TechnicalUser extends User {

	private static final long serialVersionUID = 1796077904296031306L;

}
