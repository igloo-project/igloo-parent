package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.builder;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.springframework.security.acls.model.Permission;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.impossibl.postgres.utils.guava.Joiner;

import fr.openwide.core.commons.util.binding.AbstractCoreBinding;
import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.link.descriptor.generator.ILinkGenerator;
import fr.openwide.core.wicket.more.link.descriptor.mapper.BindingOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.link.descriptor.mapper.IOneParameterLinkDescriptorMapper;
import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAction;
import fr.openwide.core.wicket.more.markup.html.action.IOneParameterAjaxAction;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.OneParameterConditionFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.DataTableBuilder;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.CoreActionColumn;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.AbstractActionColumnElementFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.ActionColumnActionFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.ActionColumnAjaxActionFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.ActionColumnConfirmActionFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.ActionColumnLinkFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state.IActionColumnAddedActionState;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state.IActionColumnAddedAjaxActionState;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state.IActionColumnAddedConfirmActionState;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state.IActionColumnAddedElementState;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state.IActionColumnAddedLinkState;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.state.IActionColumnBuildState;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.state.IAddedCoreColumnState;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.confirm.component.AjaxConfirmLink;

public class ActionColumnBuilder<T, S extends ISort<?>> implements IActionColumnBuildState<T, S> {

	private final DataTableBuilder<T, S> dataTableBuilder;

	private final IModel<String> headerLabelModel;

	private final Set<String> cssClassesOnElements = Sets.newHashSet();

	private final List<AbstractActionColumnElementFactory<T, ?>> factories = Lists.newArrayList();

	public ActionColumnBuilder(DataTableBuilder<T, S> dataTableBuilder, IModel<String> headerLabelModel) {
		this.dataTableBuilder = dataTableBuilder;
		this.headerLabelModel = headerLabelModel;
	}

	private abstract class ActionColumnBuilderWrapper implements IActionColumnBuildState<T, S> {
		
		@Override
		public IActionColumnAddedLinkState<T, S> addLink(BootstrapRenderer<? super T> renderer,
				IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
			return ActionColumnBuilder.this.addLink(renderer, mapper);
		}
		
		@Override
		public <C> IActionColumnAddedLinkState<T, S> addLink(BootstrapRenderer<? super T> renderer,
				AbstractCoreBinding<? super T, C> binding,
				IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, C> mapper) {
			return ActionColumnBuilder.this.addLink(renderer, binding, mapper);
		}
		
		@Override
		public IActionColumnAddedLinkState<T, S> addLabelledLink(BootstrapRenderer<? super T> renderer,
				IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
			return ActionColumnBuilder.this.addLabelledLink(renderer, mapper);
		}
		
		@Override
		public <C> IActionColumnAddedLinkState<T, S> addLabelledLink(BootstrapRenderer<? super T> renderer,
				AbstractCoreBinding<? super T, C> binding,
				IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, C> mapper) {
			return ActionColumnBuilder.this.addLabelledLink(renderer, binding, mapper);
		}
		
		@Override
		public IActionColumnAddedAjaxActionState<T, S> addAction(BootstrapRenderer<? super T> renderer,
				IOneParameterAjaxAction<IModel<T>> action) {
			return ActionColumnBuilder.this.addAction(renderer, action);
		}
		
		@Override
		public IActionColumnAddedAjaxActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
				IOneParameterAjaxAction<IModel<T>> action) {
			return ActionColumnBuilder.this.addLabelledAction(renderer, action);
		}
		
		@Override
		public IActionColumnAddedActionState<T, S> addAction(BootstrapRenderer<? super T> renderer,
				IOneParameterAction<IModel<T>> action) {
			return ActionColumnBuilder.this.addAction(renderer, action);
		}
		
		@Override
		public IActionColumnAddedActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
				IOneParameterAction<IModel<T>> action) {
			return ActionColumnBuilder.this.addLabelledAction(renderer, action);
		}
		
		@Override
		public ActionColumnConfirmActionBuilder<T, S> addConfirmAction(BootstrapRenderer<? super T> renderer) {
			return ActionColumnBuilder.this.addConfirmAction(renderer);
		}
		
		@Override
		public IActionColumnBuildState<T, S> withClassOnElements(String cssClassOnElements) {
			return ActionColumnBuilder.this.withClassOnElements(cssClassOnElements);
		}
		
		@Override
		public IAddedCoreColumnState<T, S> end() {
			return ActionColumnBuilder.this.end();
		}
		
	}

	private abstract class ActionColumnAddedElementState<NextState extends IActionColumnAddedElementState<T, S>>
			extends ActionColumnBuilderWrapper implements IActionColumnAddedElementState<T, S> {
		
		protected abstract NextState getNextState();
		
		protected abstract AbstractActionColumnElementFactory<T, ?> getFactory();
		
		@Override
		public NextState showLabel() {
			getFactory().showLabel();
			return getNextState();
		}
		
		@Override
		public NextState showLabel(Condition showLabelCondition) {
			getFactory().showLabel(showLabelCondition);
			return getNextState();
		}
		
		@Override
		public NextState hideLabel() {
			getFactory().hideLabel();
			return getNextState();
		}
		
		@Override
		public NextState hideLabel(Condition hideLabelCondition) {
			getFactory().hideLabel(hideLabelCondition);
			return getNextState();
		}
		
		@Override
		public NextState showTooltip() {
			getFactory().showTooltip();
			return getNextState();
		}
		
		@Override
		public NextState showTooltip(Condition showTooltipCondition) {
			getFactory().showTooltip(showTooltipCondition);
			return getNextState();
		}
		
		@Override
		public NextState hideTooltip() {
			getFactory().hideTooltip();
			return getNextState();
		}
		
		@Override
		public NextState hideTooltip(Condition hideTooltipCondition) {
			getFactory().hideTooltip(hideTooltipCondition);
			return getNextState();
		}
		
		@Override
		public NextState showIcon() {
			getFactory().showIcon();
			return getNextState();
		}
		
		@Override
		public NextState showIcon(Condition showIconCondition) {
			getFactory().showIcon(showIconCondition);
			return getNextState();
		}
		
		@Override
		public NextState hideIcon() {
			getFactory().hideIcon();
			return getNextState();
		}
		
		@Override
		public NextState hideIcon(Condition hideIconCondition) {
			getFactory().hideIcon(hideIconCondition);
			return getNextState();
		}
		
		@Override
		public NextState showPlaceholder() {
			getFactory().showPlaceholder();
			return getNextState();
		}
		
		@Override
		public NextState showPlaceholder(Condition showPlaceholderCondition) {
			getFactory().showPlaceholder(showPlaceholderCondition);
			return getNextState();
		}
		
		@Override
		public NextState hidePlaceholder() {
			getFactory().hidePlaceholder();
			return getNextState();
		}
		
		@Override
		public NextState hidePlaceholder(Condition hidePlaceholderCondition) {
			getFactory().hidePlaceholder(hidePlaceholderCondition);
			return getNextState();
		}
		
		@Override
		public NextState when(final Condition condition) {
			getFactory().addConditionFactory(OneParameterConditionFactory.<IModel<T>>identity(condition));
			return getNextState();
		}
		
		@Override
		public NextState when(final Predicate<? super T> predicate) {
			getFactory().addConditionFactory(OneParameterConditionFactory.<T>predicate(predicate));
			return getNextState();
		}
		
		@Override
		public NextState whenPermission(final String permission) {
			getFactory().addConditionFactory(OneParameterConditionFactory.<T>permission(permission));
			return getNextState();
		}
		
		@Override
		public NextState whenPermission(final Permission permission) {
			getFactory().addConditionFactory(OneParameterConditionFactory.<T>permission(permission));
			return getNextState();
		}
		
		@Override
		public NextState withClass(String cssClass) {
			getFactory().addCssClass(cssClass);
			return getNextState();
		}
	}

	private class ActionColumnAddedLinkState extends ActionColumnAddedElementState<IActionColumnAddedLinkState<T, S>>
			implements IActionColumnAddedLinkState<T, S> {
		
		private final ActionColumnLinkFactory<T> factory;
		
		public ActionColumnAddedLinkState(ActionColumnLinkFactory<T> factory) {
			super();
			this.factory = Objects.requireNonNull(factory);
		}
		
		@Override
		public ActionColumnLinkFactory<T> getFactory() {
			return factory;
		}
		
		@Override
		protected IActionColumnAddedLinkState<T, S> getNextState() {
			return this;
		}

		@Override
		public IActionColumnAddedLinkState<T, S> hideIfInvalid() {
			getFactory().hideIfInvalid();
			return getNextState();
		}
	}

	private class ActionColumnAddedAjaxActionState extends ActionColumnAddedElementState<IActionColumnAddedAjaxActionState<T, S>>
			implements IActionColumnAddedAjaxActionState<T, S> {
		
		private final ActionColumnAjaxActionFactory<T> factory;
		
		public ActionColumnAddedAjaxActionState(ActionColumnAjaxActionFactory<T> factory) {
			super();
			this.factory = Objects.requireNonNull(factory);
		}
		
		@Override
		public ActionColumnAjaxActionFactory<T> getFactory() {
			return factory;
		}
		
		@Override
		protected IActionColumnAddedAjaxActionState<T, S> getNextState() {
			return this;
		}
	}

	private class ActionColumnAddedActionState extends ActionColumnAddedElementState<IActionColumnAddedActionState<T, S>>
			implements IActionColumnAddedActionState<T, S> {
		
		private final ActionColumnActionFactory<T> factory;
		
		public ActionColumnAddedActionState(ActionColumnActionFactory<T> factory) {
			super();
			this.factory = Objects.requireNonNull(factory);
		}
		
		@Override
		public ActionColumnActionFactory<T> getFactory() {
			return factory;
		}
		
		@Override
		protected IActionColumnAddedActionState<T, S> getNextState() {
			return this;
		}
		
	}

	private class ActionColumnAddedConfirmActionState extends ActionColumnAddedElementState<IActionColumnAddedConfirmActionState<T, S>>
			implements IActionColumnAddedConfirmActionState<T, S> {
		
		private final ActionColumnConfirmActionFactory<T> factory;
		
		public ActionColumnAddedConfirmActionState(ActionColumnConfirmActionFactory<T> factory) {
			super();
			this.factory = Objects.requireNonNull(factory);
		}
		
		@Override
		public ActionColumnConfirmActionFactory<T> getFactory() {
			return factory;
		}
		
		@Override
		protected IActionColumnAddedConfirmActionState<T, S> getNextState() {
			return this;
		}
	}

	@Override
	public IActionColumnAddedLinkState<T, S> addLink(BootstrapRenderer<? super T> renderer,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
		return addLabelledLink(renderer, mapper).hideLabel();
	}

	@Override
	public <C> IActionColumnAddedLinkState<T, S> addLink(BootstrapRenderer<? super T> renderer,
			AbstractCoreBinding<? super T, C> binding,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, C> mapper) {
		return addLink(renderer, new BindingOneParameterLinkDescriptorMapper<>(binding, mapper));
	}

	@Override
	public IActionColumnAddedLinkState<T, S> addLabelledLink(BootstrapRenderer<? super T> renderer,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
		ActionColumnLinkFactory<T> factory = new ActionColumnLinkFactory<>(renderer, mapper);
		factories.add(factory);
		return new ActionColumnAddedLinkState(factory);
	}

	@Override
	public <C> IActionColumnAddedLinkState<T, S> addLabelledLink(BootstrapRenderer<? super T> renderer,
			AbstractCoreBinding<? super T, C> binding,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, C> mapper) {
		return addLabelledLink(renderer, new BindingOneParameterLinkDescriptorMapper<>(binding, mapper));
	}

	@Override
	public IActionColumnAddedAjaxActionState<T, S> addAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAjaxAction<IModel<T>> action) {
		return addLabelledAction(renderer, action).hideLabel();
	}

	@Override
	public IActionColumnAddedAjaxActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAjaxAction<IModel<T>> action) {
		ActionColumnAjaxActionFactory<T> factory = new ActionColumnAjaxActionFactory<>(renderer, action);
		factories.add(factory);
		return new ActionColumnAddedAjaxActionState(factory);
	}

	@Override
	public IActionColumnAddedActionState<T, S> addAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAction<IModel<T>> action) {
		return addLabelledAction(renderer, action).hideLabel();
	}

	@Override
	public IActionColumnAddedActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAction<IModel<T>> action) {
		ActionColumnActionFactory<T> factory = new ActionColumnActionFactory<>(renderer, action);
		factories.add(factory);
		return new ActionColumnAddedActionState(factory);
	}

	@Override
	public ActionColumnConfirmActionBuilder<T, S> addConfirmAction(BootstrapRenderer<? super T> renderer) {
		return new ActionColumnConfirmActionBuilder<>(this, renderer);
	}

	public IActionColumnAddedConfirmActionState<T, S> addConfirmAction(BootstrapRenderer<? super T> renderer,
			IOneParameterComponentFactory<AjaxConfirmLink<T>, IModel<T>> ajaxConfirmLinkFactory) {
		ActionColumnConfirmActionFactory<T> factory = new ActionColumnConfirmActionFactory<>(renderer, ajaxConfirmLinkFactory);
		factories.add(factory);
		return new ActionColumnAddedConfirmActionState(factory);
	}

	@Override
	public IActionColumnBuildState<T, S> withClassOnElements(String cssClassOnElements) {
		cssClassesOnElements.add(cssClassOnElements);
		return this;
	}

	public String getCssClassOnElements() {
		return Joiner.on(" ").join(cssClassesOnElements);
	}

	@Override
	public IAddedCoreColumnState<T, S> end() {
		for (AbstractActionColumnElementFactory<T, ?> factory : factories) {
			factory.addCssClass(getCssClassOnElements());
		}
		return dataTableBuilder.addActionColumn(new CoreActionColumn<T, S>(headerLabelModel, factories));
	}

}
