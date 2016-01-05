package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.toolbar;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class CustomizableToolbarElementBuilder<T, S extends ISort<?>>
		implements IOneParameterComponentFactory<Component, CoreDataTable<T, S>> {

	private static final long serialVersionUID = 8327298869880437772L;

	private final IOneParameterComponentFactory<Component, CoreDataTable<T, S>> factory;

	private final Integer previousColspan;

	private Integer colspan = 1;

	private final Set<String> cssClasses = Sets.newHashSet();

	private Condition condition = Condition.alwaysTrue();

	public CustomizableToolbarElementBuilder(Integer previousColspan, IOneParameterComponentFactory<Component, CoreDataTable<T, S>> factory) {
		super();
		this.previousColspan = checkNotNull(previousColspan);
		this.factory = checkNotNull(factory);
	}

	@Override
	public Component create(String wicketId, final CoreDataTable<T, S> dataTable) {
		IModel<Integer> computedColspanModel = new LoadableDetachableModel<Integer>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected Integer load() {
				Integer dataTableColumnsSize = dataTable.getColumnsConditions().size();
				
				if (dataTableColumnsSize == null || previousColspan == null) {
					return 0;
				} else if (colspan == null) {
					return dataTable.getColumns().size();
				} else if (previousColspan + colspan > dataTableColumnsSize) {
					colspan = dataTableColumnsSize - previousColspan;
				}
				
				for (Entry<IColumn<T, S>, Condition> column : FluentIterable.from(dataTable.getColumnsConditions().entrySet()).skip(previousColspan).limit(colspan)) {
					if (column.getValue() != null && !column.getValue().applies()) {
						colspan--;
					}
				}
				
				return Math.max(colspan, 0);
			}
		};
		
		return factory.create(wicketId, dataTable)
				.add(
						new AttributeModifier("colspan", computedColspanModel),
						new EnclosureBehavior().condition(
								condition
								.and(
										Condition.predicate(computedColspanModel, Range.atLeast(1))
								)
						),
						new AttributeModifier("class", Joiner.on(" ").join(cssClasses))
				);
	}

	public Integer getColspan() {
		return colspan;
	}

	public void colspan(Integer colspan) {
		this.colspan = colspan;
	}

	public void full() {
		this.colspan = null;
	}

	public void when(Condition condition) {
		this.condition = condition;
	}

	public void withClass(String cssClass) {
		this.cssClasses.add(cssClass);
	}

	@Override
	public void detach() {
		Detachables.detach(factory, condition);
	}

}
