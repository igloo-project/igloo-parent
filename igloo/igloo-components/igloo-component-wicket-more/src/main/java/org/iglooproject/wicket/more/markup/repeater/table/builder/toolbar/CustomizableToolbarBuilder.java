package org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.component.CoreLabel;
import org.iglooproject.wicket.condition.Condition;
import org.iglooproject.wicket.factory.IOneParameterComponentFactory;
import org.iglooproject.wicket.model.Detachables;
import org.iglooproject.wicket.more.markup.repeater.table.CoreDataTable;
import org.iglooproject.wicket.more.markup.repeater.table.builder.DataTableBuilder;
import org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state.IAddedToolbarCoreElementState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state.IAddedToolbarElementState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state.IAddedToolbarLabelElementState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.toolbar.state.IToolbarElementState;
import org.iglooproject.wicket.more.markup.repeater.table.toolbar.CoreCustomizableToolbar;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class CustomizableToolbarBuilder<T, S extends ISort<?>> implements IToolbarElementState<T, S> {

	private final DataTableBuilder<T, S> dataTableBuilder;

	private final List<CustomizableToolbarElementBuilder<T, S>> builders = Lists.newArrayList();

	private boolean hideIfEmpty = false;

	public CustomizableToolbarBuilder(DataTableBuilder<T, S> dataTableBuilder) {
		super();
		this.dataTableBuilder = dataTableBuilder;
	}

	private abstract class CustomizableToolbarBuilderWrapper implements IToolbarElementState<T, S> {
		@Override
		public IAddedToolbarLabelElementState<T, S> addLabel(IModel<String> model) {
			return CustomizableToolbarBuilder.this.addLabel(model);
		}
		@Override
		public IAddedToolbarCoreElementState<T, S> addComponent(IOneParameterComponentFactory<Component, CoreDataTable<T, S>> delegateFactory) {
			return CustomizableToolbarBuilder.this.addComponent(delegateFactory);
		}
		@Override
		public IToolbarElementState<T, S> hideIfEmpty() {
			return CustomizableToolbarBuilder.this.hideIfEmpty();
		}
		@Override
		public DataTableBuilder<T, S> end() {
			return CustomizableToolbarBuilder.this.end();
		}
	}

	private abstract class AddedToolbarElementState<NextState extends IAddedToolbarElementState<T, S>>
			extends CustomizableToolbarBuilderWrapper implements IAddedToolbarElementState<T, S> {
		
		protected abstract CustomizableToolbarElementBuilder<T, S> getFactory();
		
		protected abstract NextState getNextState();
		
		@Override
		public NextState when(Condition condition) {
			getFactory().when(condition);
			return getNextState();
		}
		
		@Override
		public NextState withClass(String cssClass) {
			getFactory().withClass(cssClass);
			return getNextState();
		}
		
		@Override
		public NextState colspan(Integer colspan) {
			getFactory().colspan(colspan);
			return getNextState();
		}
		
		@Override
		public NextState full() {
			getFactory().full();
			return getNextState();
		}
	}

	private class AddedToolbarLabelElementState extends AddedToolbarElementState<IAddedToolbarLabelElementState<T, S>>
			implements IAddedToolbarLabelElementState<T, S> {
		
		private final CustomizableToolbarElementBuilder<T, S> factory;
		
		public AddedToolbarLabelElementState(CustomizableToolbarElementBuilder<T, S> factory) {
			super();
			this.factory = factory;
		}
		
		@Override
		protected CustomizableToolbarElementBuilder<T, S> getFactory() {
			return factory;
		}
		
		@Override
		protected IAddedToolbarLabelElementState<T, S> getNextState() {
			return this;
		}
	}
	
	private class AddedToolbarLabelCoreElementState extends AddedToolbarElementState<IAddedToolbarCoreElementState<T, S>>
			implements IAddedToolbarCoreElementState<T, S> {
		
		private final CustomizableToolbarElementBuilder<T, S> elementBuilder;
		
		public AddedToolbarLabelCoreElementState(CustomizableToolbarElementBuilder<T, S> elementBuilder) {
			super();
			this.elementBuilder = elementBuilder;
		}
		
		@Override
		protected CustomizableToolbarElementBuilder<T, S> getFactory() {
			return elementBuilder;
		}
		
		@Override
		protected IAddedToolbarCoreElementState<T, S> getNextState() {
			return this;
		}
	}

	@Override
	public IAddedToolbarLabelElementState<T, S> addLabel(final IModel<String> model) {
		CustomizableToolbarElementBuilder<T, S> elementBuilder = new CustomizableToolbarElementBuilder<>(
				getPreviousColspan(builders.size()),
				new CustomizableToolbarLabelElementDelegateFactory<T, S>(model)
		);
		builders.add(elementBuilder);
		return new AddedToolbarLabelElementState(elementBuilder);
	}

	public static class CustomizableToolbarLabelElementDelegateFactory<T, S extends ISort<?>>
			implements IOneParameterComponentFactory<Component, CoreDataTable<T, S>> {
		private static final long serialVersionUID = 1L;
		
		private final IModel<String> model;
		
		public CustomizableToolbarLabelElementDelegateFactory(IModel<String> model) {
			super();
			this.model = model;
		}
		
		@Override
		public Component create(String wicketId, CoreDataTable<T, S> parameter) {
			return new CoreLabel(wicketId, model);
		}
		
		@Override
		public void detach() {
			Detachables.detach(model);
		}
	}

	@Override
	public IAddedToolbarCoreElementState<T, S> addComponent(IOneParameterComponentFactory<Component, CoreDataTable<T, S>> delegateFactory) {
		CustomizableToolbarElementBuilder<T, S> elementBuilder = new CustomizableToolbarElementBuilder<>(
				getPreviousColspan(builders.size()), delegateFactory
		);
		builders.add(elementBuilder);
		return new AddedToolbarLabelCoreElementState(elementBuilder);
	}

	@Override
	public IToolbarElementState<T, S> hideIfEmpty() {
		this.hideIfEmpty = true;
		return this;
	}

	@Override
	public DataTableBuilder<T, S> end() {
		return dataTableBuilder;
	}

	public CoreCustomizableToolbar<T, S> build(final CoreDataTable<T, S> table) {
		CoreCustomizableToolbar<T, S> component = new CoreCustomizableToolbar<>(table, builders);
		if (hideIfEmpty) {
			component.add(
					Condition.isNotEmpty(table.getSequenceProvider()).thenShow()
			);
		}
		return component;
	}

	private Integer getPreviousColspan(final int currentIndex) {
		Integer previousColspan = 0;
		if (builders.isEmpty()) {
			return previousColspan;
		}
		for (CustomizableToolbarElementBuilder<T, S> builder : FluentIterable.from(builders).limit(currentIndex)) {
			if (builder.getColspan() == null) {
				previousColspan = null;
				break;
			}
			previousColspan = previousColspan + builder.getColspan();
		}
		return previousColspan;
	}
}
