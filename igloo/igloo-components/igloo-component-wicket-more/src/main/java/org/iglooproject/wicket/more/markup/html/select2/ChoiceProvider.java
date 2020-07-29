package org.iglooproject.wicket.more.markup.html.select2;

import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.springframework.context.ApplicationContext;
import org.wicketstuff.select2.Response;

import com.google.common.collect.ImmutableList;

public abstract class ChoiceProvider<T> extends org.wicketstuff.select2.ChoiceProvider<T> {

	private static final long serialVersionUID = 269566747046800197L;

	private static final int PAGE_SIZE = 10;

	private int pageSize = PAGE_SIZE;

	public ChoiceProvider() {
	}

	@Override
	public final void query(String term, int page, Response<T> response) {
		query(term, page * pageSize, pageSize + 1, response);
		
		if (response.size() > 0 && response.size() > pageSize) {
			response.setHasMore(true);
			response.setResults(response.getResults().stream().limit(response.size() - 1L).collect(ImmutableList.toImmutableList()));
		}
	}

	protected abstract void query(String term, int offset, int limit, Response<T> response);

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	protected <O> O getBean(Class<O> clazz) {
		return getContext().getBean(clazz);
	}
	
	protected <O> O getBean(Class<O> clazz, Object...args) {
		return getContext().getBean(clazz, args);
	}
	
	private ApplicationContext getContext() {
		return CoreWicketApplication.get().getApplicationContext();
	}

}