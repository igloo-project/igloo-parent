package fr.openwide.core.wicket.more.markup.html.repeater.data.table.builder.toolbar;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import fr.openwide.core.jpa.more.business.sort.ISort;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.factory.IOneParameterComponentFactory;
import fr.openwide.core.wicket.more.markup.html.repeater.data.table.CoreDataTable;
import fr.openwide.core.wicket.more.util.model.Detachables;

public class CustomizableToolbarElementFactory<T, S extends ISort<?>> implements
		IOneParameterComponentFactory<Component, CoreDataTable<T, S>> {

	private static final long serialVersionUID = 8327298869880437772L;

	private final IOneParameterComponentFactory<Component, CoreDataTable<T, S>> delegate;

	private IModel<Long> colspanModel = Model.of(1L);

	private final Set<String> cssClasses = Sets.newHashSet();

	private Condition condition = Condition.alwaysTrue();

	public CustomizableToolbarElementFactory(IOneParameterComponentFactory<Component, CoreDataTable<T, S>> delegate) {
		super();
		this.delegate = delegate;
	}

	@Override
	public Component create(String wicketId, final CoreDataTable<T, S> dataTable) {
		return delegate.create(wicketId, dataTable)
				.add(
						new AttributeModifier("colspan", new LoadableDetachableModel<Long>() {
							private static final long serialVersionUID = 1L;
							@Override
							protected Long load() {
								Long colspan = (colspanModel != null && colspanModel.getObject() != null) ? colspanModel.getObject() : dataTable.getColumns().size();
								return colspan;
							}
						}),
						new EnclosureBehavior().condition(condition),
						new AttributeModifier("class", Joiner.on(" ").join(cssClasses))
				);
	}

	public void colspan(IModel<Long> colspanModel) {
		this.colspanModel = colspanModel;
	}

	public void colspan(long colspan) {
		this.colspanModel.setObject(colspan);
	}

	public void full() {
		this.colspanModel.setObject(null);
	}

	public void when(Condition condition) {
		this.condition = condition;
	}

	public void withClass(String cssClass) {
		this.cssClasses.add(cssClass);
	}

	@Override
	public void detach() {
		Detachables.detach(delegate, colspanModel);
	}

}
