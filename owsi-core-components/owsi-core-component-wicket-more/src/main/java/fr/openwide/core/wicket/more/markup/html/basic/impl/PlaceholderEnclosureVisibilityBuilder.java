package fr.openwide.core.wicket.more.markup.html.basic.impl;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.wicket.more.markup.html.basic.IPlaceholderEnclosureBuilder;

public class PlaceholderEnclosureVisibilityBuilder implements IDetachable, IPlaceholderEnclosureBuilder<PlaceholderEnclosureVisibilityBuilder> {
	
	private static final long serialVersionUID = -3036412805097142177L;
	
	private static final IModel<?> NULL_MODEL = new AbstractReadOnlyModel<Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Object getObject() {
			return null;
		}
	};

	public static enum Visibility {
		SHOW_IF_EMPTY,
		HIDE_IF_EMPTY
	}
	
	private final Visibility visibility;
	
	private final List<Component> components = Lists.newArrayList();
	
	private final List<PredicateWrapper<?>> predicateWrappers = Lists.newArrayList();
	
	private static class PredicateWrapper<T> implements IDetachable {
		private static final long serialVersionUID = 1L;
		
		private final Predicate<? super T> predicate;
		private final IModel<? extends T> model;
		
		@SuppressWarnings("unchecked")
		public PredicateWrapper(Predicate<? super T> predicate, IModel<? extends T> model) {
			super();
			this.predicate = predicate;
			this.model = model == null ? (IModel<T>) NULL_MODEL : model;
		}
		
		public boolean apply() {
			return predicate.apply(model.getObject());
		}
		
		@Override
		public void detach() {
			if (predicate instanceof IDetachable) {
				((IDetachable)predicate).detach();
			}
			model.detach();
		}
	}
	
	public PlaceholderEnclosureVisibilityBuilder(Visibility visibility) {
		super();
		this.visibility = visibility;
	}
	
	@Override
	public PlaceholderEnclosureVisibilityBuilder model(IModel<?> model) {
		return model(Predicates.notNull(), model);
	}

	@Override
	public PlaceholderEnclosureVisibilityBuilder collectionModel(IModel<? extends Collection<?>> model) {
		return model(Predicates2.notEmpty(), model);
	}
	
	@Override
	public <T2> PlaceholderEnclosureVisibilityBuilder model(Predicate<? super T2> predicate, IModel<? extends T2> model) {
		predicateWrappers.add(new PredicateWrapper<T2>(predicate, model));
		return this;
	}
	
	@Override
	public PlaceholderEnclosureVisibilityBuilder models(IModel<?> firstModel, IModel<?>... otherModels) {
		for (IModel<?> model : Lists.asList(firstModel, otherModels)) {
			model(model);
		}
		return this;
	}
	
	@Override
	public <T2> PlaceholderEnclosureVisibilityBuilder models(Predicate<? super T2> predicate, IModel<? extends T2> firstModel, IModel<? extends T2>... otherModels) {
		for (IModel<? extends T2> model : Lists.asList(firstModel, otherModels)) {
			model(predicate, model);
		}
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
		
		for (PredicateWrapper<?> predicateWrapper : predicateWrappers) {
			if (predicateWrapper.apply()) {
				return Visibility.HIDE_IF_EMPTY.equals(visibility);
			}
		}
		
		return Visibility.SHOW_IF_EMPTY.equals(visibility);
	}
	
	@Override
	public void detach() {
		for (PredicateWrapper<?> predicateWrapper : predicateWrappers) {
			predicateWrapper.detach();
		}
	}

}
