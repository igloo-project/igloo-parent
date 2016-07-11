package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.model.ReadOnlyModel;


public abstract class AbstractOneParameterLinkDescriptorMapper<L, T1>
		implements IOneParameterLinkDescriptorMapper<L, T1> {

	private static final long serialVersionUID = 5478611315785648670L;

	@Override
	public void detach() { }
	
	@Override
	public <U1 extends T1> IOneParameterLinkDescriptorMapper<L, U1> castParameter1() {
		return new AbstractOneParameterLinkDescriptorMapper<L, U1>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<U1> model1) {
				return AbstractOneParameterLinkDescriptorMapper.this.map(ReadOnlyModel.<T1>of(model1));
			}
			@Override
			public void detach() {
				super.detach();
				AbstractOneParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
}
