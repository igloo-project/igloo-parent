package org.iglooproject.basicapp.core.business.referencedata.search;

import org.bindgen.Bindable;
import org.iglooproject.basicapp.core.business.common.model.PostalCode;
import org.iglooproject.basicapp.core.business.referencedata.model.City;

@Bindable
public class CitySearchQueryData extends AbstractReferenceDataSearchQueryData<City> {

	private PostalCode postalCode;

	public PostalCode getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(PostalCode postalCode) {
		this.postalCode = postalCode;
	}

}
