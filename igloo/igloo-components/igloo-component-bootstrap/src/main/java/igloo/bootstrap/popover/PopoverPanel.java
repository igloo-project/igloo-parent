package igloo.bootstrap.popover;

import static igloo.bootstrap.woption.WOptionHelpers.asModel;
import static igloo.wicket.condition.Condition.anyChildVisible;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.iglooproject.functional.Predicates2;

import igloo.bootstrap.woption.IWOptionComponentFactory;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.component.EnclosureContainer;
import igloo.wicket.condition.Condition;

public class PopoverPanel extends Panel {
	private static final long serialVersionUID = -8520209335567737130L;

	private final IModel<Boolean> showLabelModel = Model.of(Boolean.TRUE);

	private final IModel<String> iconCssClassModel = Model.of();

	private final IModel<String> linkCssClassModel = Model.of();

	public PopoverPanel(String wicketId, IModel<String> model, Popover popover) {
		super(wicketId, model);
		WebMarkupContainer link = new WebMarkupContainer("link");
		
		Component titleComponent;
		if (popover.title() instanceof IWOptionComponentFactory) {
			titleComponent = ((IWOptionComponentFactory) popover.title()).createComponent("titleComponent");
		} else {
			titleComponent = new EmptyPanel("titleComponent");
		}
		Component contentComponent;
		if (popover.title() instanceof IWOptionComponentFactory) {
			contentComponent = ((IWOptionComponentFactory) popover.content()).createComponent("contentComponent");
		} else {
			contentComponent = new EmptyPanel("contentComponent");
		}
		
		add(
			titleComponent,
			contentComponent,
			
			link
				.add(
					new EnclosureContainer("icon").condition(Condition.predicate(iconCssClassModel, Predicates2.hasText()))
						.add(new AttributeModifier("class", asModel(popover.linkCssClass()))),
					new CoreLabel("label", getDefaultModel())
						.hideIfEmpty()
						.add(Condition.predicate(asModel(popover.showLabel()), Predicates2.isTrue()).thenShow())
				)
				.add(
					anyChildVisible(link).thenShowInternal(),
					new AttributeModifier("class", linkCssClassModel),
					new PopoverBehavior(popover)
				)
		);
	}

	public PopoverPanel hideLabel() {
		showLabelModel.setObject(false);
		return this;
	}

	public PopoverPanel iconCssClass(String iconCssClass) {
		iconCssClassModel.setObject(iconCssClass);
		return this;
	}

	public PopoverPanel linkCssClass(String linkCssClass) {
		linkCssClassModel.setObject(linkCssClass);
		return this;
	}

}
