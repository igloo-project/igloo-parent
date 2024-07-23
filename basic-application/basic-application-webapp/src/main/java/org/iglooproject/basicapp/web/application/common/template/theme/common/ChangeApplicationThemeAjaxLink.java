package org.iglooproject.basicapp.web.application.common.template.theme.common;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.iglooproject.basicapp.web.application.common.template.theme.BasicApplicationApplicationTheme;
import org.iglooproject.basicapp.web.application.navigation.page.HomePage;
import org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;

public class ChangeApplicationThemeAjaxLink extends AjaxLink<Void> {

  private static final long serialVersionUID = 1L;

  @SpringBean private IPropertyService propertyService;

  public ChangeApplicationThemeAjaxLink(String id) {
    super(id);
  }

  @Override
  public void onClick(AjaxRequestTarget target) {
    try {
      BasicApplicationApplicationTheme applicationTheme =
          propertyService.get(BasicApplicationWebappPropertyIds.APPLICATION_THEME);

      if (applicationTheme == null) {
        return;
      }

      propertyService.set(
          BasicApplicationWebappPropertyIds.APPLICATION_THEME, applicationTheme.next());

      throw HomePage.linkDescriptor().newRestartResponseException();
    } catch (Exception e) {
      throw new IllegalStateException("Error on updating application theme.", e);
    }
  }
}
