package basicapp.front.common.form;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.form.GenericEntityRendererToChoiceRenderer;
import org.iglooproject.wicket.more.markup.html.model.GenericReferenceDataModel;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;

import basicapp.back.business.referencedata.model.ReferenceData;
import basicapp.back.business.referencedata.model.comparator.ReferenceDataComparator;
import basicapp.front.common.renderer.ReferenceDataRenderer;

public class ReferenceDataDropDownMultipleChoice<T extends ReferenceData<? super T>> extends GenericSelect2DropDownMultipleChoice<T> {

	private static final long serialVersionUID = -4170507171595192143L;

	public <C extends Collection<T>> ReferenceDataDropDownMultipleChoice(
		String id,
		IModel<C> model,
		SerializableSupplier2<? extends C> collectionSupplier,
		Class<T> clazz
	) {
		this(
			id,
			model,
			collectionSupplier,
			new GenericReferenceDataModel<>(clazz, ReferenceDataComparator.get(), EnabledFilter.ENABLED_ONLY)
		);
	}

	public <C extends Collection<T>> ReferenceDataDropDownMultipleChoice(
		String id,
		IModel<C> model,
		SerializableSupplier2<? extends C> collectionSupplier,
		IModel<? extends Collection<? extends T>> choicesModel
	) {
		super(
			id,
			model,
			collectionSupplier,
			choicesModel,
			GenericEntityRendererToChoiceRenderer.of(ReferenceDataRenderer.get())
		);
	}

}
