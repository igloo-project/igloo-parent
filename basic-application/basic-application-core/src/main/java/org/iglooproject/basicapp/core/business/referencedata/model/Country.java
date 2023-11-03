package org.iglooproject.basicapp.core.business.referencedata.model;

import org.bindgen.Bindable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;

@Entity
@Bindable
@Indexed
@Cacheable
public class Country extends ReferenceData<Country> {

	private static final long serialVersionUID = -6503012143488022508L;

}
