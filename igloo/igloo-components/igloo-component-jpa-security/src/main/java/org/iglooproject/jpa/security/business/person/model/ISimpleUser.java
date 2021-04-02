package org.iglooproject.jpa.security.business.person.model;

import org.bindgen.Bindable;

@Bindable
public interface ISimpleUser extends IUser {

	String getFirstName();

	String getLastName();

}
