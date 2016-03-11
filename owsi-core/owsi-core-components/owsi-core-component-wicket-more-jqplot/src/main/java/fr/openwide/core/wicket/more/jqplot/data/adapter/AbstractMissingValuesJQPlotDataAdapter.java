package fr.openwide.core.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.jqplot.data.provider.IJQPlotDataProvider;

public abstract class AbstractMissingValuesJQPlotDataAdapter<S, K, V> extends AbstractJQPlotDataAdapter<S, K, V> {
	
	private static final long serialVersionUID = 51423240690775236L;
	
	private final IModel<? extends Collection<? extends S>> seriesModel;
	private final IModel<? extends Collection<? extends K>> keysModel;

	public AbstractMissingValuesJQPlotDataAdapter(
			IJQPlotDataProvider<S, K, V> dataProvider,
			@Nullable IModel<? extends Collection<? extends S>> seriesModel,
			@Nullable IModel<? extends Collection<? extends K>> keysModel) {
		super(dataProvider);
		this.seriesModel = seriesModel != null ? seriesModel : new AbstractReadOnlyModel<Collection<? extends S>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<? extends S> getObject() {
				return AbstractMissingValuesJQPlotDataAdapter.this.dataProvider.getSeries();
			}
			@Override
			public void detach() {
				AbstractMissingValuesJQPlotDataAdapter.this.dataProvider.detach();
			}
		};
		this.keysModel = keysModel != null ? keysModel : new AbstractReadOnlyModel<Collection<? extends K>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<? extends K> getObject() {
				return AbstractMissingValuesJQPlotDataAdapter.this.dataProvider.getKeys();
			}
			@Override
			public void detach() {
				AbstractMissingValuesJQPlotDataAdapter.this.dataProvider.detach();
			}
		};
	}

	@Override
	public void detach() {
		super.detach();
		seriesModel.detach();
		keysModel.detach();
	}

	@Override
	public Collection<S> getSeries() {
		return Collections.unmodifiableCollection(seriesModel.getObject());
	}

	@Override
	public Collection<K> getKeys() {
		return Collections.unmodifiableCollection(keysModel.getObject());
	}

}
