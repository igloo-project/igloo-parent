package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.action.factory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.IModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.impossibl.postgres.utils.guava.Joiner;

import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.bootstrap.label.renderer.BootstrapLabelRenderer;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterConditionFactory;
import fr.openwide.core.wicket.more.util.model.Detachables;


public abstract class AbstractActionColumnElementFactory<T, F extends AbstractActionColumnElementFactory<T, ?>> implements IOneParameterComponentFactory<AbstractLink, IModel<T>> {

	private static final long serialVersionUID = 8791565179874571105L;

	private final BootstrapLabelRenderer<? super T> renderer;

	private Condition showLabelCondition = Condition.alwaysTrue();

	private Condition showTooltipCondition = Condition.alwaysTrue();

	private Condition showIconCondition = Condition.alwaysTrue();

	private Condition showPlaceholderCondition = Condition.alwaysTrue();

	private final List<IOneParameterConditionFactory<IModel<T>>> conditionFactories = Lists.newArrayList();

	private final Set<String> cssClasses = Sets.newHashSet();

	public AbstractActionColumnElementFactory(BootstrapLabelRenderer<? super T> renderer) {
		this.renderer = renderer;
	}

	public BootstrapLabelRenderer<? super T> getRenderer() {
		return renderer;
	}

	public F showLabel() {
		showLabelCondition = Condition.alwaysTrue();
		return thisAsF();
	}

	public F showLabel(Condition showLabelCondition) {
		this.showLabelCondition = Objects.requireNonNull(showLabelCondition);
		return thisAsF();
	}

	public F hideLabel() {
		showLabelCondition = Condition.alwaysFalse();
		return thisAsF();
	}

	public F hideLabel(Condition hideLabelCondition) {
		return showLabel(Objects.requireNonNull(hideLabelCondition).negate());
	}

	public Condition getShowLabelCondition() {
		return showLabelCondition;
	}

	public F showTooltip() {
		showTooltipCondition = Condition.alwaysTrue();
		return thisAsF();
	}

	public F showTooltip(Condition showTooltipCondition) {
		this.showTooltipCondition = Objects.requireNonNull(showTooltipCondition);
		return thisAsF();
	}

	public F hideTooltip() {
		showTooltipCondition = Condition.alwaysFalse();
		return thisAsF();
	}

	public F hideTooltip(Condition hideTooltipCondition) {
		return showTooltip(Objects.requireNonNull(hideTooltipCondition).negate());
	}

	public Condition getShowTooltipCondition() {
		return showTooltipCondition;
	}

	public F showIcon() {
		showIconCondition = Condition.alwaysTrue();
		return thisAsF();
	}

	public F showIcon(Condition showIconCondition) {
		this.showIconCondition = Objects.requireNonNull(showIconCondition);
		return thisAsF();
	}

	public F hideIcon() {
		showIconCondition = Condition.alwaysFalse();
		return thisAsF();
	}

	public F hideIcon(Condition hideIconCondition) {
		return showIcon(Objects.requireNonNull(hideIconCondition).negate());
	}

	public Condition getShowIconCondition() {
		return showIconCondition;
	}

	public F showPlaceholder() {
		showPlaceholderCondition = Condition.alwaysTrue();
		return thisAsF();
	}

	public F showPlaceholder(Condition showPlaceholderCondition) {
		this.showPlaceholderCondition = Objects.requireNonNull(showPlaceholderCondition);
		return thisAsF();
	}

	public F hidePlaceholder() {
		showPlaceholderCondition = Condition.alwaysFalse();
		return thisAsF();
	}

	public F hidePlaceholder(Condition hidePlaceholderCondition) {
		return showPlaceholder(Objects.requireNonNull(hidePlaceholderCondition).negate());
	}

	public Condition getShowPlaceholderCondition() {
		return showPlaceholderCondition;
	}

	public List<IOneParameterConditionFactory<IModel<T>>> getConditionFactories() {
		return Collections.unmodifiableList(conditionFactories);
	}

	public F addConditionFactory(IOneParameterConditionFactory<IModel<T>> conditionFactory) {
		conditionFactories.add(Objects.requireNonNull(conditionFactory));
		return thisAsF();
	}

	public String getCssClass() {
		return Joiner.on(" ").join(cssClasses);
	}

	public F addCssClass(String cssClass) {
		cssClasses.add(cssClass);
		return thisAsF();
	}

	public abstract F thisAsF();

	@Override
	public void detach() {
		Detachables.detach(showLabelCondition, showTooltipCondition, showIconCondition, showPlaceholderCondition);
		Detachables.detach(conditionFactories);
	}
}
