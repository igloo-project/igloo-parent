package basicapp.front.referencedata.template;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;

import basicapp.front.common.template.MainTemplate;
import basicapp.front.referencedata.page.ReferenceDataPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.markup.html.template.model.BreadCrumbElement;
import org.iglooproject.wicket.more.security.authorization.AuthorizeInstantiationIfPermission;

@AuthorizeInstantiationIfPermission(permissions = GLOBAL_REFERENCE_DATA_READ)
public abstract class ReferenceDataTemplate extends MainTemplate {

  private static final long serialVersionUID = -5226976873952135450L;

  protected ReferenceDataTemplate(PageParameters parameters) {
    super(parameters);

    addBreadCrumbElement(
        new BreadCrumbElement(
            new ResourceModel("navigation.referenceData"), ReferenceDataPage.linkDescriptor()));
  }

  @Override
  protected Class<? extends WebPage> getFirstMenuPage() {
    return ReferenceDataPage.class;
  }
}
