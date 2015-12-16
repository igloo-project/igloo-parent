package fr.openwide.core.basicapp.web.application.administration.component;

import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.web.application.audit.component.AuditListPanel;
import fr.openwide.core.basicapp.web.application.audit.model.AuditDataProvider;
import fr.openwide.core.spring.property.service.IPropertyService;
import fr.openwide.core.wicket.markup.html.panel.GenericPanel;

public class UserAuditsPanel extends GenericPanel<User> {

	private static final long serialVersionUID = 1457366466253945552L;

	@SpringBean
	private IPropertyService propertyService;

	public UserAuditsPanel(String id, final IModel<? extends User> userModel) {
		super(id, userModel);
		
		add(new AuditListPanel("listPanel", new AuditDataProvider(userModel), propertyService.get(PORTFOLIO_ITEMS_PER_PAGE)));
	}
}
