package basicapp.front.referencedata.page;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;

import basicapp.front.common.component.FeatureNotYetAvailablePanel;
import basicapp.front.common.component.NavTabsPanel;
import basicapp.front.referencedata.component.CityListPanel;
import basicapp.front.referencedata.template.ReferenceDataTemplate;
import igloo.wicket.condition.Condition;
import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.iglooproject.wicket.more.link.descriptor.IPageLinkDescriptor;
import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;

public class ReferenceDataPage extends ReferenceDataTemplate {

  private static final long serialVersionUID = -4381694964311714573L;

  public static final IPageLinkDescriptor linkDescriptor() {
    return LinkDescriptorBuilder.start()
        .validator(Condition.permission(GLOBAL_REFERENCE_DATA_READ))
        .page(ReferenceDataPage.class);
  }

  public ReferenceDataPage(PageParameters parameters) {
    super(parameters);

    add(
        new NavTabsPanel("tabs")
            .add(
                new NavTabsPanel.SimpleTabFactory("city", "business.city") {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component createContent(String wicketId) {
                    return new CityListPanel(wicketId);
                  }
                })
            .add(
                new NavTabsPanel.SimpleTabFactory("reference-data-2", Model.of("Item #2")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component createContent(String wicketId) {
                    return new FeatureNotYetAvailablePanel(wicketId);
                  }
                })
            .add(
                new NavTabsPanel.SimpleTabFactory("reference-data-3", Model.of("Item #3")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component createContent(String wicketId) {
                    return new FeatureNotYetAvailablePanel(wicketId);
                  }
                })
            .add(
                new NavTabsPanel.SimpleTabFactory("reference-data-4", Model.of("Item #4")) {
                  private static final long serialVersionUID = 1L;

                  @Override
                  public Component createContent(String wicketId) {
                    return new FeatureNotYetAvailablePanel(wicketId);
                  }
                }));
  }
}
