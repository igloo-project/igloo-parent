package org.iglooproject.wicket.download;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;

import org.apache.wicket.Component;
import org.apache.wicket.IRequestListener;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.iglooproject.wicket.model.Detachables;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.core.javascript.JsUtils;

/**
 * @author Sven Meier
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 * @author Jordi Deu-Pons (jordi@jordeu.net)
 * @author Renaud Joly
 * @author Florian Lacreuse
 * @author Yoann Rodière
 * @see <a href="https://cwiki.apache.org/confluence/display/WICKET/AJAX+update+and+file+download+in+one+blow">AJAX update and file download in one blow</a>
 */
public abstract class AbstractDeferredDownloadBehavior extends Behavior {

	private static final long serialVersionUID = -4484163101766083913L;
	
	private Component component;

	protected final IModel<File> tempFileModel;
	
	private final boolean addAntiCache;
	
	private final ResourceDownloadBehavior resourceDownloadBehavior;
	
	public AbstractDeferredDownloadBehavior(IModel<File> tempFileModel) {
		this(tempFileModel, true);
	}
	
	public AbstractDeferredDownloadBehavior(IModel<File> tempFileModel, boolean addAntiCache) {
		super();
		this.tempFileModel = checkNotNull(tempFileModel);
		this.addAntiCache = addAntiCache;
		this.resourceDownloadBehavior = new ResourceDownloadBehavior();
	}
	
	
	private class ResourceDownloadBehavior extends Behavior implements IRequestListener {
		private static final long serialVersionUID = -7831306343846345227L;
		
		@Override
		public void onRequest() {
			ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(getResourceStream(), getFileDisplayName());
			handler.setContentDisposition(ContentDisposition.ATTACHMENT);
			getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
		}
	}
	
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		Page page = component.getPage();
		if (!page.getBehaviors().contains(resourceDownloadBehavior)) {
			page.add(resourceDownloadBehavior);
		}
	}
	
	@Override
	public final void bind(Component component) {
		checkState(this.component == null, "Cannot add a " + Classes.simpleName(getClass()) + " to multiple components");
		this.component = checkNotNull(component);
	}
	
	@Override
	public final void unbind(Component component) {
		if (this.component == component) {
			this.component = null;
		}
	}
	
	public Component getComponent() {
		checkState(this.component != null, "A " + Classes.simpleName(getClass()) + " must be added to a component before use");
		return component;
	}
	
	/**
	 * Call this method to initiate the download after an ajax refresh.
	 */
	public final void initiate(AjaxRequestTarget target) {
		target.appendJavaScript(createDownloadJsStatement(false).render());
	}
	
	private String getUrl(boolean fullUrl) {
		String url = getComponent().getPage().urlForListener(resourceDownloadBehavior, new PageParameters()).toString();
		if (fullUrl) {
			url = RequestCycle.get().getUrlRenderer()
					.renderFullUrl(
							Url.parse(url)
					);
		}
		if (addAntiCache) {
			url = url + (url.contains("?") ? "&" : "?");
			url = url + "t=" + System.currentTimeMillis();
		}
		return url;
	}
	
	public final String getFullUrl() {
		return getUrl(true);
	}
	
	private JsStatement createDownloadJsStatement(boolean fullUrl) {
		String url = getUrl(fullUrl);
		
		// the timeout is needed to let Wicket release the channel
		return new JsStatement().append("window").chain("setTimeout", JsUtils.doubleQuotes("window.location.href=" + JsUtils.quotes(url, true), true), String.valueOf(100));
	}

	/**
	 * Call this method to redirect to a page ({@link Component#setResponsePage(Page)}) and then initiate the download.
	 */
	public final void initiate(Page redirectPage) {
		final CharSequence statement = createDownloadJsStatement(true).render();
		redirectPage.add(new DoOnDomReadyBehavior(statement));
		getComponent().setResponsePage(redirectPage);
	}
	
	private static class DoOnDomReadyBehavior extends Behavior {
		private static final long serialVersionUID = 1L;
		private final CharSequence statement;
		public DoOnDomReadyBehavior(CharSequence statement) {
			super();
			this.statement = statement;
		}
		@Override
		public boolean isTemporary(Component component) {
			return true;
		}
		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			response.render(OnDomReadyHeaderItem.forScript(statement));
		}
	}
	
	protected abstract String getFileDisplayName();
	
	protected IResourceStream getResourceStream() {
		File file = tempFileModel.getObject();
		if (file != null && file.exists() && file.canRead() && file.isFile()) {
			return new FileResourceStream(file);
		}
		
		throw new IllegalStateException();
	}
	
	@Override
	public void detach(Component component) {
		super.detach(component);
		Detachables.detach(tempFileModel);
	}
}