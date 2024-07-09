package basicapp.front.referencedata.validator;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.util.validate.validators.AbstractUnicityFormValidator;

import basicapp.back.business.common.model.PostalCode;
import basicapp.back.business.referencedata.model.City;
import basicapp.back.business.referencedata.service.ICityService;

public class CityUnicityFormValidator extends AbstractUnicityFormValidator<City> {

	private static final long serialVersionUID = -5035428934340760607L;

	@SpringBean
	private ICityService cityService;

	private final FormComponent<String> label;
	private final FormComponent<PostalCode> postalCode;

	public CityUnicityFormValidator(IModel<City> model, FormComponent<String> label, FormComponent<PostalCode> postalCode) {
		super(model, "common.validator.city.unicity.error", label/*, postalCode*/);
		this.label = label;
		this.postalCode = postalCode;
	}

	@Override
	protected City getByUniqueField() {
		return cityService.getByLabelAndPostalCode(label.getConvertedInput(), postalCode.getConvertedInput());
	}

}
