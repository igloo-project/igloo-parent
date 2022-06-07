package org.iglooproject.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.IModel;
import org.iglooproject.functional.SerializableFunction2;
import org.iglooproject.wicket.model.Models;
import org.iglooproject.wicket.model.ReadOnlyModel;
import org.javatuples.Pair;
import org.javatuples.Triplet;

public abstract class AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T3>
		implements IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> {
	private static final long serialVersionUID = 1993813798185549585L;
	
	@Override
	public void detach() { }
	
	@Override
	public abstract L map(Triplet<? extends IModel<T1>, ? extends IModel<T2>, ? extends IModel<T3>> param);
	
	@Override
	public final L map(IModel<T1> model1, IModel<T2> model2, IModel<T3> model3) {
		return map(Triplet.with(model1, model2, model3));
	}
	
	private static final class DerivingModel<U1, U2, U> implements IModel<U> {
		private static final long serialVersionUID = 1L;
		private final Pair<? extends IModel<U1>, ? extends IModel<U2>> models;
		private final SerializableFunction2<Pair<U1, U2>, U> function;
		public DerivingModel(Pair<? extends IModel<U1>, ? extends IModel<U2>> models, SerializableFunction2<Pair<U1, U2>, U> function) {
			super();
			this.models = models;
			this.function = function;
		}
		@Override
		public U getObject() {
			return function.apply(Pair.with(models.getValue0().getObject(), models.getValue1().getObject()));
		}
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(final IModel<T1> model1) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T2, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Pair<? extends IModel<T2>, ? extends IModel<T3>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.addAt0(model1));
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
	public ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(final SerializableFunction2<Pair<T2, T3>, T1> function) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T2, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Pair<? extends IModel<T2>, ? extends IModel<T3>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.addAt0(new DerivingModel<>(param, function)));
			}
			@Override
			public void detach() {
				super.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public <U1 extends T1> IThreeParameterLinkDescriptorMapper<L, U1, T2, T3> castParameter1() {
		return new AbstractThreeParameterLinkDescriptorMapper<L, U1, T2, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Triplet<? extends IModel<U1>, ? extends IModel<T2>, ? extends IModel<T3>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.setAt0(ReadOnlyModel.<T1>of(param.getValue0())));
			}
			@Override
			public void detach() {
				super.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public ITwoParameterLinkDescriptorMapper<L, T2, T3> ignoreParameter1() {
		return setParameter1(Models.<T1>placeholder());
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(final IModel<T2> model2) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T1, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Pair<? extends IModel<T1>, ? extends IModel<T3>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.addAt1(model2));
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
	public ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(final SerializableFunction2<Pair<T1, T3>, T2> function) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T1, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Pair<? extends IModel<T1>, ? extends IModel<T3>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.addAt1(new DerivingModel<>(param, function)));
			}
			@Override
			public void detach() {
				super.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public <U2 extends T2> IThreeParameterLinkDescriptorMapper<L, T1, U2, T3> castParameter2() {
		return new AbstractThreeParameterLinkDescriptorMapper<L, T1, U2, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Triplet<? extends IModel<T1>, ? extends IModel<U2>, ? extends IModel<T3>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.setAt1(ReadOnlyModel.<T2>of(param.getValue1())));
			}
			@Override
			public void detach() {
				super.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public ITwoParameterLinkDescriptorMapper<L, T1, T3> ignoreParameter2() {
		return setParameter2(Models.<T2>placeholder());
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(final IModel<T3> model3) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T1, T2>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Pair<? extends IModel<T1>, ? extends IModel<T2>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.addAt2(model3));
			}
			@Override
			public void detach() {
				super.detach();
				model3.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}

	@Override
	public ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(final SerializableFunction2<Pair<T1, T2>, T3> function) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T1, T2>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Pair<? extends IModel<T1>, ? extends IModel<T2>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.addAt2(new DerivingModel<>(param, function)));
			}
			@Override
			public void detach() {
				super.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public <U3 extends T3> IThreeParameterLinkDescriptorMapper<L, T1, T2, U3> castParameter3() {
		return new AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, U3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(Triplet<? extends IModel<T1>, ? extends IModel<T2>, ? extends IModel<U3>> param) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(param.setAt2(ReadOnlyModel.<T3>of(param.getValue2())));
			}
			@Override
			public void detach() {
				super.detach();
				AbstractThreeParameterLinkDescriptorMapper.this.detach();
			}
		};
	}
	
	@Override
	public ITwoParameterLinkDescriptorMapper<L, T1, T2> ignoreParameter3() {
		return setParameter3(Models.<T3>placeholder());
	}


}
