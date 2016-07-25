package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.retzlaff.select2.Select2Behavior;
import org.retzlaff.select2.Select2Settings;

import com.google.common.base.Supplier;

import fr.openwide.core.wicket.markup.html.form.ListMultipleChoice;
import fr.openwide.core.wicket.markup.html.model.ConcreteCollectionToCollectionWrapperModel;
import fr.openwide.core.wicket.more.markup.html.model.MultipleChoicesWrapperModel;
import fr.openwide.core.wicket.more.markup.html.select2.util.DropDownChoiceWidth;
import fr.openwide.core.wicket.more.markup.html.select2.util.IDropDownChoiceWidth;
import fr.openwide.core.wicket.more.markup.html.select2.util.Select2Utils;

public abstract class GenericSelect2DropDownMultipleChoice<T> extends ListMultipleChoice<T> {
	
	private static final long serialVersionUID = -6179538711780820058L;
	
	private IDropDownChoiceWidth width = DropDownChoiceWidth.AUTO;
	
	private MultipleChoicesWrapperModel<T> choicesWrapperModel;
	
	private final Select2Behavior<T, T> select2Behavior;
	
	/**
	 * @param id The wicket ID.
	 * @param collectionModel The model containing the selected elements.
	 * @param collectionSupplier A factory for new, empty collections to be put in <code>collectionModel</code>
	 * @param choicesModel The model containing the selectable elements.
	 * @param renderer The choice renderer.
	 */
	protected <C extends Collection<T>> GenericSelect2DropDownMultipleChoice(
			String id, IModel<C> collectionModel, Supplier<? extends C> collectionSupplier,
			IModel<? extends Collection<? extends T>> choicesModel, IChoiceRenderer<? super T> renderer) {
		super(id);
		
		setModel(new ConcreteCollectionToCollectionWrapperModel<T, C>(collectionModel, collectionSupplier));
		
		choicesWrapperModel = new MultipleChoicesWrapperModel<T>(collectionModel, choicesModel);
		setChoices(choicesWrapperModel);
		setSelectedObjectForcedInChoices(true);
		setChoiceRenderer(renderer);
		
		select2Behavior = Select2Behavior.forChoice(this);
		fillSelect2Settings(select2Behavior.getSettings());
		add(select2Behavior);
	}
	
	@SuppressWarnings("unchecked")
	protected void ensureChoicesModelIsWrapped() {
		/*
		 * Ideally this wrapping should be done in setChoices(IModel) or in wrap(IModel),
		 * but those methods cannot be overriden...
		 */
		IModel<? extends List<? extends T>> choicesModel = getChoicesModel();
		if (choicesModel != choicesWrapperModel) {
			if (choicesModel instanceof MultipleChoicesWrapperModel) {
				this.choicesWrapperModel = (MultipleChoicesWrapperModel<T>) choicesModel;
			} else {
				this.choicesWrapperModel = new MultipleChoicesWrapperModel<>(getModel(), choicesModel);
				setChoices(choicesWrapperModel);
			}
		}
	}
	
	@Override
	protected void onInitialize() {
		ensureChoicesModelIsWrapped();
		
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
		ensureChoicesModelIsWrapped();
		
		super.onConfigure();
		if (isRequired()) {
			Select2Utils.setRequiredSettings(getSettings());
		}
	}

	protected void fillSelect2Settings(Select2Settings settings) {
		Select2Utils.setDefaultSettings(settings);
	}
	
	protected final Select2Settings getSettings() {
		return select2Behavior.getSettings();
	}
	
	public GenericSelect2DropDownMultipleChoice<T> setWidth(IDropDownChoiceWidth width) {
		this.width = width;
		return this;
	}
	
	public boolean isSelectedObjectForcedInChoices() {
		return choicesWrapperModel.isSelectedObjectForcedInChoices();
	}
	
	public GenericSelect2DropDownMultipleChoice<T> setSelectedObjectForcedInChoices(boolean selectedObjectForcedInChoices) {
		choicesWrapperModel.setSelectedObjectForcedInChoices(selectedObjectForcedInChoices);
		return this;
	}
}
