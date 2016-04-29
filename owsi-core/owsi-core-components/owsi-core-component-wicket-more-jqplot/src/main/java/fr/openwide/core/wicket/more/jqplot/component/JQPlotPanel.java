package fr.openwide.core.wicket.more.jqplot.component;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import nl.topicus.wqplot.components.JQPlot;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotSeries;
import nl.topicus.wqplot.options.PlotTick;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.openwide.core.commons.util.functional.Predicates2;
import fr.openwide.core.wicket.markup.html.panel.InvisiblePanel;
import fr.openwide.core.wicket.more.condition.Condition;
import fr.openwide.core.wicket.more.jqplot.config.DefaultJQPlotRendererOptionsFactory;
import fr.openwide.core.wicket.more.jqplot.config.IJQPlotConfigurer;
import fr.openwide.core.wicket.more.jqplot.data.adapter.IJQPlotDataAdapter;
import fr.openwide.core.wicket.more.jqplot.plugin.autoresize.JQPlotAutoresizeJavascriptReference;
import fr.openwide.core.wicket.more.markup.html.basic.PlaceholderContainer;

/**
 * A wrapper component around {@link JQPlot} that makes it dynamic:
 * <ul>
 * <li>It uses a {@link IJQPlotDataAdapter} to generate JQPlot's data on each rendering
 * <li>It uses {@link IJQPlotConfigurer}s to refresh JQPlot's options on each rendering
 * <li>It includes a built-in placeholder for cases when there's no data to be shown (see {@link #setNonEmptyDataPredicate(Predicate)})
 * </ul>
 * 
 * <strong>Note:</strong> don't use this class directly; use one of its subclasses instead.
 * 
 * @see JQPlotPiePanel
 * @see JQPlotLinesPanel
 * @see JQPlotStackedLinesPanel
 * @see JQPlotBarsPanel
 * @see JQPlotStackedBarsPanel
 */
public abstract class JQPlotPanel<S, K, V extends Number & Comparable<V>> extends Panel {

	private static final long serialVersionUID = -138468277129057498L;

	private final IJQPlotDataAdapter<S, K, V> dataAdapter;
	
	@SpringBean
	private DefaultJQPlotRendererOptionsFactory optionsFactory;
	
	@SpringBean
	private IJQPlotConfigurer<Object, Object> defaultjqPlotConfigurer; 
	
	/*
	 * Also contains the default configurer (in first position) and the dataAdapter (in second position).
	 */
	private final Collection<IJQPlotConfigurer<? super S, ? super K>> jqPlotConfigurers = Lists.newLinkedList();
	
	private JQPlot jqPlot;
	
	private Predicate<Collection<V>> nonEmptyDataPredicate = Predicates2.notEmpty();
	
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
		
		add(new PlaceholderContainer("jqPlotPlaceholder").condition(Condition.componentVisible(jqPlot)));
		
		jqPlot.add(new Behavior() {
			private static final long serialVersionUID = 1L;
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				response.render(JavaScriptHeaderItem.forReference(JQPlotAutoresizeJavascriptReference.get()));
			}
		});
		
		jqPlot.add(
				new Condition() {
					private static final long serialVersionUID = 1L;
					@Override
					public boolean applies() {
						/*
						 *  We must implement this with a custom condition because the nonEmptyDataPredicate
						 * might change later (and thus a call to EnclosureBehavior.model(nonEmptyDataPredicate, model)
						 * would be incorrect).
						 */
						return nonEmptyDataPredicate.apply(Collections.unmodifiableCollection(dataAdapter.getValues()));
					}
				}
						.thenShow()
		);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		PlotOptions options = jqPlot.getOptions();
		
		Map<S, PlotSeries> seriesMap = Maps.newLinkedHashMap();
		for (S series : dataAdapter.getSeriesTicks()) {
			seriesMap.put(series, new PlotSeries());
		}
		
		Map<K, PlotTick> keysMap = Maps.newLinkedHashMap();
		for (K key : dataAdapter.getKeysTicks()) {
			keysMap.put(key, new PlotTick(key));
		}
		
		Locale locale = getLocale();
		for (IJQPlotConfigurer<? super S, ? super K> configurer : jqPlotConfigurers) {
			configurer.configure(options, seriesMap, keysMap, locale);
		}
		
		dataAdapter.afterConfigure(options, seriesMap, keysMap, locale);
		
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
	
	public DefaultJQPlotRendererOptionsFactory getOptionsFactory() {
		return optionsFactory;
	}
	
	public void setOptionsFactory(DefaultJQPlotRendererOptionsFactory optionsFactory) {
		this.optionsFactory = optionsFactory;
	}
}
