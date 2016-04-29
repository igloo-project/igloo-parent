package fr.openwide.core.wicket.more.markup.html.basic.impl;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.wicket.more.condition.BooleanOperator;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.AbstractComponentBooleanPropertyBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.ComponentBooleanProperty;
import fr.openwide.core.wicket.more.markup.html.basic.IPlaceholderEnclosureBuilder;

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
		WHEN_ALL_TRUE(BooleanOperator.AND),
		WHEN_ANY_TRUE(BooleanOperator.OR),
		WHEN_ALL_FALSE(BooleanOperator.NOR),
		WHEN_ANY_FALSE(BooleanOperator.NAND);
		
		private final BooleanOperator booleanOperator;
		
		private Operator(BooleanOperator booleanOperator) {
			this.booleanOperator = booleanOperator;
		}
		
		public BooleanOperator getBooleanOperator() {
			return booleanOperator;
		}
	}

	private final BooleanOperator operator;
	
	private final List<Condition> conditions = Lists.newArrayList();
	
	protected AbstractConfigurableComponentBooleanPropertyBehavior(ComponentBooleanProperty property, Operator operator) {
		this(property, operator.getBooleanOperator());
	}
	
	private AbstractConfigurableComponentBooleanPropertyBehavior(ComponentBooleanProperty property, BooleanOperator operator) {
		super(property);
		this.operator = operator;
		Injector.get().inject(this);
	}
	
	@Override
	protected boolean generatePropertyValue(Component attachedComponent) {
		return operator.apply(conditions);
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
	@SuppressWarnings("unchecked")
	public <T2> T model(Predicate<? super T2> predicate, IModel<? extends T2> model) {
		conditions.add(Condition.predicate(model == null ? (IModel<? extends T2>) NULL_MODEL : model, predicate));
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
		conditions.add(Condition.visible(component));
		return thisAsT();
	}
	
	@Override
	public T components(Component firstComponent, Component... otherComponents) {
		return components(Lists.asList(firstComponent, otherComponents));
	}
	
	@Override
	public T components(Collection<? extends Component> targetComponents) {
		for (Component component : targetComponents) {
			conditions.add(Condition.visible(component));
		}
		return thisAsT();
	}
	
	public T condition(Condition condition) {
		conditions.add(condition);
		return thisAsT();
	}
	
	@Override
	public void detach(Component component) {
		super.detach(component);
		for (Condition condition : conditions) {
			condition.detach();
		}
	}

}
