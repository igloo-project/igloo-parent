package fr.openwide.core.wicket.more.jqplot.component;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.wicket.markup.html.panel.InvisiblePanel;
import fr.openwide.core.wicket.more.jqplot.config.IJQPlotConfigurer;
import fr.openwide.core.wicket.more.jqplot.config.JQPlotRendererOptionsFactory;
import fr.openwide.core.wicket.more.jqplot.data.adapter.IJQPlotDataAdapter;
import fr.openwide.core.wicket.more.jqplot.plugin.autoresize.JQPlotAutoresizeJavascriptReference;
import fr.openwide.core.wicket.more.markup.html.basic.EnclosureBehavior;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;
import nl.topicus.wqplot.components.JQPlot;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

public abstract class JQPlotPanel<S, K, V extends Number & Comparable<V>> extends Panel {

	private static final long serialVersionUID = -138468277129057498L;

	private final IJQPlotDataAdapter<S, K, V> dataAdapter;
	
	@SpringBean
	private JQPlotRendererOptionsFactory optionsFactory;
	
	@SpringBean
	private IJQPlotConfigurer<Object, Object> defaultjqPlotConfigurer; 
	
	/*
	 * Also contains the default configurer (in first position) and the dataAdapter (in second position).
	 */
	private final Collection<IJQPlotConfigurer<? super S, ? super K>> jqPlotConfigurers = Lists.newLinkedList();
	
	private JQPlot jqPlot;
	
	private Predicate<Collection<V>> nonEmptyDataPredicate = Predicates2.notEmpty();
	
	private EnclosureBehavior jqPlotEnclosureBehavior = null;

	protected JQPlotPanel(String id, IJQPlotDataAdapter<S, K, V> dataAdapter) {
		super(id, dataAdapter);
		
		add(defaultjqPlotConfigurer);
		
		this.dataAdapter = dataAdapter;
		add(dataAdapter);
		
		setOutputMarkupId(true);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		
		for (IJQPlotConfigurer<? super S, ? super K> configurer : jqPlotConfigurers) {
			configurer.detach();
		}
	}

	protected Component createTitleActionsPanel(String wicketId) {
		return new InvisiblePanel(wicketId);
	}
	
	@SafeVarargs
	public final JQPlotPanel<S, K, V> add(IJQPlotConfigurer<? super S, ? super K> configurer,
			IJQPlotConfigurer<? super S, ? super K> ... otherConfigurers) {
		jqPlotConfigurers.addAll(Lists.asList(configurer, otherConfigurers));
		return this;
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		this.jqPlot = new JQPlot("jqPlot", dataAdapter);
		add(jqPlot);
		
		add(new PlaceholderContainer("jqPlotPlaceholder").component(jqPlot));
		
		for (IJQPlotConfigurer<? super S, ? super K> configurer : jqPlotConfigurers) {
			configurer.initialize(jqPlot.getOptions());
		}
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		if (jqPlotEnclosureBehavior != null) {
			jqPlot.remove(jqPlotEnclosureBehavior);
		}
		jqPlotEnclosureBehavior = new EnclosureBehavior().model(nonEmptyDataPredicate, new AbstractReadOnlyModel<Collection<V>>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Collection<V> getObject() {
				return Collections.unmodifiableCollection(dataAdapter.getValues());
			}
		});
		jqPlot.add(jqPlotEnclosureBehavior);
		jqPlot.add(new Behavior() {
			private static final long serialVersionUID = 1L;
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				response.render(JavaScriptHeaderItem.forReference(JQPlotAutoresizeJavascriptReference.get()));
			}
		});
		
		PlotOptions options = jqPlot.getOptions();
		
		Map<S, PlotSeries> seriesMap = Maps.newLinkedHashMap();
		for (S series : dataAdapter.getSeriesTicks()) {
			seriesMap.put(series, new PlotSeries());
		}
		
		Map<K, PlotTick> keysMap = Maps.newLinkedHashMap();
		for (K key : dataAdapter.getKeysTicks()) {
			keysMap.put(key, new PlotTick(key));
		}
		
		for (IJQPlotConfigurer<? super S, ? super K> configurer : jqPlotConfigurers) {
			configurer.configure(jqPlot.getOptions(), seriesMap, keysMap, getLocale());
		}

		if (!seriesMap.isEmpty()) {
			options.setSeries(Lists.newArrayList(seriesMap.values()));
		}
		if (!keysMap.isEmpty()) {
			options.getAxes().getXaxis().setTicks(Lists.newArrayList(keysMap.values()));
		}
	}
	
	public JQPlotPanel<S, K, V> setNonEmptyDataPredicate(Predicate<Collection<V>> nonEmptyDataPredicate) {
		this.nonEmptyDataPredicate = checkNotNull(nonEmptyDataPredicate);
		return this;
	}
	
	public JQPlotRendererOptionsFactory getOptionsFactory() {
		return optionsFactory;
	}
	
	public void setOptionsFactory(JQPlotRendererOptionsFactory optionsFactory) {
		this.optionsFactory = optionsFactory;
	}
}
