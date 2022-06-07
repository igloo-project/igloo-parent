package org.iglooproject.basicapp.web.application.history.component.factory;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import com.google.common.collect.Maps;

import org.iglooproject.basicapp.core.business.history.model.HistoryDifference;
import org.iglooproject.basicapp.web.application.history.component.CompositeHistoryDifferencePanel;
import org.iglooproject.basicapp.web.application.history.component.DefaultHistoryDifferencePanel;
import org.iglooproject.commons.util.fieldpath.FieldPath;
import org.iglooproject.wicket.model.Detachables;

/**
 * A {@link IHistoryComponentFactory} allowing the caller to customize the way some particular fields are displayed.
 * <p>The customization is done through the <code>customXXX()</code> methods.
 */
public class CustomizableHistoryComponentFactory implements IHistoryComponentFactory {
	
	private static final long serialVersionUID = 7490592521281331323L;
	
	private final IHistoryComponentFactory defaultFactory;
	
	private final Map<FieldPath, IHistoryComponentFactory> specificComponentFactories = Maps.newHashMap();
	
	private final IHistoryComponentFactory compositeBlockFactory;
	
	private final IHistoryComponentFactory compositeInlineFactory;
	
	private transient boolean detaching = false;

	public CustomizableHistoryComponentFactory() {
		this(DefaultHistoryDifferencePanel.factory());
	}

	public CustomizableHistoryComponentFactory(IHistoryComponentFactory defaultFactory) {
		super();
		this.defaultFactory = defaultFactory;
		this.compositeBlockFactory = CompositeHistoryDifferencePanel.block(this);
		this.compositeInlineFactory = CompositeHistoryDifferencePanel.inline(this);
	}

	@Override
	public Component create(String wicketId, IModel<HistoryDifference> parameter) {
		IHistoryComponentFactory factory = getFactory(parameter.getObject());
		return factory.create(wicketId, parameter);
	}
	
	private IHistoryComponentFactory getFactory(HistoryDifference difference) {
		FieldPath path = difference.getPath().getPath();
		IHistoryComponentFactory result = specificComponentFactories.get(path);
		if (result != null) {
			return result;
		} else {
			return defaultFactory;
		}
	}

	/**
	 * Use a custom {@link IHistoryComponentFactory} for the given field.
	 */
	public CustomizableHistoryComponentFactory customForPath(FieldPath path, IHistoryComponentFactory factory) {
		specificComponentFactories.put(path, factory);
		return this;
	}

	/**
	 * Use a custom {@link IHistoryComponentFactory} for the given field.
	 */
	public CustomizableHistoryComponentFactory customForBinding(BindingRoot<?, ?> binding, IHistoryComponentFactory factory) {
		return customForPath(FieldPath.fromBinding(binding), factory);
	}

	/**
	 * Use a custom {@link IHistoryComponentFactory} for the given field's items.
	 */
	public CustomizableHistoryComponentFactory customForBindingItem(BindingRoot<?, ?> binding, IHistoryComponentFactory factory) {
		return customForPath(FieldPath.fromBinding(binding).item(), factory);
	}
	
	/**
	 * Use the {@link CompositeHistoryDifferencePanel#block(IHistoryComponentFactory) block composite}
	 * layout for the given field.
	 */
	public CustomizableHistoryComponentFactory compositeBlockForPath(FieldPath path) {
		return customForPath(path, compositeBlockFactory);
	}

	/**
	 * Use the {@link CompositeHistoryDifferencePanel#block(IHistoryComponentFactory) block composite}
	 * layout for the given field.
	 */
	public CustomizableHistoryComponentFactory compositeBlockForBinding(BindingRoot<?, ?> binding) {
		return customForBinding(binding, compositeBlockFactory);
	}

	/**
	 * Use the {@link CompositeHistoryDifferencePanel#block(IHistoryComponentFactory) block composite}
	 * layout for the given field's items.
	 */
	public CustomizableHistoryComponentFactory compositeBlockForBindingItem(BindingRoot<?, ?> binding) {
		return customForBindingItem(binding, compositeBlockFactory);
	}

	/**
	 * Use the {@link CompositeHistoryDifferencePanel#inline(IHistoryComponentFactory) inline composite}
	 * layout for the given field.
	 */
	public CustomizableHistoryComponentFactory compositeInlineForPath(FieldPath path) {
		return customForPath(path, compositeInlineFactory);
	}

	/**
	 * Use the {@link CompositeHistoryDifferencePanel#inline(IHistoryComponentFactory) inline composite}
	 * layout for the given field.
	 */
	public CustomizableHistoryComponentFactory compositeInlineForBinding(BindingRoot<?, ?> binding) {
		return customForBinding(binding, compositeInlineFactory);
	}

	/**
	 * Use the {@link CompositeHistoryDifferencePanel#inline(IHistoryComponentFactory) inline composite}
	 * layout for the given field's items.
	 */
	public CustomizableHistoryComponentFactory compositeInlineForBindingItem(BindingRoot<?, ?> binding) {
		return customForBindingItem(binding, compositeInlineFactory);
	}

	@Override
	public void detach() {
		if (!detaching) {
			try {
				this.detaching = true;
				Detachables.detach(specificComponentFactories.values());
			} finally {
				this.detaching = false;
			}
		}
	}

}
