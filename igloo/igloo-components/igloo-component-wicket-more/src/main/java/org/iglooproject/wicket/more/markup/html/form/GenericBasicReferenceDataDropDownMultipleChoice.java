package org.iglooproject.wicket.more.markup.html.form;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericBasicReferenceData;
import org.iglooproject.wicket.more.markup.html.model.GenericReferenceDataModel;
import org.iglooproject.wicket.more.markup.html.select2.GenericSelect2DropDownMultipleChoice;
import org.iglooproject.wicket.more.rendering.GenericBasicReferenceDataRenderer;

public class GenericBasicReferenceDataDropDownMultipleChoice<T extends GenericBasicReferenceData<? super T>> extends GenericSelect2DropDownMultipleChoice<T> {

	private static final long serialVersionUID = -1907453710653648631L;

	public <C extends Collection<T>> GenericBasicReferenceDataDropDownMultipleChoice(
			String id,
			IModel<C> model,
			SerializableSupplier2<? extends C> collectionSupplier,
			Class<T> clazz
	) {
		this(
				id,
				model,
				collectionSupplier,
				new GenericReferenceDataModel<T>(clazz, EnabledFilter.ENABLED_ONLY)
		);
	}

	public <C extends Collection<T>> GenericBasicReferenceDataDropDownMultipleChoice(
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
				GenericEntityRendererToChoiceRenderer.of(GenericBasicReferenceDataRenderer.get())
		);
	}

}
