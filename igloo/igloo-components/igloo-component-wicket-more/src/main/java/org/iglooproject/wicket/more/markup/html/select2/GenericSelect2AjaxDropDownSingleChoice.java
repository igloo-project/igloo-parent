package org.iglooproject.wicket.more.markup.html.select2;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.util.convert.ConversionException;
import org.retzlaff.select2.ISelect2AjaxAdapter;
import org.retzlaff.select2.Select2Behavior;
import org.retzlaff.select2.Select2Settings;
import org.retzlaff.select2.Select2SingleChoice;
import org.springframework.util.StringUtils;

import org.iglooproject.wicket.more.markup.html.select2.util.DropDownChoiceWidth;
import org.iglooproject.wicket.more.markup.html.select2.util.IDropDownChoiceWidth;
import org.iglooproject.wicket.more.markup.html.select2.util.Select2Utils;
import org.iglooproject.wicket.more.model.BindingModel;

public class GenericSelect2AjaxDropDownSingleChoice<T> extends Select2SingleChoice<T> {

	private static final long serialVersionUID = 6355575209286187233L;
	
	private IDropDownChoiceWidth width = DropDownChoiceWidth.AUTO;

	protected GenericSelect2AjaxDropDownSingleChoice(String id, IModel<T> model, ISelect2AjaxAdapter<T> adapter) {
		super(id, model, adapter);
		
		fillSelect2Settings(getSettings());
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		add(new AttributeModifier("style", new LoadableDetachableModel<String>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected String load() {
				return "width: " + width.getWidth();
			}
		}) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled(Component component) {
				return width != null;
			}
		});
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
	
	public GenericSelect2AjaxDropDownSingleChoice<T> setWidth(IDropDownChoiceWidth width) {
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
	public void convertInput() {
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
