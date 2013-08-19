package fr.openwide.core.wicket.more.link.descriptor.builder.impl;

import org.apache.wicket.Page;
import org.apache.wicket.core.util.lang.WicketObjects;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.springframework.util.Assert;

import fr.openwide.core.wicket.more.link.descriptor.IPageLinkDescriptor;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IBasePageState;
import fr.openwide.core.wicket.more.link.descriptor.builder.state.IPageParametersState;
import fr.openwide.core.wicket.more.link.descriptor.impl.CorePageLinkDescriptorImpl;
import fr.openwide.core.wicket.more.link.descriptor.impl.PageParametersModel;
import fr.openwide.core.wicket.more.model.ReadOnlyModel;

public class CorePageLinkDescriptorBuilder
		extends AbstractCoreLinkDescriptorBuilderParametersStateImpl<IPageLinkDescriptor>
		implements IBasePageState, IPageParametersState {
	
	private IModel<Class<? extends Page>> pageClassModel = null;

	@Override
	public <P extends Page> IPageParametersState page(Class<P> pageClass) {
		final String className = pageClass.getName();

		// Seems to be the "clean" Wicket way to serialize a class object. See BookmarkablePageLink.
		IModel<Class<P>> pageClassModel = 
				new LoadableDetachableModel<Class<P>>() {
					private static final long serialVersionUID = 1L;
					@Override
					protected Class<P> load() {
						return WicketObjects.<P>resolveClass(className);
					}
				};
		
		return page(pageClassModel);
	}

	@Override
	public IPageParametersState page(IModel<? extends Class<? extends Page>> pageClassModel) {
		Assert.notNull(pageClassModel, "pageClassModel cannot be null");
		Assert.isNull(this.pageClassModel, "Cannot set the Page class twice");
		this.pageClassModel = ReadOnlyModel.<Class<? extends Page>>of(pageClassModel);
		return this;
	}

	@Override
	protected IPageLinkDescriptor build(PageParametersModel parametersModel) {
		return new CorePageLinkDescriptorImpl(pageClassModel, parametersModel);
	}

}
