package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.lang.Args;
import org.retzlaff.select2.ISelect2AjaxAdapter;
import org.retzlaff.select2.Select2Behavior;
import org.retzlaff.select2.Select2Settings;
import org.retzlaff.select2.Select2SingleChoice;
import org.springframework.util.StringUtils;

import fr.openwide.core.wicket.more.markup.html.select2.util.DropDownChoiceWidth;
import fr.openwide.core.wicket.more.markup.html.select2.util.Select2Utils;
import fr.openwide.core.wicket.more.model.BindingModel;

public class GenericSelect2AjaxDropDownSingleChoice<T> extends Select2SingleChoice<T> {

	private static final long serialVersionUID = 6355575209286187233L;
	
	/**
	 * Hack.
	 * @see DropDownChoiceWidth
	 */
	private DropDownChoiceWidth width = DropDownChoiceWidth.NORMAL;

	protected GenericSelect2AjaxDropDownSingleChoice(String id, IModel<T> model, ISelect2AjaxAdapter<T> adapter) {
		super(id, model, adapter);
		
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
	
	protected void fillSelect2Settings(Select2Settings settings) {
		Select2Utils.setDefaultAjaxSettings(settings);
	}
	
	public GenericSelect2AjaxDropDownSingleChoice<T> setWidth(DropDownChoiceWidth width) {
		Args.notNull(width, "width");
		this.width = width;
		return this;
	}

	/**
	 * Workaround.<br>
	 * The client method, {@link Select2Behavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)},
	 * does not handle input re-rendering when form validation fails.<br>
	 * Since overloading {@link Select2Behavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)}
	 * would be both tedious and dangerous, we handle it here...
	 */
	@Override
	protected List<T> getModelObjects() {
		T object = getConvertedInput();
		if (object == null) {
			return super.getModelObjects();
		}
		return Collections.singletonList(object);
	}
	
	/**
	 * Workaround.<br>
	 * When the underlying model is a {@link IObjectClassAwareModel} (a {@link BindingModel} for example),
	 * the {@link AbstractTextComponent#resolveType()} method will be able to determine the model type.<br>
	 * As a consequence, the {@link FormComponent#convertInput()} method will totally ignore {@link Select2SingleChoice#convertValue(String[]))},
	 * which is quite annoying since this is the method that makes use of {@link ISelect2AjaxAdapter#getChoice(String)}...<br>
	 * That's why we have to force the use of {@link #convertValue(String[])}.
	 */
	@Override
	protected void convertInput() {
		String[] value = getInputAsArray();
		String tmp = value != null && value.length > 0 ? value[0] : null;
		if (getConvertEmptyInputStringToNull() && !StringUtils.hasText(tmp)) {
			setConvertedInput(null);
		} else {
			try {
				setConvertedInput(convertValue(getInputAsArray()));
			} catch (ConversionException e) {
				error(newValidationError(e));
			}
		}
	}
}
