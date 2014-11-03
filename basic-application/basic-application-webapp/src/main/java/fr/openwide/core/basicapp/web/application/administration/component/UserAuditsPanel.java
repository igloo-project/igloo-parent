package fr.openwide.core.basicapp.web.application.administration.component;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.config.application.BasicApplicationConfigurer;
import fr.openwide.core.basicapp.web.application.audit.component.AuditListPanel;
import fr.openwide.core.basicapp.web.application.audit.model.AuditDataProvider;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;

public class UserAuditsPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 1457366466253945552L;

	@SpringBean
	private BasicApplicationConfigurer configurer;

	public UserAuditsPanel(String id, final IModel<? extends User> userModel) {
		super(id, userModel);
		
		add(new AuditListPanel("listPanel", new AuditDataProvider(userModel), configurer.getPortfolioItemsPerPage()));
	}
}
