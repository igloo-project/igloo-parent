package basicapp.front.common.template.theme.advanced;

import basicapp.back.property.BasicApplicationBackPropertyIds;
import basicapp.back.util.time.DateTimePattern;
import igloo.wicket.component.CoreLabel;
import igloo.wicket.condition.Condition;
import igloo.wicket.renderer.Renderer;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;

public class SidebarFooterPanel extends Panel {

  private static final long serialVersionUID = 5860908201606549343L;

  public SidebarFooterPanel(String id) {
    super(id);

    add(
        new CoreLabel(
                "version",
                new StringResourceModel("common.application.version")
                    .setParameters(
                        ApplicationPropertyModel.of(SpringPropertyIds.VERSION),
                        Condition.modelNotNull(
                                ApplicationPropertyModel.of(
                                    BasicApplicationBackPropertyIds.BUILD_DATE))
                            .then(
                                Renderer.fromDateTimePattern(DateTimePattern.SHORT_DATE)
                                    .asModel(
                                        ApplicationPropertyModel.of(
                                            BasicApplicationBackPropertyIds.BUILD_DATE)))
                            .otherwise(
                                new ResourceModel("common.application.version.date.placeholder"))))
            .add(
                new AttributeModifier(
                    "title",
                    new StringResourceModel("common.application.version.full")
                        .setParameters(
                            ApplicationPropertyModel.of(SpringPropertyIds.VERSION),
                            ApplicationPropertyModel.of(SpringPropertyIds.IGLOO_VERSION)))),
        new CoreLabel(
                "sha",
                Condition.hasText(
                        ApplicationPropertyModel.of(BasicApplicationBackPropertyIds.BUILD_SHA))
                    .then(ApplicationPropertyModel.of(BasicApplicationBackPropertyIds.BUILD_SHA))
                    .otherwise(new ResourceModel("common.application.build.sha.placeholder")))
            .hideIfEmpty()
            .add(Condition.role(CoreAuthorityConstants.ROLE_ADMIN).thenShow()));
  }
}
