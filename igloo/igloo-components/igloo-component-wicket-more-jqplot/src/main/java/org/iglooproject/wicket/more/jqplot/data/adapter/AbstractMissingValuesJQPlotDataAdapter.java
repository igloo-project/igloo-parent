package org.iglooproject.wicket.more.jqplot.data.adapter;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.model.Models;
import org.iglooproject.wicket.more.jqplot.data.provider.IJQPlotDataProvider;

public abstract class AbstractMissingValuesJQPlotDataAdapter<S, K, V, TK> extends AbstractJQPlotDataAdapter<S, K, V, TK> {
	
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
		this.seriesModel = seriesModel != null ? seriesModel : new IModel<Collection<? extends S>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<? extends S> getObject() {
				return dataProvider.getSeries();
			}
			@Override
			public void detach() {
				IModel.super.detach();
				dataProvider.detach();
			}
		};
		this.keysModel = keysModel != null ? keysModel : new IModel<Collection<? extends K>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<? extends K> getObject() {
				return dataProvider.getKeys();
			}
			@Override
			public void detach() {
				IModel.super.detach();
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
