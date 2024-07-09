package basicapp.front.common.template.theme.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.spring.property.service.IPropertyService;

import basicapp.front.common.template.theme.BasicApplicationApplicationTheme;
import basicapp.front.navigation.page.HomePage;
import basicapp.front.property.BasicApplicationWebappPropertyIds;

public class ChangeApplicationThemeAjaxLink extends AjaxLink<Void> {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private IPropertyService propertyService;

	public ChangeApplicationThemeAjaxLink(String id) {
		super(id);
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		try {
			BasicApplicationApplicationTheme applicationTheme = propertyService.get(BasicApplicationWebappPropertyIds.APPLICATION_THEME);
			
			if (applicationTheme == null) {
				return;
			}
			
			propertyService.set(BasicApplicationWebappPropertyIds.APPLICATION_THEME, applicationTheme.next());
			
			throw HomePage.linkDescriptor().newRestartResponseException();
		} catch (Exception e) {
			throw new IllegalStateException("Error on updating application theme.", e);
		}
	}

}
