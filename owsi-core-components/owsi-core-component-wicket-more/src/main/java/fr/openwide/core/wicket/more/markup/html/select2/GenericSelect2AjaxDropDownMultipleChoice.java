package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.Collection;

import org.apache.wicket.model.IModel;
import org.retzlaff.select2.ISelect2AjaxAdapter;
import org.retzlaff.select2.Select2MultipleChoice;
import org.retzlaff.select2.Select2Settings;

import com.google.common.base.Supplier;

import fr.openwide.core.wicket.more.markup.html.select2.util.Select2Utils;
import fr.openwide.core.wicket.more.model.ConcreteCollectionToCollectionWrapperModel;

public class GenericSelect2AjaxDropDownMultipleChoice<T, C extends Collection<T>> extends Select2MultipleChoice<T> {

	private static final long serialVersionUID = 6355575209286187233L;

	protected GenericSelect2AjaxDropDownMultipleChoice(String id, IModel<C> model, Supplier<? extends C> collectionSupplier, ISelect2AjaxAdapter<T> adapter) {
		super(id, new ConcreteCollectionToCollectionWrapperModel<T, C>(model, collectionSupplier), adapter);
		
		fillSelect2Settings(getSettings());
	}

	protected void fillSelect2Settings(Select2Settings settings) {
		Select2Utils.setDefaultAjaxSettings(settings);
	}
}
