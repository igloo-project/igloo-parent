package fr.openwide.core.basicapp.web.application.common.template;

import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

import fr.openwide.core.basicapp.web.application.BasicApplicationSession;
import fr.openwide.core.basicapp.web.application.common.template.styles.StylesLessCssResourceReference;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.markup.html.feedback.AnimatedGlobalFeedbackPanel;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltip;
import fr.openwide.core.wicket.more.markup.html.template.js.jquery.plugins.bootstrap.tooltip.BootstrapTooltipDocumentBehavior;
import fr.openwide.core.wicket.more.markup.html.template.model.BreadCrumbElement;

public abstract class MainTemplate extends CoreWebPage {

	private static final long serialVersionUID = -1312989780696228848L;

	private static final String META_TITLE_SEPARATOR = " ‹ ";

	private List<BreadCrumbElement> pageTitleElements = Lists.newArrayList();

	public MainTemplate(PageParameters parameters) {
		super(parameters);

		MarkupContainer htmlRootElement = new TransparentWebMarkupContainer("htmlRootElement");
		htmlRootElement.add(AttributeAppender.append("lang", BasicApplicationSession.get().getLocale().getLanguage()));
		add(htmlRootElement);

		add(new AnimatedGlobalFeedbackPanel("animatedGlobalFeedbackPanel"));

		add(new Label("headPageTitle", getHeadPageTitleModel()));

		// JavaScripts
		add(new BootstrapTooltipDocumentBehavior(getBootstrapTooltip()));
	}

	protected void addHeadPageTitleElement(BreadCrumbElement pageTitleElement) {
		pageTitleElements.add(0, pageTitleElement);
	}

	protected IModel<String> getHeadPageTitleModel() {
		return new PropertyModel<String>(this, "headPageTitle");
	}

	public String getHeadPageTitle() {
		StringBuilder sb = new StringBuilder();

		for (BreadCrumbElement pageTitleElement : pageTitleElements) {
			IModel<String> titleElementModel = pageTitleElement.getLabelModel();
			if (titleElementModel instanceof IComponentAssignedModel) {
				titleElementModel = wrap(titleElementModel);
			}
			sb.append(titleElementModel.getObject());
			sb.append(META_TITLE_SEPARATOR);
		}

		sb.append(getLocalizer().getString("common.rootPageTitle", this));

		return sb.toString();
	}

	public static BootstrapTooltip getBootstrapTooltip() {
		BootstrapTooltip bootstrapTooltip = new BootstrapTooltip();
		bootstrapTooltip.setSelector("[title],[data-original-title]");
		bootstrapTooltip.setAnimation(true);
		bootstrapTooltip.setPlacement(BootstrapTooltip.Placement.BOTTOM);
		return bootstrapTooltip;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		response.renderCSSReference(StylesLessCssResourceReference.get());
	}
}