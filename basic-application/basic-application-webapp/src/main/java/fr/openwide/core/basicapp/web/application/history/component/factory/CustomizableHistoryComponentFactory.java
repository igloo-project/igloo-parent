package fr.openwide.core.basicapp.web.application.history.component.factory;

import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.bindgen.BindingRoot;

import com.google.common.collect.Maps;

import fr.openwide.core.basicapp.core.business.history.model.HistoryDifference;
import fr.openwide.core.basicapp.web.application.history.component.CompositeHistoryDifferencePanel;
import fr.openwide.core.basicapp.web.application.history.component.DefaultHistoryDifferencePanel;
import fr.openwide.core.jpa.more.util.fieldpath.model.FieldPath;
import fr.openwide.core.wicket.more.util.model.Detachables;

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
	
	public CustomizableHistoryComponentFactory customForPath(FieldPath path, IHistoryComponentFactory factory) {
		specificComponentFactories.put(path, factory);
		return this;
	}
	
	public CustomizableHistoryComponentFactory customForBinding(BindingRoot<?, ?> binding, IHistoryComponentFactory factory) {
		return customForPath(FieldPath.fromBinding(binding), factory);
	}
	
	public CustomizableHistoryComponentFactory customForBindingItem(BindingRoot<?, ?> binding, IHistoryComponentFactory factory) {
		return customForPath(FieldPath.fromBinding(binding).item(), factory);
	}
	
	public CustomizableHistoryComponentFactory compositeBlockForPath(FieldPath path) {
		return customForPath(path, compositeBlockFactory);
	}
	
	public CustomizableHistoryComponentFactory compositeBlockForBinding(BindingRoot<?, ?> binding) {
		return customForBinding(binding, compositeBlockFactory);
	}
	
	public CustomizableHistoryComponentFactory compositeBlockForBindingItem(BindingRoot<?, ?> binding) {
		return customForBindingItem(binding, compositeBlockFactory);
	}
	
	public CustomizableHistoryComponentFactory compositeInlineForPath(FieldPath path) {
		return customForPath(path, compositeInlineFactory);
	}
	
	public CustomizableHistoryComponentFactory compositeInlineForBinding(BindingRoot<?, ?> binding) {
		return customForBinding(binding, compositeInlineFactory);
	}
	
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
