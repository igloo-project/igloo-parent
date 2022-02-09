package org.iglooproject.wicket.api.condition;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.injection.Injector;

import com.google.common.collect.Lists;

public abstract class AbstractConfigurableComponentBooleanPropertyBehavior<T extends AbstractConfigurableComponentBooleanPropertyBehavior<T>>
		extends AbstractComponentBooleanPropertyBehavior
		implements IPlaceholderEnclosureBuilder<T> {

	private static final long serialVersionUID = 5054905572454226562L;
	
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
