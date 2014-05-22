package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.Collection;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.lang.Args;
import org.retzlaff.select2.ISelect2AjaxAdapter;
import org.retzlaff.select2.Select2MultipleChoice;
import org.retzlaff.select2.Select2Settings;

import com.google.common.base.Supplier;

import fr.openwide.core.wicket.markup.html.model.ConcreteCollectionToCollectionWrapperModel;
import fr.openwide.core.wicket.more.markup.html.select2.util.DropDownChoiceWidth;
import fr.openwide.core.wicket.more.markup.html.select2.util.IDropDownChoiceWidth;
import fr.openwide.core.wicket.more.markup.html.select2.util.Select2Utils;

public class GenericSelect2AjaxDropDownMultipleChoice<T> extends Select2MultipleChoice<T> {

	private static final long serialVersionUID = 6355575209286187233L;
	
	/**
	 * Hack.
	 * @see IDropDownChoiceWidth
	 */
	private IDropDownChoiceWidth width = DropDownChoiceWidth.NORMAL;

	protected <C extends Collection<T>> GenericSelect2AjaxDropDownMultipleChoice(
			String id, IModel<C> model, Supplier<? extends C> collectionSupplier, ISelect2AjaxAdapter<T> adapter) {
		super(id, new ConcreteCollectionToCollectionWrapperModel<T, C>(model, collectionSupplier), adapter);
		
		fillSelect2Settings(getSettings());
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new AttributeAppender("style", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String load() {
				return "width: " + width.getWidth() + "px";
			}
		}));
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		if (isRequired()) {
			Select2Utils.setRequiredSettings(getSettings());
		}
	}

	protected void fillSelect2Settings(Select2Settings settings) {
		Select2Utils.setDefaultAjaxSettings(settings);
	}
	
	public GenericSelect2AjaxDropDownMultipleChoice<T> setWidth(IDropDownChoiceWidth width) {
		Args.notNull(width, "width");
		this.width = width;
		return this;
	}
}
