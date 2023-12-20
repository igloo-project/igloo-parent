package org.iglooproject.wicket.more.markup.html.select2;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableSupplier2;
import org.iglooproject.wicket.more.markup.html.model.MultipleChoicesWrapperModel;
import org.iglooproject.wicket.more.markup.html.select2.util.IDropDownChoiceWidth;
import org.iglooproject.wicket.more.markup.html.select2.util.Select2Utils;
import org.wicketstuff.select2.Select2Behavior;
import org.wicketstuff.select2.Settings;

import igloo.wicket.markup.html.form.ListMultipleChoice;
import igloo.wicket.model.ConcreteCollectionToCollectionWrapperModel;
import igloo.wicket.model.Detachables;

public class GenericSelect2DropDownMultipleChoice<T> extends ListMultipleChoice<T> {

	private static final long serialVersionUID = -6179538711780820058L;

	private MultipleChoicesWrapperModel<T> choicesWrapperModel;

	private final Select2Behavior select2Behavior;

	/**
	 * @param id The wicket ID.
	 * @param collectionModel The model containing the selected elements.
	 * @param collectionSupplier A factory for new, empty collections to be put in <code>collectionModel</code>
	 * @param choicesModel The model containing the selectable elements.
	 * @param renderer The choice renderer.
	 */
	protected <C extends Collection<T>> GenericSelect2DropDownMultipleChoice(
			String id, IModel<C> collectionModel, SerializableSupplier2<? extends C> collectionSupplier,
			IModel<? extends Collection<? extends T>> choicesModel, IChoiceRenderer<? super T> renderer) {
		super(id);
		
		setModel(new ConcreteCollectionToCollectionWrapperModel<>(collectionModel, collectionSupplier));
		
		choicesWrapperModel = new MultipleChoicesWrapperModel<>(collectionModel, choicesModel);
		setChoices(choicesWrapperModel);
		setSelectedObjectForcedInChoices(true);
		setChoiceRenderer(renderer);
		
		select2Behavior = Select2Behavior.forMultiChoice();
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
	protected void onConfigure() {
		ensureChoicesModelIsWrapped();
		
		super.onConfigure();
		
		if (isRequired()) {
			Select2Utils.setRequiredSettings(getSettings());
		} else {
			Select2Utils.setOptionalSettings(getSettings());
		}
	}

	protected void fillSelect2Settings(Settings settings) {
		Select2Utils.setDefaultSettings(settings);
	}

	public final Settings getSettings() {
		return select2Behavior.getSettings();
	}

	public GenericSelect2DropDownMultipleChoice<T> setWidth(IDropDownChoiceWidth width) {
		getSettings().setWidth(width.getWidth());
		return this;
	}

	public boolean isSelectedObjectForcedInChoices() {
		return choicesWrapperModel.isSelectedObjectForcedInChoices();
	}

	public GenericSelect2DropDownMultipleChoice<T> setSelectedObjectForcedInChoices(boolean selectedObjectForcedInChoices) {
		choicesWrapperModel.setSelectedObjectForcedInChoices(selectedObjectForcedInChoices);
		return this;
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("size", 1);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		Detachables.detach(choicesWrapperModel);
	}

}
