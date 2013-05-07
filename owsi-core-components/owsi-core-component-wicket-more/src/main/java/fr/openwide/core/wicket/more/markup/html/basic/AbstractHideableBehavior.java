package fr.openwide.core.wicket.more.markup.html.basic;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

public abstract class AbstractHideableBehavior<T extends AbstractHideableBehavior<T>> extends Behavior {

	private static final long serialVersionUID = 5054905572454226562L;
	
	protected static enum Visibility {
		SHOW_IF_EMPTY,
		HIDE_IF_EMPTY
	}

	private final Visibility visibility;
	
	private final List<IModel<?>> models = Lists.newArrayList();

	private final List<IModel<? extends Collection<?>>> collectionModels = Lists.newArrayList();
	
	private final List<Component> components = Lists.newArrayList();
	
	protected AbstractHideableBehavior(Visibility visibility) {
		super();
		this.visibility = visibility;
	}
	
	/**
	 * @return this as an object of type T
	 * @see PlaceholderContainer
	 * @see EnclosureContainer
	 */
	protected abstract T thisAsT();
	
	public T collectionModel(IModel<? extends Collection<?>> model) {
		collectionModels.add(model);
		return thisAsT();
	}
	
	public T model(IModel<?> model) {
		models.add(model);
		return thisAsT();
	}
	
	public T models(IModel<?>... model) {
		models.addAll(Arrays.asList(model));
		return thisAsT();
	}
	
	public T component(Component component) {
		components.add(component);
		return thisAsT();
	}
	
	public T components(Component... component) {
		components.addAll(Arrays.asList(component));
		return thisAsT();
	}
	
	@Override
	public void onConfigure(Component boundComponent) {
		super.onConfigure(boundComponent);
		
		for (Component component : components) {
			component.configure();
			if (component.determineVisibility()) {
				boundComponent.setVisible(Visibility.HIDE_IF_EMPTY.equals(visibility));
				return;
			}
		}
		
		for (IModel<? extends Collection<?>> collectionModel : collectionModels) {
			if (collectionModel != null && collectionModel.getObject() != null && collectionModel.getObject().size() > 0) {
				boundComponent.setVisible(Visibility.HIDE_IF_EMPTY.equals(visibility));
				return;
			}
		}
		
		for (IModel<?> model : models) {
			if (model != null && model.getObject() != null) {
				boundComponent.setVisible(Visibility.HIDE_IF_EMPTY.equals(visibility));
				return;
			}
		}
		
		boundComponent.setVisible(Visibility.SHOW_IF_EMPTY.equals(visibility));
	}
	
	@Override
	public void detach(Component component) {
		super.detach(component);
		
		for (IModel<?> listModel : collectionModels) {
			listModel.detach();
		}
		
		for (IModel<?> model : models) {
			model.detach();
		}
	}

}
