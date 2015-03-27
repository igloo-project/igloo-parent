package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;

public abstract class AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T3>
		implements IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> {
	private static final long serialVersionUID = 1993813798185549585L;
	
	@Override
	public void detach() { }

	@Override
	public ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(final IModel<T1> model1) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T2, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<T2> model2, IModel<T3> model3) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(model1, model2, model3);
			}
			@Override
			public void detach() {
				super.detach();
				model1.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(final IModel<T2> model2) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T1, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<T1> model1, IModel<T3> model3) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(model1, model2, model3);
			}
			@Override
			public void detach() {
				super.detach();
				model2.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(final IModel<T3> model3) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T1, T2>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<T1> model1, IModel<T2> model2) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(model1, model2, model3);
			}
			@Override
			public void detach() {
				super.detach();
				model3.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}


}
