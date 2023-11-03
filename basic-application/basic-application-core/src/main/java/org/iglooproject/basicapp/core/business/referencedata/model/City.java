package org.iglooproject.basicapp.core.business.referencedata.model;

import org.bindgen.Bindable;
import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.business.common.model.embeddable.LocalizedText;
import org.iglooproject.basicapp.core.config.hibernate.type.PostalCodeValueBridge;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Bindable
@Indexed
@Cacheable
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"label_fr", "postalcode"}) })
public class City extends ReferenceData<City> {

	private static final long serialVersionUID = -5714475132350205234L;

	public static final String POSTAL_CODE = "postalCode";

	@Basic(optional = false)
	@GenericField(name = POSTAL_CODE, valueBridge = @ValueBridgeRef(type = PostalCodeValueBridge.class), sortable = Sortable.YES)
	private PostalCode postalCode;

	public City() {
	}

	public City(LocalizedText label) {
		super(label);
	}

	public PostalCode getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(PostalCode postalCode) {
		this.postalCode = postalCode;
	}

}
