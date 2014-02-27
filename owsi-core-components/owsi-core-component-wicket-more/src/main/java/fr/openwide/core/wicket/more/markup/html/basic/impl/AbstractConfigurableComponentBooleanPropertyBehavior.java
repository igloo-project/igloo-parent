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
import fr.openwide.core.wicket.more.markup.html.basic.AbstractComponentBooleanPropertyBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.ComponentBooleanProperty;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.IPlaceholderEnclosureBuilder;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderBehavior;

public abstract class AbstractConfigurableComponentBooleanPropertyBehavior<T extends AbstractConfigurableComponentBooleanPropertyBehavior<T>>
		extends AbstractComponentBooleanPropertyBehavior
		implements IPlaceholderEnclosureBuilder<T> {

	private static final long serialVersionUID = 5054905572454226562L;
	
	private static final IModel<?> NULL_MODEL = new AbstractReadOnlyModel<Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Object getObject() {
			return null;
		}
	};

	public static enum Operator {
		/** AND(*) */
		WHEN_ALL_TRUE {
			@Override public boolean isDecisiveOperand(boolean operand) { return operand == false; }
			@Override public boolean getResultWhenDecisiveOperandMet() { return false; }
		},
		/** OR(*) */
		WHEN_ANY_TRUE {
			@Override public boolean isDecisiveOperand(boolean operand) { return operand == true; }
			@Override public boolean getResultWhenDecisiveOperandMet() { return true; }
		},
		/** NOT(OR(*)) */
		WHEN_ALL_FALSE {
			@Override public boolean isDecisiveOperand(boolean operand) { return operand == true; }
			@Override public boolean getResultWhenDecisiveOperandMet() { return false; }
		},
		/** NOT(AND(*)) */
		WHEN_ANY_FALSE {
			@Override public boolean isDecisiveOperand(boolean operand) { return operand == false; }
			@Override public boolean getResultWhenDecisiveOperandMet() { return true; }
		};
		
		public abstract boolean isDecisiveOperand(boolean operand);
		public abstract boolean getResultWhenDecisiveOperandMet();
	}

	private final Operator operator;
	
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
	
	protected AbstractConfigurableComponentBooleanPropertyBehavior(ComponentBooleanProperty property, Operator operator) {
		super(property);
		this.operator = operator;
	}
	
	@Override
	protected boolean generatePropertyValue(Component attachedComponent) {
		for (Component component : components) {
			component.configure();
			if (operator.isDecisiveOperand(component.determineVisibility())) {
				return operator.getResultWhenDecisiveOperandMet();
			}
		}
		
		for (PredicateWrapper<?> predicateWrapper : predicateWrappers) {
			if (operator.isDecisiveOperand(predicateWrapper.apply())) {
				return operator.getResultWhenDecisiveOperandMet();
			}
		}

		return !operator.getResultWhenDecisiveOperandMet();
	}
	
	/**
	 * @return this as an object of type T
	 * @see PlaceholderBehavior
	 * @see EnclosureBehavior
	 */
	protected abstract T thisAsT();
	
	
	@Override
	public T model(IModel<?> model) {
		return model(Predicates.notNull(), model);
	}

	@Override
	public T collectionModel(IModel<? extends Collection<?>> model) {
		return model(Predicates2.notEmpty(), model);
	}
	
	@Override
	public <T2> T model(Predicate<? super T2> predicate, IModel<? extends T2> model) {
		predicateWrappers.add(new PredicateWrapper<T2>(predicate, model));
		return thisAsT();
	}
	
	@Override
	public T models(IModel<?> firstModel, IModel<?>... otherModels) {
		for (IModel<?> model : Lists.asList(firstModel, otherModels)) {
			model(model);
		}
		return thisAsT();
	}

	@Override
	@SafeVarargs
	public final <T2> T models(Predicate<? super T2> predicate, IModel<? extends T2> firstModel,
			IModel<? extends T2>... otherModels) {
		for (IModel<? extends T2> model : Lists.asList(firstModel, otherModels)) {
			model(predicate, model);
		}
		return thisAsT();
	}
	
	@Override
	public T component(Component component) {
		components.add(component);
		return thisAsT();
	}
	
	@Override
	public T components(Component firstComponent, Component... otherComponents) {
		components.addAll(Lists.asList(firstComponent, otherComponents));
		return thisAsT();
	}
	
	@Override
	public T components(Collection<Component> targetComponents) {
		components.addAll(targetComponents);
		return thisAsT();
	}
	
	@Override
	public void detach(Component component) {
		super.detach(component);
		for (PredicateWrapper<?> predicateWrapper : predicateWrappers) {
			predicateWrapper.detach();
		}
	}

}
