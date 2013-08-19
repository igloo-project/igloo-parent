package fr.openwide.core.wicket.more.link.descriptor.builder.state;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

public interface IBasePageState {
	
	<P extends Page> IPageParametersState page(Class<P> pageClass);
	
	IPageParametersState page(IModel<? extends Class<? extends Page>> pageClassModel);

}
