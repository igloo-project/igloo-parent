package fr.openwide.core.wicket.more.markup.repeater.data;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.markup.repeater.OddEvenItem;


public abstract class OddEvenDataView<T> extends DataView<T> {

	private static final long serialVersionUID = 7579892102655985018L;

	public OddEvenDataView(String id, final IDataProvider<T> dataProvider,
			int itemsPerPage) {
		super(id, dataProvider, itemsPerPage);
	}

	public OddEvenDataView(String id, final IDataProvider<T> dataProvider) {
		super(id, dataProvider);
	}

	@Override
	protected Item<T> newItem(final String id, int index, final IModel<T> model) {
		return new OddEvenItem<T>(id, index, model);
	}

}
