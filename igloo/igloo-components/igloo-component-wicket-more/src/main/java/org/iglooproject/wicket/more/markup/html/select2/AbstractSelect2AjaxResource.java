package org.iglooproject.wicket.more.markup.html.select2;

import org.apache.wicket.injection.Injector;
import org.retzlaff.select2.resource.AjaxSearchResource;
import org.retzlaff.select2.resource.AjaxSearchResourceReference;
import org.springframework.context.ApplicationContext;

import org.iglooproject.wicket.more.application.CoreWicketApplication;

public abstract class AbstractSelect2AjaxResource<E> extends AjaxSearchResource<E> {

	private static final long serialVersionUID = 1L;
	
	public AbstractSelect2AjaxResource(AjaxSearchResourceReference<E> reference) {
		super(reference);
		Injector.get().inject(this);
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
