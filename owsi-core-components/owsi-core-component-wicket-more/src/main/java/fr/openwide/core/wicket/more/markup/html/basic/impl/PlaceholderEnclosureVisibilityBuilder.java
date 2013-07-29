package fr.openwide.core.wicket.more.markup.html.basic.impl;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;

import fr.openwide.core.wicket.more.markup.html.basic.IPlaceholderEnclosureBuilder;

public class PlaceholderEnclosureVisibilityBuilder implements IDetachable, IPlaceholderEnclosureBuilder<PlaceholderEnclosureVisibilityBuilder> {
	
	private static final long serialVersionUID = -3036412805097142177L;

	public static enum Visibility {
		SHOW_IF_EMPTY,
		HIDE_IF_EMPTY
	}
	
	private final Visibility visibility;
	
	private final List<IModel<?>> models = Lists.newArrayList();

	private final List<IModel<? extends Collection<?>>> collectionModels = Lists.newArrayList();
	
	private final List<Component> components = Lists.newArrayList();
	
	public PlaceholderEnclosureVisibilityBuilder(Visibility visibility) {
		super();
		this.visibility = visibility;
	}

	@Override
	public PlaceholderEnclosureVisibilityBuilder collectionModel(IModel<? extends Collection<?>> model) {
		collectionModels.add(model);
		return this;
	}
	
	@Override
	public PlaceholderEnclosureVisibilityBuilder model(IModel<?> model) {
		models.add(model);
		return this;
	}
	
	@Override
	public PlaceholderEnclosureVisibilityBuilder models(IModel<?> firstModel, IModel<?>... otherModels) {
		models.addAll(Lists.asList(firstModel, otherModels));
		return this;
	}
	
	@Override
	public PlaceholderEnclosureVisibilityBuilder component(Component component) {
		components.add(component);
		return this;
	}
	
	@Override
	public PlaceholderEnclosureVisibilityBuilder components(Component firstComponent, Component... otherComponents) {
		components.addAll(Lists.asList(firstComponent, otherComponents));
		return this;
	}
	
	public boolean isVisible() {
		for (Component component : components) {
			component.configure();
			if (component.determineVisibility()) {
				return Visibility.HIDE_IF_EMPTY.equals(visibility);
			}
		}
		
		for (IModel<? extends Collection<?>> collectionModel : collectionModels) {
			if (collectionModel != null && collectionModel.getObject() != null && collectionModel.getObject().size() > 0) {
				return Visibility.HIDE_IF_EMPTY.equals(visibility);
			}
		}
		
		for (IModel<?> model : models) {
			if (model != null && model.getObject() != null) {
				return Visibility.HIDE_IF_EMPTY.equals(visibility);
			}
		}
		
		return Visibility.SHOW_IF_EMPTY.equals(visibility);
	}
	
	@Override
	public void detach() {
		for (IModel<?> listModel : collectionModels) {
			listModel.detach();
		}
		
		for (IModel<?> model : models) {
			model.detach();
		}
	}

}
