package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.retzlaff.select2.Select2Behavior;
import org.retzlaff.select2.Select2Settings;

import fr.openwide.core.wicket.more.markup.html.select2.util.DropDownChoiceWidth;
import fr.openwide.core.wicket.more.markup.html.select2.util.Select2Utils;

public abstract class GenericSelect2DropDownSingleChoice<T> extends DropDownChoice<T> {
	private static final long serialVersionUID = -3776700270762640109L;
	
	/**
	 * Hack.
	 * @see DropDownChoiceWidth
	 */
	private DropDownChoiceWidth width = DropDownChoiceWidth.NORMAL;
	
	protected GenericSelect2DropDownSingleChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choicesModel, IChoiceRenderer<? super T> renderer) {
		super(id);
		
		setModel(model);
		setChoices(choicesModel);
		setChoiceRenderer(renderer);
		setNullValid(true);
		
		Select2Behavior<T, T> select2Behavior = Select2Behavior.forChoice(this);
		fillSelect2Settings(select2Behavior.getSettings());
		add(select2Behavior);
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
		Select2Utils.setDefaultSettings(settings);
	}
	
	public GenericSelect2DropDownSingleChoice<T> setWidth(DropDownChoiceWidth width) {
		this.width = width;
		return this;
	}
	
	protected String getRootKey() {
		return GenericSelect2DropDownSingleChoice.class.getSimpleName() + "." + this.getId();
	}
	
	@Override
	protected String getNullKey() {
		return getRootKey() + ".null";
	}
	
	@Override
	protected String getNullValidKey() {
		return getRootKey() + ".nullValid";
	}
}
