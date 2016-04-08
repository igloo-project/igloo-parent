package fr.openwide.core.wicket.more.notification.service;

import java.util.concurrent.Callable;

import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

import fr.openwide.core.context.IContextualService;
import fr.openwide.core.spring.util.StringUtils;
import fr.openwide.core.wicket.more.link.descriptor.generator.IPageLinkGenerator;

public abstract class AbstractNotificationUrlBuilderServiceImpl implements IContextualService {
	
	private static final String ANCHOR_ROOT = "#";
	
	private IWicketContextExecutor wicketExecutor;
	
	public AbstractNotificationUrlBuilderServiceImpl(IWicketContextExecutor wicketExecutor) {
		this.wicketExecutor = wicketExecutor;
	}
	
	@Override
	public <T> T runWithContext(Callable<T> callable) throws Exception {
		return wicketExecutor.runWithContext(callable);
	}
	
	protected String buildUrl(Callable<IPageLinkGenerator> pageLinkGeneratorTask) {
		return buildUrl(pageLinkGeneratorTask, null);
	}
	
	protected String buildUrl(final Callable<IPageLinkGenerator> pageLinkGeneratorTask, final String anchor) {
		Args.notNull(pageLinkGeneratorTask, "pageLinkGeneratorTask");
		
		try {
			return wicketExecutor.runWithContext(new Callable<String>() {
				@Override
				public String call() throws Exception {
					IPageLinkGenerator pageLinkGenerator = pageLinkGeneratorTask.call();
					
					StringBuilder url = new StringBuilder();
					url.append(pageLinkGenerator.fullUrl());
					if (StringUtils.hasText(anchor)) {
						url.append(ANCHOR_ROOT).append(anchor);
					}
					
					return url.toString();
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Deprecated
	protected String buildUrl(Class<? extends Page> pageClass, PageParameters parameters) {
		return buildUrl(pageClass, parameters, null);
	}
	
	@Deprecated
	protected String buildUrl(Class<? extends Page> pageClass, PageParameters parameters, String anchor) {
		return buildUrl(new BookmarkablePageRequestHandler(new PageProvider(pageClass, parameters)), anchor);
	}
	
	@Deprecated
	protected String buildUrl(final IRequestHandler requestHandler, final String anchor) {
		Args.notNull(requestHandler, "requestHandler");

		try {
			return wicketExecutor.runWithContext(new Callable<String>() {
				@Override
				public String call() throws Exception {
					StringBuilder url = new StringBuilder();
					url.append(RequestCycle.get().getUrlRenderer().renderFullUrl(Url.parse(RequestCycle.get().urlFor(requestHandler))));
					if (StringUtils.hasText(anchor)) {
						url.append(ANCHOR_ROOT).append(anchor);
					}
					
					return url.toString();
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
