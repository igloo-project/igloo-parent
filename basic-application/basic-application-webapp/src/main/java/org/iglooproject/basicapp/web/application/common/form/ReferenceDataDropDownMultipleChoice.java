package org.iglooproject.basicapp.web.application.common.form;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.iglooproject.basicapp.core.business.referencedata.model.ReferenceData;
import org.iglooproject.basicapp.core.business.referencedata.model.comparator.ReferenceDataComparator;
import org.iglooproject.basicapp.web.application.common.renderer.ReferenceDataRenderer;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.wicket.more.markup.html.form.GenericEntityRendererToChoiceRenderer;
import org.iglooproject.wicket.more.markup.html.model.GenericReferenceDataModel;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;

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
			new GenericReferenceDataModel<T>(clazz, ReferenceDataComparator.get(), EnabledFilter.ENABLED_ONLY)
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
