package fr.openwide.core.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

import fr.openwide.core.wicket.more.jqplot.data.provider.IJQPlotDataProvider;
import fr.openwide.core.wicket.more.util.model.Models;

public abstract class AbstractMissingValuesJQPlotDataAdapter<S, K, V> extends AbstractJQPlotDataAdapter<S, K, V> {
	
	private static final long serialVersionUID = 51423240690775236L;
	
	private final IModel<? extends Collection<? extends S>> seriesModel;
	private final IModel<? extends Collection<? extends K>> keysModel;
	
	private final IModel<? extends V> missingValueReplacementModel;

	public AbstractMissingValuesJQPlotDataAdapter(
			final IJQPlotDataProvider<S, K, V> dataProvider,
			@Nullable IModel<? extends Collection<? extends S>> seriesModel,
			@Nullable IModel<? extends Collection<? extends K>> keysModel,
			@Nullable IModel<? extends V> missingValueReplacementModel) {
		super(dataProvider);
		this.seriesModel = seriesModel != null ? seriesModel : new AbstractReadOnlyModel<Collection<? extends S>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<? extends S> getObject() {
				return dataProvider.getSeries();
			}
			@Override
			public void detach() {
				dataProvider.detach();
			}
		};
		this.keysModel = keysModel != null ? keysModel : new AbstractReadOnlyModel<Collection<? extends K>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<? extends K> getObject() {
				return dataProvider.getKeys();
			}
			@Override
			public void detach() {
				dataProvider.detach();
			}
		};
		this.missingValueReplacementModel =
				missingValueReplacementModel == null ? Models.<V>placeholder() : missingValueReplacementModel;
	}
	
	@Override
	public void detach() {
		super.detach();
		seriesModel.detach();
		keysModel.detach();
	}

	@Override
	public Collection<S> getSeriesTicks() {
		return Collections.unmodifiableCollection(seriesModel.getObject());
	}

	@Override
	public Collection<K> getKeysTicks() {
		return Collections.unmodifiableCollection(keysModel.getObject());
	}
	
	@Override
	public V getValue(S serie, K key) {
		V value = super.getValue(serie, key);
		if (value == null) {
			return missingValueReplacementModel.getObject();
		} else {
			return value;
		}
	}

}
