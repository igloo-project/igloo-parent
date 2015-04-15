package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.util.model.Models;

public abstract class AbstractTwoParameterLinkDescriptorMapper<L, T1, T2>
		implements ITwoParameterLinkDescriptorMapper<L, T1, T2> {
	private static final long serialVersionUID = 1993813798185549585L;
	
	@Override
	public void detach() { }

	@Override
	public IOneParameterLinkDescriptorMapper<L, T2> setParameter1(final IModel<T1> model1) {
		return new AbstractOneParameterLinkDescriptorMapper<L, T2>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<T2> model2) {
				return AbstractTwoParameterLinkDescriptorMapper.this.map(model1, model2);
			}
			@Override
			public void detach() {
				super.detach();
				model1.detach();
				AbstractTwoParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public IOneParameterLinkDescriptorMapper<L, T2> ignoreParameter1() {
		return setParameter1(Models.<T1>placeholder());
	}

	@Override
	public IOneParameterLinkDescriptorMapper<L, T1> setParameter2(final IModel<T2> model2) {
		return new AbstractOneParameterLinkDescriptorMapper<L, T1>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<T1> model1) {
				return AbstractTwoParameterLinkDescriptorMapper.this.map(model1, model2);
			}
			@Override
			public void detach() {
				super.detach();
				model2.detach();
				AbstractTwoParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public IOneParameterLinkDescriptorMapper<L, T1> ignoreParameter2() {
		return setParameter2(Models.<T2>placeholder());
	}

}
