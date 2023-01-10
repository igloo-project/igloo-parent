package igloo.bootstrap.popover;

import static igloo.bootstrap.woption.WOptionHelpers.asModel;
import static igloo.wicket.condition.Condition.anyChildVisible;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.iglooproject.functional.Predicates2;

import igloo.bootstrap.js.statement.IJsStatement;
import igloo.bootstrap.woption.WDetachablesVisitor;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;
import igloo.wicket.factory.IComponentFactory;

public class PopoverPanel extends Panel {

	private static final long serialVersionUID = -8520209335567737130L;

	private final Popover popover;

	public PopoverPanel(String wicketId, Popover popover) {
		super(wicketId);
		this.popover = popover;
		
		WebMarkupContainer link = new WebMarkupContainer("link");
		
		Component titleComponent;
		IJsStatement title = popover.js().title();
		if (title instanceof IComponentFactory) {
			titleComponent = ((IComponentFactory<?>) title).create("titleComponent");
		} else {
			titleComponent = new EmptyPanel("titleComponent");
		}
		
		Component contentComponent;
		IJsStatement content = popover.js().content();
		if (content instanceof IComponentFactory) {
			contentComponent = ((IComponentFactory<?>) content).create("contentComponent");
		} else {
			contentComponent = new EmptyPanel("contentComponent");
		}
		
		add(
			titleComponent,
			contentComponent
		);
		
		add(
			link
				.add(
					new EnclosureContainer("icon").condition(Condition.predicate(asModel(popover.iconCssClass()), Predicates2.hasText()))
						.add(new AttributeModifier("class", asModel(popover.iconCssClass()))),
					new CoreLabel("label", asModel(popover.label()))
						.hideIfEmpty()
						.add(Condition.predicate(asModel(popover.showLabel()), Predicates2.isTrue()).thenShow())
				)
				.add(
					anyChildVisible(link).thenShowInternal(),
					new AttributeModifier("class", asModel(popover.linkCssClass())),
					new PopoverBehavior(popover.js())
				)
		);
		
		// Hide the whole popover panel if the content component is not visible.
		add(
				Condition.componentVisible(contentComponent).thenShowInternal()
		);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		new WDetachablesVisitor().visitAndDetach(popover);
	}

}
