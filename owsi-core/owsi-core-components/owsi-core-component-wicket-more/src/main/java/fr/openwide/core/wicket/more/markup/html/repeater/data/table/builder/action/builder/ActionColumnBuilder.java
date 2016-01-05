package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.builder;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.springframework.security.acls.model.Permission;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.ActionColumnActionFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory.ActionColumnAjaxActionFactory;
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

	private final List<ActionColumnElementBuilder<T, ?, ?>> builders = Lists.newArrayList();

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
		
		protected abstract ActionColumnElementBuilder<T, ?, ?> getElementBuilder();
		
		@Override
		public NextState showLabel() {
			getElementBuilder().showLabel();
			return getNextState();
		}
		
		@Override
		public NextState showLabel(Condition showLabelCondition) {
			getElementBuilder().showLabel(showLabelCondition);
			return getNextState();
		}
		
		@Override
		public NextState hideLabel() {
			getElementBuilder().hideLabel();
			return getNextState();
		}
		
		@Override
		public NextState hideLabel(Condition hideLabelCondition) {
			getElementBuilder().hideLabel(hideLabelCondition);
			return getNextState();
		}
		
		@Override
		public NextState showTooltip() {
			getElementBuilder().showTooltip();
			return getNextState();
		}
		
		@Override
		public NextState showTooltip(Condition showTooltipCondition) {
			getElementBuilder().showTooltip(showTooltipCondition);
			return getNextState();
		}
		
		@Override
		public NextState hideTooltip() {
			getElementBuilder().hideTooltip();
			return getNextState();
		}
		
		@Override
		public NextState hideTooltip(Condition hideTooltipCondition) {
			getElementBuilder().hideTooltip(hideTooltipCondition);
			return getNextState();
		}
		
		@Override
		public NextState showIcon() {
			getElementBuilder().showIcon();
			return getNextState();
		}
		
		@Override
		public NextState showIcon(Condition showIconCondition) {
			getElementBuilder().showIcon(showIconCondition);
			return getNextState();
		}
		
		@Override
		public NextState hideIcon() {
			getElementBuilder().hideIcon();
			return getNextState();
		}
		
		@Override
		public NextState hideIcon(Condition hideIconCondition) {
			getElementBuilder().hideIcon(hideIconCondition);
			return getNextState();
		}
		
		@Override
		public NextState showPlaceholder() {
			getElementBuilder().showPlaceholder();
			return getNextState();
		}
		
		@Override
		public NextState showPlaceholder(Condition showPlaceholderCondition) {
			getElementBuilder().showPlaceholder(showPlaceholderCondition);
			return getNextState();
		}
		
		@Override
		public NextState hidePlaceholder() {
			getElementBuilder().hidePlaceholder();
			return getNextState();
		}
		
		@Override
		public NextState hidePlaceholder(Condition hidePlaceholderCondition) {
			getElementBuilder().hidePlaceholder(hidePlaceholderCondition);
			return getNextState();
		}
		
		@Override
		public NextState when(final Condition condition) {
			getElementBuilder().addConditionFactory(OneParameterConditionFactory.<IModel<T>>identity(condition));
			return getNextState();
		}
		
		@Override
		public NextState when(final Predicate<? super T> predicate) {
			getElementBuilder().addConditionFactory(OneParameterConditionFactory.<T>predicate(predicate));
			return getNextState();
		}
		
		@Override
		public NextState whenPermission(final String permission) {
			getElementBuilder().addConditionFactory(OneParameterConditionFactory.<T>permission(permission));
			return getNextState();
		}
		
		@Override
		public NextState whenPermission(final Permission permission) {
			getElementBuilder().addConditionFactory(OneParameterConditionFactory.<T>permission(permission));
			return getNextState();
		}
		
		@Override
		public NextState withClass(String cssClass) {
			getElementBuilder().addCssClass(cssClass);
			return getNextState();
		}
	}

	private class ActionColumnAddedLinkState extends ActionColumnAddedElementState<IActionColumnAddedLinkState<T, S>>
			implements IActionColumnAddedLinkState<T, S> {
		
		private final ActionColumnLinkBuilder<T> elementBuilder;
		
		public ActionColumnAddedLinkState(ActionColumnLinkBuilder<T> elementBuilder) {
			super();
			this.elementBuilder = Objects.requireNonNull(elementBuilder);
		}
		
		@Override
		public ActionColumnLinkBuilder<T> getElementBuilder() {
			return elementBuilder;
		}
		
		@Override
		protected IActionColumnAddedLinkState<T, S> getNextState() {
			return this;
		}

		@Override
		public IActionColumnAddedLinkState<T, S> hideIfInvalid() {
			getElementBuilder().hideIfInvalid();
			return getNextState();
		}
	}

	private class ActionColumnAddedAjaxActionState extends ActionColumnAddedElementState<IActionColumnAddedAjaxActionState<T, S>>
			implements IActionColumnAddedAjaxActionState<T, S> {
		
		private final ActionColumnElementBuilder<T, ?, ?> elementBuilder;
		
		public ActionColumnAddedAjaxActionState(ActionColumnElementBuilder<T, ?, ?> elementBuilder) {
			super();
			this.elementBuilder = Objects.requireNonNull(elementBuilder);
		}
		
		@Override
		public ActionColumnElementBuilder<T, ?, ?> getElementBuilder() {
			return elementBuilder;
		}
		
		@Override
		protected IActionColumnAddedAjaxActionState<T, S> getNextState() {
			return this;
		}
	}

	private class ActionColumnAddedActionState extends ActionColumnAddedElementState<IActionColumnAddedActionState<T, S>>
			implements IActionColumnAddedActionState<T, S> {
		
		private final ActionColumnElementBuilder<T, ?, ?> elementBuilder;
		
		public ActionColumnAddedActionState(ActionColumnElementBuilder<T, ?, ?> elementBuilder) {
			super();
			this.elementBuilder = Objects.requireNonNull(elementBuilder);
		}
		
		@Override
		public ActionColumnElementBuilder<T, ?, ?> getElementBuilder() {
			return elementBuilder;
		}
		
		@Override
		protected IActionColumnAddedActionState<T, S> getNextState() {
			return this;
		}
	}

	private class ActionColumnAddedConfirmActionState extends ActionColumnAddedElementState<IActionColumnAddedConfirmActionState<T, S>>
			implements IActionColumnAddedConfirmActionState<T, S> {
		
		private final ActionColumnElementBuilder<T, ?, ?> elementBuilder;
		
		public ActionColumnAddedConfirmActionState(ActionColumnElementBuilder<T, ?, ?> elementBuilder) {
			super();
			this.elementBuilder = Objects.requireNonNull(elementBuilder);
		}
		
		@Override
		public ActionColumnElementBuilder<T, ?, ?> getElementBuilder() {
			return elementBuilder;
		}
		
		@Override
		protected IActionColumnAddedConfirmActionState<T, S> getNextState() {
			return this;
		}
	}

	@Override
	public IActionColumnAddedLinkState<T, S> addLink(BootstrapRenderer<? super T> renderer,
			IOneParameterLinkDescriptorMapper<? extends ILinkGenerator, T> mapper) {
		ActionColumnLinkBuilder<T> factory = new ActionColumnLinkBuilder<>(renderer, mapper);
		builders.add(factory);
		return new ActionColumnAddedLinkState(factory);
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
		return addLink(renderer, mapper).showLabel();
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
		ActionColumnElementBuilder<T, ?, ?> factory =
				new ActionColumnElementBuilder<>(renderer, new ActionColumnAjaxActionFactory<>(action));
		builders.add(factory);
		return new ActionColumnAddedAjaxActionState(factory);
	}

	@Override
	public IActionColumnAddedAjaxActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAjaxAction<IModel<T>> action) {
		return addAction(renderer, action).showLabel();
	}

	@Override
	public IActionColumnAddedActionState<T, S> addAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAction<IModel<T>> action) {
		ActionColumnElementBuilder<T, ?, ?> factory =
				new ActionColumnElementBuilder<>(renderer, new ActionColumnActionFactory<>(action));
		builders.add(factory);
		return new ActionColumnAddedActionState(factory);
	}

	@Override
	public IActionColumnAddedActionState<T, S> addLabelledAction(BootstrapRenderer<? super T> renderer,
			IOneParameterAction<IModel<T>> action) {
		return addAction(renderer, action).showLabel();
	}

	@Override
	public ActionColumnConfirmActionBuilder<T, S> addConfirmAction(BootstrapRenderer<? super T> renderer) {
		return new ActionColumnConfirmActionBuilder<>(this, renderer);
	}

	public IActionColumnAddedConfirmActionState<T, S> addConfirmAction(BootstrapRenderer<? super T> renderer,
			IOneParameterComponentFactory<AjaxConfirmLink<T>, IModel<T>> ajaxConfirmLinkFactory) {
		ActionColumnElementBuilder<T, ?, ?> factory =new ActionColumnElementBuilder<>(renderer, ajaxConfirmLinkFactory);
		builders.add(factory);
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
		for (ActionColumnElementBuilder<T, ?, ?> builder : builders) {
			builder.addCssClass(getCssClassOnElements());
		}
		return dataTableBuilder.addActionColumn(new CoreActionColumn<T, S>(headerLabelModel, builders));
	}

}
