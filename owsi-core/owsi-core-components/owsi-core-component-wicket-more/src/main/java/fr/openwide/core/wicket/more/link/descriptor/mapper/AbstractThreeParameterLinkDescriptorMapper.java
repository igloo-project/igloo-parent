package fr.openwide.core.wicket.more.link.descriptor.mapper;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.javatuples.Pair;

import com.google.common.base.Function;

import fr.openwide.core.wicket.more.util.model.Models;

public abstract class AbstractThreeParameterLinkDescriptorMapper<L, T1, T2, T3>
		implements IThreeParameterLinkDescriptorMapper<L, T1, T2, T3> {
	private static final long serialVersionUID = 1993813798185549585L;
	
	@Override
	public void detach() { }
	
	private static final class DerivingModel<U1, U2, U> extends AbstractReadOnlyModel<U> {
		private static final long serialVersionUID = 1L;
		private final IModel<U1> model1;
		private final IModel<U2> model2;
		private final Function<Pair<U1, U2>, U> function;
		public DerivingModel(IModel<U1> model1, IModel<U2> model2, Function<Pair<U1, U2>, U> function) {
			super();
			this.model1 = model1;
			this.model2 = model2;
			this.function = function;
		}
		@Override
		public U getObject() {
			return function.apply(Pair.with(model1.getObject(), model2.getObject()));
		}
	}

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
	public ITwoParameterLinkDescriptorMapper<L, T2, T3> setParameter1(final Function<Pair<T2, T3>, T1> function) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T2, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<T2> model2, IModel<T3> model3) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(new DerivingModel<>(model2, model3, function), model2, model3);
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
	public ITwoParameterLinkDescriptorMapper<L, T1, T3> setParameter2(final Function<Pair<T1, T3>, T2> function) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T1, T3>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<T1> model1, IModel<T3> model3) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(model1, new DerivingModel<>(model1, model3, function), model3);
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

	@Override
	public ITwoParameterLinkDescriptorMapper<L, T1, T2> setParameter3(final Function<Pair<T1, T2>, T3> function) {
		return new AbstractTwoParameterLinkDescriptorMapper<L, T1, T2>() {
			private static final long serialVersionUID = 1L;
			@Override
			public L map(IModel<T1> model1, IModel<T2> model2) {
				return AbstractThreeParameterLinkDescriptorMapper.this.map(model1, model2, new DerivingModel<>(model1, model2, function));
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
