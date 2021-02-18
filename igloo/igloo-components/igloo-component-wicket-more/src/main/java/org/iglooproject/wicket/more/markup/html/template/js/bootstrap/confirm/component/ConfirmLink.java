package org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.component;

import java.util.List;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.IBootstrapConfirmModule;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.behavior.ConfirmContentBehavior;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.fluid.IConfirmLinkBuilderStepStart;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.statement.BootstrapConfirmEvent;
import org.iglooproject.wicket.more.markup.html.template.js.bootstrap.confirm.statement.BootstrapConfirmStatement;
import org.wicketstuff.wiquery.core.events.Event;
import org.wicketstuff.wiquery.core.javascript.JsScope;
import org.wicketstuff.wiquery.core.javascript.JsScopeEvent;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

import com.google.common.collect.Lists;

public abstract class ConfirmLink<O> extends Link<O> {

	private static final long serialVersionUID = -4124927130129944090L;

	@SpringBean(required = false)
	private List<IBootstrapConfirmModule> modules = Lists.newArrayList();

	public static <O> IConfirmLinkBuilderStepStart<ConfirmLink<O>, O> build() {
		return new ConfirmLinkBuilder<>();
	}

	protected ConfirmLink(String id, IModel<O> model, IModel<String> titleModel, IModel<String> textModel,
			IModel<String> yesLabelModel, IModel<String> noLabelModel, IModel<String> yesIconModel,
			IModel<String> noIconModel, IModel<String> yesButtonModel, IModel<String> noButtonModel,
			IModel<String> cssClassNamesModel, boolean textNoEscape) {
		super(id, model);
		setOutputMarkupId(true);
		add(new ConfirmContentBehavior(titleModel, textModel, yesLabelModel, noLabelModel, yesIconModel, noIconModel,
				yesButtonModel, noButtonModel, cssClassNamesModel, textNoEscape));
	}

	@Override
	protected CharSequence getOnClickScript(CharSequence url) {
		return BootstrapConfirmStatement.confirm(ConfirmLink.this).append("return false;").render();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		modules.forEach(module -> module.renderHead(this, response));
		
		Event confirmEvent = new Event(BootstrapConfirmEvent.CONFIRM) {
			private static final long serialVersionUID = 6466300052232971891L;
			
			@Override
			public JsScope callback() {
				return JsScopeEvent.quickScope(
						new JsStatement()
							.append("window.location = " + JsUtils.quotes(getURL(), true)).append(";")
							.append("event.preventDefault();")
				);
			}
		};
		response.render(OnDomReadyHeaderItem.forScript(new JsStatement().$(this).chain(confirmEvent).render(true)));
	}

}
