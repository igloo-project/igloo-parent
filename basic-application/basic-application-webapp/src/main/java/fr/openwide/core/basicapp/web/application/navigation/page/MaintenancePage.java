package fr.openwide.core.basicapp.web.application.navigation.page;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;

import fr.openwide.core.basicapp.web.application.common.template.styles.StylesLessCssResourceReference;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;

public class MaintenancePage extends CoreWebPage {

	private static final long serialVersionUID = 7371109597310862894L;

	public MaintenancePage() {
		super();
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(StylesLessCssResourceReference.get()));
	}

}
