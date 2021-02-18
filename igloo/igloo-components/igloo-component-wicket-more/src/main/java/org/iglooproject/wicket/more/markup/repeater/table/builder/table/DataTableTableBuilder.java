package org.iglooproject.wicket.more.markup.repeater.table.builder.table;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.wicket.behavior.Behavior;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.wicket.behavior.ClassAttributeAppender;
import org.iglooproject.wicket.more.markup.repeater.table.builder.state.IBuildState;
import org.iglooproject.wicket.more.markup.repeater.table.builder.table.state.IDataTableTableState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

public abstract class DataTableTableBuilder<T, S extends ISort<?>> implements IDataTableTableState<T, S> {

	private final Builder<Behavior> tableBehaviorsBuilder = ImmutableList.builder();

	public DataTableTableBuilder() {
		super();
	}

	@Override
	public IDataTableTableState<T, S> add(Collection<Behavior> tableBehaviorFactories) {
		this.tableBehaviorsBuilder.addAll(Objects.requireNonNull(tableBehaviorFactories));
		return this;
	}

	@Override
	public IDataTableTableState<T, S> add(Behavior firstRowsBehavior, Behavior... otherRowsBehaviors) {
		return add(Lists.asList(Objects.requireNonNull(firstRowsBehavior), otherRowsBehaviors));
	}

	@Override
	public IDataTableTableState<T, S> fixed() {
		return add(new ClassAttributeAppender("table-layout-fixed"));
	}

	@Override
	public IBuildState<T, S> end() {
		return onEnd(tableBehaviorsBuilder.build());
	}

	protected abstract IBuildState<T, S> onEnd(List<Behavior> tableBehaviors);

}
