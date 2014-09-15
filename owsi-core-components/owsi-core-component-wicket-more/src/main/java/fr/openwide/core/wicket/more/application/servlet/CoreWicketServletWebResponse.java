package fr.openwide.core.wicket.more.application.servlet;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.UrlRenderer;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.lang.Args;

/**
 * Fix temporaire du problème d'encodage des ancres des Url par Wicket.
 */
@Deprecated
public class CoreWicketServletWebResponse extends ServletWebResponse {

	private final HttpServletResponse httpServletResponse;
	private final ServletWebRequest webRequest;

	public CoreWicketServletWebResponse(ServletWebRequest webRequest, HttpServletResponse httpServletResponse) {
		super(webRequest, httpServletResponse);
		this.webRequest = webRequest;
		this.httpServletResponse = httpServletResponse;
	}

	private UrlRenderer getUrlRenderer()
	{
		RequestCycle requestCycle = RequestCycle.get();
		if (requestCycle == null)
		{
			return new UrlRenderer(webRequest);
		}
		return requestCycle.getUrlRenderer();
	}

	@Override
	public String encodeURL(CharSequence url) {
		Args.notNull(url, "url");

		String urlStr = url.toString();
		int anchorAt = urlStr.indexOf("#");
		
		String urlWithoutAnchor;
		String anchor;
		
		if (anchorAt == -1) {
			urlWithoutAnchor = urlStr;
			anchor = "";
		} else {
			urlWithoutAnchor = urlStr.substring(0, anchorAt);
			anchor = urlStr.substring(anchorAt + 1);
		}

		UrlRenderer urlRenderer = getUrlRenderer();

		Url originalUrl = Url.parse(urlWithoutAnchor);

		/*
		  WICKET-4645 - always pass absolute url to the web container for encoding
		  because when REDIRECT_TO_BUFFER is in use Wicket may render PageB when
		  PageA is actually the requested one and the web container cannot resolve
		  the base url properly
		 */
		String fullUrl = urlRenderer.renderFullUrl(originalUrl);
		String encodedFullUrl = httpServletResponse.encodeURL(fullUrl);

		final String encodedUrl;
		if (originalUrl.isFull())
		{
			encodedUrl = encodedFullUrl;
		}
		else
		{
			if (fullUrl.equals(encodedFullUrl))
			{
				// no encoding happened so just reuse the original url
				encodedUrl = urlWithoutAnchor.toString();
			}
			else
			{
				// get the relative url with the jsessionid encoded in it
				Url _encoded = Url.parse(encodedFullUrl);
				encodedUrl = urlRenderer.renderRelativeUrl(_encoded);
			}
		}
		
		return encodedUrl + anchor;
	}
	
	@Override
	public String encodeRedirectURL(CharSequence url) {
		Args.notNull(url, "url");

		String urlStr = url.toString();
		int anchorAt = urlStr.indexOf("#");
		
		String urlWithoutAnchor;
		String anchor;
		
		if (anchorAt == -1) {
			urlWithoutAnchor = urlStr;
			anchor = "";
		} else {
			urlWithoutAnchor = urlStr.substring(0, anchorAt);
			anchor = urlStr.substring(anchorAt);
		}
		
		Url originalUrl = Url.parse(urlWithoutAnchor);
		
		UrlRenderer urlRenderer = getUrlRenderer();

		/*
		 * WICKET-4645 - always pass absolute url to the web container for encoding because when
		 * REDIRECT_TO_BUFFER is in use Wicket may render PageB when PageA is actually the requested
		 * one and the web container cannot resolve the base url properly
		 */
		String fullUrl = urlRenderer.renderFullUrl(originalUrl);
		String encodedFullUrl = httpServletResponse.encodeRedirectURL(fullUrl);

		final String encodedUrl;
		if (originalUrl.isFull())
		{
			encodedUrl = encodedFullUrl;
		}
		else
		{
			if (fullUrl.equals(encodedFullUrl))
			{
				// no encoding happened so just reuse the original url
				encodedUrl = urlWithoutAnchor.toString();
			}
			else
			{
				// get the relative url with the jsessionid encoded in it
				Url _encoded = Url.parse(encodedFullUrl);
				encodedUrl = urlRenderer.renderRelativeUrl(_encoded);
			}
		}
		return encodedUrl + anchor;
	}
}
