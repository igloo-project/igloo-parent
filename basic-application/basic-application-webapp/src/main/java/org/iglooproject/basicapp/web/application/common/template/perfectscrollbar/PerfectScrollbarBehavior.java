package org.iglooproject.basicapp.web.application.common.template.perfectscrollbar;

import org.apache.commons.text.StringSubstitutor;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.iglooproject.spring.util.StringUtils;
import org.iglooproject.wicket.more.markup.html.template.js.perfectscrollbar.PerfectScrollbarJavaScriptResourceReference;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

public class PerfectScrollbarBehavior extends Behavior {

	private static final long serialVersionUID = 1L;

	private static final String JS_TEMPLATE = "new PerfectScrollbar('${selector}');";

	private final String selector;

	public PerfectScrollbarBehavior(String selector) {
		super();
		Preconditions.checkArgument(!StringUtils.isEmpty(selector));
		this.selector = selector;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(PerfectScrollbarJavaScriptResourceReference.get()));
		response.render(OnDomReadyHeaderItem.forScript(
				new StringSubstitutor(
						ImmutableMap.<String, Object>builder()
								.put("selector", selector)
								.build()
				)
						.replace(JS_TEMPLATE)
		));
	}

}
