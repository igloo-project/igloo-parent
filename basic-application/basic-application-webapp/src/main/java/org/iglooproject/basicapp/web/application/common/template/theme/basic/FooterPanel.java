package org.iglooproject.basicapp.web.application.common.template.theme.basic;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.iglooproject.basicapp.core.property.BasicApplicationCorePropertyIds;
import org.iglooproject.basicapp.web.application.common.template.theme.common.ChangeApplicationThemeAjaxLink;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.wicket.api.condition.Condition;
import org.iglooproject.wicket.markup.html.basic.CoreLabel;
import org.iglooproject.wicket.more.model.ApplicationPropertyModel;
import org.iglooproject.wicket.more.rendering.Renderer;
import org.iglooproject.wicket.more.util.DatePattern;

public class FooterPanel extends Panel {

	private static final long serialVersionUID = 7635635641028580879L;

	public FooterPanel(String id) {
		super(id);
		
		add(
			new CoreLabel(
				"version",
				new StringResourceModel("common.version")
					.setParameters(
						ApplicationPropertyModel.of(SpringPropertyIds.VERSION),
						Condition.modelNotNull(ApplicationPropertyModel.of(BasicApplicationCorePropertyIds.BUILD_DATE))
							.then(Renderer.fromDatePattern(DatePattern.SHORT_DATE).asModel(ApplicationPropertyModel.of(BasicApplicationCorePropertyIds.BUILD_DATE)))
							.otherwise(new ResourceModel("common.version.date.placeholder"))
					)
			)
				.add(new AttributeModifier(
					"title",
					new StringResourceModel("common.version.full")
						.setParameters(
							ApplicationPropertyModel.of(SpringPropertyIds.VERSION),
							ApplicationPropertyModel.of(SpringPropertyIds.IGLOO_VERSION)
						)
				)),
			
			new CoreLabel(
				"sha",
				Condition.hasText(ApplicationPropertyModel.of(BasicApplicationCorePropertyIds.BUILD_SHA))
					.then(ApplicationPropertyModel.of(BasicApplicationCorePropertyIds.BUILD_SHA))
					.otherwise(new ResourceModel("common.build.sha.placeholder"))
			)
				.hideIfEmpty()
				.add(Condition.role(CoreAuthorityConstants.ROLE_ADMIN).thenShow())
		);
		
		add(
			new ChangeApplicationThemeAjaxLink("changeTheme")
		);
	}

}
