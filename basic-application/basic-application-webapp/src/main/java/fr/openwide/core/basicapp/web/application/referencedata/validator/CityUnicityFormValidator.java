package fr.openwide.core.basicapp.web.application.referencedata.validator;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.common.model.PostalCode;
import fr.openwide.core.basicapp.core.business.referencedata.model.City;
import fr.openwide.core.basicapp.core.business.referencedata.service.IReferenceDataService;
import fr.openwide.core.wicket.more.util.validate.validators.AbstractUnicityFormValidator;

public class CityUnicityFormValidator extends AbstractUnicityFormValidator<City> {
	
	private static final long serialVersionUID = -5035428934340760607L;

	@SpringBean
	private IReferenceDataService referenceDataService;
	
	private final FormComponent<String> label;
	private final FormComponent<PostalCode> postalCode;
	
	public CityUnicityFormValidator(IModel<City> model,
			FormComponent<String> label, FormComponent<PostalCode> postalCode) {
		super(model, "common.validator.city.unicity.error", label, postalCode);
		this.label = label;
		this.postalCode = postalCode;
	}

	@Override
	protected City getByUniqueField() {
		return referenceDataService.getCityByLabelAndPostalCode(
				label.getConvertedInput(), postalCode.getConvertedInput()
		);
	}

}
