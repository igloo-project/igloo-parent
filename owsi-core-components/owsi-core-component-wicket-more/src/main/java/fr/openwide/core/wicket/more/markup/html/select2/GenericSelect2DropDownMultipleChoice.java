package fr.openwide.core.wicket.more.markup.html.select2;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
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
	
	/**
	 * Hack.
	 * @see IDropDownChoiceWidth
	 */
	private IDropDownChoiceWidth width = DropDownChoiceWidth.NORMAL;
	
	private MultipleChoicesWrapperModel<T> choicesWrapperModel;
	
	protected <C extends Collection<T>> GenericSelect2DropDownMultipleChoice(
			String id, IModel<C> collectionModel, Supplier<? extends C> collectionSupplier,
			IModel<? extends List<? extends T>> choicesModel, IChoiceRenderer<? super T> renderer) {
		super(id);
		
		setModel(new ConcreteCollectionToCollectionWrapperModel<T, C>(collectionModel, collectionSupplier));
		
		choicesWrapperModel = new MultipleChoicesWrapperModel<T>(collectionModel, choicesModel);
		setChoices(choicesWrapperModel);
		setSelectedObjectForcedInChoices(true);
		setChoiceRenderer(renderer);
		
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
	
	public GenericSelect2DropDownMultipleChoice<T> setWidth(IDropDownChoiceWidth width) {
		this.width = width;
		return this;
	}
	
	public boolean isSelectedObjectForcedInChoices() {
		return choicesWrapperModel.isSelectedObjectForcedInChoices();
	}
	
	public void setSelectedObjectForcedInChoices(boolean selectedObjectForcedInChoices) {
		choicesWrapperModel.setSelectedObjectForcedInChoices(selectedObjectForcedInChoices);
	}
}
