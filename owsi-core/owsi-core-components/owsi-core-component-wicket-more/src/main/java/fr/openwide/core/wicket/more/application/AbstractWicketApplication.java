package fr.openwide.core.wicket.more.application;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public abstract class AbstractWicketApplication extends WebApplication {
	
	public AbstractWicketApplication() {
		super();
	}
	
	@Override
	public void init() {
		super.init();

		getMarkupSettings().setStripWicketTags(true);
		getResourceSettings().setAddLastModifiedTimeToResourceReferenceUrl(true);
		
		addComponentInstantiationListener(new SpringComponentInjector(this));
		
		mountPages();
	}
	
	protected abstract void mountPages();

}