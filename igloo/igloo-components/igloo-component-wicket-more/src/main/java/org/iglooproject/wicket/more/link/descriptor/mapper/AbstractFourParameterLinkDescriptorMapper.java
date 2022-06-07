package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.model.Models;
import org.javatuples.Quartet;
import org.javatuples.Triplet;

public abstract class AbstractFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4>
		implements IFourParameterLinkDescriptorMapper<L, T1, T2, T3, T4> {
	private static final long serialVersionUID = -3040065136637291311L;

	@Override
	public void detach() { }
	
	@Override
	public abstract L map(Quartet<? extends IModel<T1>, ? extends IModel<T2>, ? extends IModel<T3>, ? extends IModel<T4>> param);
	
	@Override
	public final L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3, IModel<T4> model4) {
		return map(Quartet.with(model1, model2, model3, model4));
	}
	
	@Override
	public IThreeParameterLinkDescriptorMapper<L, T2, T3, T4> setParameter1(final IModel<T1> model1) {
		return new AbstractThreeParameterLinkDescriptorMapper<L, T2, T3, T4>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Triplet<? extends IModel<T2>, ? extends IModel<T3>, ? extends IModel<T4>> param) {
				return AbstractFourParameterLinkDescriptorMapper.this.map(param.addAt0(model1));
			}
			@Override
			public void detach() {
				super.detach();
				model1.detach();
				AbstractFourParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public IThreeParameterLinkDescriptorMapper<L, T2, T3, T4> ignoreParameter1() {
		return setParameter1(Models.<T1>placeholder());
	}

	@Override
	public AbstractThreeParameterLinkDescriptorMapper<L, T1, T3, T4> setParameter2(final IModel<T2> model2) {
		return new AbstractThreeParameterLinkDescriptorMapper<L, T1, T3, T4>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Triplet<? extends IModel<T1>, ? extends IModel<T3>, ? extends IModel<T4>> param) {
				return AbstractFourParameterLinkDescriptorMapper.this.map(param.addAt1(model2));
			}
			@Override
			public void detach() {
				super.detach();
				model2.detach();
				AbstractFourParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public AbstractThreeParameterLinkDescriptorMapper<L, T1, T3, T4> ignoreParameter2() {
		return setParameter2(Models.<T2>placeholder());
	}

	@Override
	public AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T4> setParameter3(final IModel<T3> model3) {
		return new AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T4>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Triplet<? extends IModel<T1>, ? extends IModel<T2>, ? extends IModel<T4>> param) {
				return AbstractFourParameterLinkDescriptorMapper.this.map(param.addAt2(model3));
			}
			@Override
			public void detach() {
				super.detach();
				model3.detach();
				AbstractFourParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T4> ignoreParameter3() {
		return setParameter3(Models.<T3>placeholder());
	}

	@Override
	public AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T3> setParameter4(final IModel<T4> model4) {
		return new AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Triplet<? extends IModel<T1>, ? extends IModel<T2>, ? extends IModel<T3>> param) {
				return AbstractFourParameterLinkDescriptorMapper.this.map(param.addAt3(model4));
			}
			@Override
			public void detach() {
				super.detach();
				model4.detach();
				AbstractFourParameterLinkDescriptorMapper.this.detach();
			}
		};
	}

	@Override
	public AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T3> ignoreParameter4() {
		return setParameter4(Models.<T4>placeholder());
	}

}
