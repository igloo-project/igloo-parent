package org.iglooproject.wicket.more.link.descriptor.builder.state.base;

import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

import org.iglooproject.wicket.more.link.descriptor.builder.LinkDescriptorBuilder;
import org.iglooproject.wicket.more.link.descriptor.builder.state.pageinstance.IPageInstanceState;
import org.iglooproject.wicket.more.link.descriptor.generator.IPageLinkGenerator;

/**
 * @deprecated Use the late target definition syntax instead. See {@link LinkDescriptorBuilder#LinkDescriptorBuilder()}.
 */
@Deprecated
public interface IBasePageInstanceState {
	
	IPageInstanceState<IPageLinkGenerator> pageInstance(Page pageInstance);

	IPageInstanceState<IPageLinkGenerator> pageInstance(IModel<? extends Page> pageInstanceModel);

}
