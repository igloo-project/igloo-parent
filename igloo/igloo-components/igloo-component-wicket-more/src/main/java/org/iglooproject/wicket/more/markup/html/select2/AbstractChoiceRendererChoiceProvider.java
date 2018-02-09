package org.iglooproject.wicket.more.markup.html.select2;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.springframework.context.ApplicationContext;
import org.wicketstuff.select2.ChoiceProvider;

public abstract class AbstractChoiceRendererChoiceProvider<T> extends ChoiceProvider<T> {

	private static final long serialVersionUID = -4654743240914954744L;
	
	protected final IChoiceRenderer<? super T> choiceRenderer;

	public AbstractChoiceRendererChoiceProvider(IChoiceRenderer<? super T> choiceRenderer) {
		Injector.get().inject(this);
		this.choiceRenderer = choiceRenderer;
	}

	@Override
	public String getIdValue(T object) {
		return choiceRenderer.getIdValue(object, 0 /* unused */);
	}

	@Override
	public String getDisplayValue(T object) {
		return (String) choiceRenderer.getDisplayValue(object);
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