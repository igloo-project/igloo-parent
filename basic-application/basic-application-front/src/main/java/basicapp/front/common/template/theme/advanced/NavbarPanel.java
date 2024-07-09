package basicapp.front.common.template.theme.advanced;

import java.util.function.Supplier;

import org.apache.wicket.markup.html.WebPage;

import basicapp.front.BasicApplicationApplication;
import basicapp.front.common.template.theme.common.AbstractNavbarPanel;

public class NavbarPanel extends AbstractNavbarPanel {

	private static final long serialVersionUID = 3273009208331893767L;

	public NavbarPanel(
		String id,
		Supplier<Class<? extends WebPage>> firstMenuPageSupplier
	) {
		super(id);
		
		add(
			BasicApplicationApplication.get().getHomePageLinkDescriptor()
				.link("home")
		);
	}

}
