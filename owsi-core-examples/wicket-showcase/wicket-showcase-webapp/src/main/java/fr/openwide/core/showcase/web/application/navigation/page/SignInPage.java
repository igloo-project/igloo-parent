package fr.openwide.core.showcase.web.application.navigation.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.core.IWiQueryPlugin;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import fr.openwide.core.showcase.web.application.ShowcaseSession;
import fr.openwide.core.showcase.web.application.util.template.styles.SignInLessCssResourceReference;
import fr.openwide.core.wicket.markup.html.util.css3pie.Css3PieHeadBehavior;
import fr.openwide.core.wicket.more.markup.html.CoreWebPage;
import fr.openwide.core.wicket.more.markup.html.feedback.GlobalFeedbackPanel;

public class SignInPage extends CoreWebPage implements IWiQueryPlugin {
	private static final long serialVersionUID = 5503959273448832421L;

	private static final String[] ROUNDED_CORNERS_CSS_CLASSES = new String[] {
			".login-box-outer", ".login-box-footer-outer",
			".field-inline .field-label", ".field-inline .form-label",
			".buttons a.button", "input" };

	private RequestCache requestCache = new HttpSessionRequestCache();

	public SignInPage() {
		super();
		
		add(new Css3PieHeadBehavior(ROUNDED_CORNERS_CSS_CLASSES));
		
		SavedRequest savedRequest = (SavedRequest) requestCache.getRequest(
				(HttpServletRequest) getRequest().getContainerRequest(),
				(HttpServletResponse) getResponse().getContainerResponse());
		if (savedRequest != null) {
			ShowcaseSession.get().registerRedirectUrl(
					savedRequest.getRedirectUrl());
		}
		
		add(new GlobalFeedbackPanel("feedback"));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
		response.renderCSSReference(SignInLessCssResourceReference.get());
	}
	
	@Override
	public JsStatement statement() {
		return null;
	}
}
