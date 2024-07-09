package basicapp.front.common.template.theme.advanced;

import org.apache.wicket.markup.html.panel.Panel;

import basicapp.front.BasicApplicationApplication;

public class SidebarNavbarPanel extends Panel {

	private static final long serialVersionUID = -3741272240940846720L;

	public SidebarNavbarPanel(String id) {
		super(id);
		
		add(
			BasicApplicationApplication.get().getHomePageLinkDescriptor()
				.link("home")
		);
	}

}
