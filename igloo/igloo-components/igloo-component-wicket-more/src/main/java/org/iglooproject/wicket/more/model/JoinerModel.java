package org.iglooproject.wicket.more.model;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.iglooproject.commons.util.functional.SerializableSupplier;
import org.iglooproject.wicket.more.condition.Condition;
import org.javatuples.Pair;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class JoinerModel extends LoadableDetachableModel<String> {

	private static final long serialVersionUID = -1202031311993202356L;

	private static final IModel<?> NULL_MODEL = new IModel<Object>() {
		private static final long serialVersionUID = 1L;
		@Override
		public Object getObject() {
			return null;
		}
	};

	private SerializableSupplier<Joiner> joinerSupplier;

	private final List<Pair<IModel<?>, Condition>> conditionnalModels = Lists.newLinkedList();

	private JoinerModel(SerializableSupplier<Joiner> joinerSupplier) {
		this.joinerSupplier = joinerSupplier;
	}

	public static JoinerModel on(final String separator) {
		return new JoinerModel(new SerializableSupplier<Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner get() {
				return Joiner.on(separator);
			}
		});
	}

	public JoinerModel skipNulls() {
		final SerializableSupplier<Joiner> joinerSupplier = this.joinerSupplier;
		this.joinerSupplier = new SerializableSupplier<Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner get() {
				return joinerSupplier.get().skipNulls();
			}
		};
		return this;
	}

	public JoinerModel useForNull(final String nullText) {
		final SerializableSupplier<Joiner> joinerSupplier = this.joinerSupplier;
		this.joinerSupplier = new SerializableSupplier<Joiner>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Joiner get() {
				return joinerSupplier.get().useForNull(nullText);
			}
		};
		return this;
	}

	@SafeVarargs
	public final JoinerModel join(IModel<?> firstModel, IModel<?>... otherModels) {
		return join(Condition.alwaysTrue(), firstModel, otherModels);
	}

	@SafeVarargs
	public final <T> JoinerModel join(Condition condition, IModel<?> firstModel, IModel<?>... otherModels) {
		for (IModel<?> model : Lists.asList(firstModel, otherModels)) {
			join(condition, model);
		}
		return this;
	}

	@SafeVarargs
	public final <T> JoinerModel join(Predicate<? super T> predicate, IModel<? extends T> firstModel, IModel<? extends T>... otherModels) {
		for (IModel<? extends T> model : Lists.asList(firstModel, otherModels)) {
			join(Condition.predicate(model, predicate), model);
		}
		return this;
	}

	public final JoinerModel join(Condition condition, IModel<?> model) {
		conditionnalModels.add(Pair.<IModel<?>, Condition>with(model != null ? model : NULL_MODEL, condition));
		return this;
	}

	@Override
	protected String load() {
		if (joinerSupplier == null || joinerSupplier.get() == null) {
			return null;
		}
		
		List<Object> values = Lists.newLinkedList();
		for (Pair<IModel<?>, Condition> conditionnalModel : conditionnalModels) {
			if (conditionnalModel.getValue1() == null || conditionnalModel.getValue1().applies()) {
				values.add(conditionnalModel.getValue0().getObject());
			}
		}
		
		return joinerSupplier.get().join(values);
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		for (Pair<IModel<?>, Condition> conditionnalModel : conditionnalModels) {
			if (conditionnalModel != null && conditionnalModel.getValue0() != null) {
				conditionnalModel.getValue0().detach();
				conditionnalModel.getValue1().detach();
			}
		}
	}
}
