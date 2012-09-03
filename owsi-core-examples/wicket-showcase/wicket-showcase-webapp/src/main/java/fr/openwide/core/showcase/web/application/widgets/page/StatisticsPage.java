package fr.openwide.core.showcase.web.application.widgets.page;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nl.topicus.wqplot.components.JQPlot;
import nl.topicus.wqplot.data.BaseSeries;
import nl.topicus.wqplot.data.SimpleNumberSeries;
import nl.topicus.wqplot.options.PlotBarRendererOptions;
import nl.topicus.wqplot.options.PlotCanvasAxisTickRendererOptions;
import nl.topicus.wqplot.options.PlotLegendLocation;
import nl.topicus.wqplot.options.PlotMarkerStyle;
import nl.topicus.wqplot.options.PlotOptions;
import nl.topicus.wqplot.options.PlotPieRendererOptions;
import nl.topicus.wqplot.options.PlotTooltipAxes;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.google.common.collect.Lists;

public class StatisticsPage extends WidgetsTemplate {

	private static final long serialVersionUID = -2974578921366640131L;
	
	private static final List<Integer> ACTIVE_USERS_STATS = Lists.newArrayList(40,61,108,125,134,159);
	
	private static final List<Integer> SALES_P1_STATS = Lists.newArrayList(14,8,35,40);
	private static final List<Integer> SALES_P2_STATS = Lists.newArrayList(5,11,22,3);
	private static final List<Integer> SALES_P3_STATS = Lists.newArrayList(23,38,55,24);
	private static final String P1_LEGEND = "Product 1";
	private static final String P2_LEGEND = "Product 2";
	private static final String P3_LEGEND = "Product 3";
	private static final String Q1_TICK = "Q1";
	private static final String Q2_TICK = "Q2";
	private static final String Q3_TICK = "Q3";
	private static final String Q4_TICK = "Q4";
	
	public StatisticsPage(PageParameters parameters) {
		super(parameters);
		
		addDefaultChart();
		
		addSalesChart();
		
		addPieChart();
		
		addStackedLinesChart();
	}
	
	@Override
	protected Class<? extends WebPage> getSecondMenuPage() {
		return StatisticsPage.class;
	}
	
	private void addDefaultChart() {
		// Create series
		SimpleNumberSeries<Integer> accountsCreated = new SimpleNumberSeries<Integer>();
		for (Integer account : ACTIVE_USERS_STATS) {
			accountsCreated.addEntry(account);
		}
		
		// Build JQPlot object with given series
		@SuppressWarnings("unchecked")
		JQPlot defaultChart = new JQPlot("defaultChart", new ListModel<SimpleNumberSeries<Integer>>(Lists.newArrayList(accountsCreated)));
		
		// Select few options
		PlotOptions defaultChartOptions = defaultChart.getOptions();
		defaultChartOptions.setTitle("Active users");
		defaultChartOptions.getAxes().getXaxis().setRenderer("$.jqplot.CategoryAxisRenderer");
		defaultChartOptions.getAxes().getXaxis().setTicks("2007", "2008", "2009", "2010", "2011", "2012");
		
		// Add JQPlot object to the page
		add(defaultChart);
	}
	
	private void addSalesChart() {
		// Create series
		SimpleNumberSeries<Integer> product1series = new SimpleNumberSeries<Integer>();
		product1series.addEntry(SALES_P1_STATS.get(0));
		product1series.addEntry(SALES_P1_STATS.get(1));
		product1series.addEntry(SALES_P1_STATS.get(2));
		product1series.addEntry(SALES_P1_STATS.get(3));
		SimpleNumberSeries<Integer> product2series = new SimpleNumberSeries<Integer>();
		product2series.addEntry(SALES_P2_STATS.get(0));
		product2series.addEntry(SALES_P2_STATS.get(1));
		product2series.addEntry(SALES_P2_STATS.get(2));
		product2series.addEntry(SALES_P2_STATS.get(3));
		SimpleNumberSeries<Integer> product3series = new SimpleNumberSeries<Integer>();
		product3series.addEntry(SALES_P3_STATS.get(0));
		product3series.addEntry(SALES_P3_STATS.get(1));
		product3series.addEntry(SALES_P3_STATS.get(2));
		product3series.addEntry(SALES_P3_STATS.get(3));
		
		// Build JQPlot object with given series
		@SuppressWarnings("unchecked")
		JQPlot salesChart = new JQPlot("salesChart", new ListModel<SimpleNumberSeries<Integer>>(
				Lists.newArrayList(product1series, product2series, product3series)));
		
		// Chart options
		PlotOptions salesChartOptions = salesChart.getOptions();
		salesChartOptions.setTitle("Sales in 2011");
		salesChartOptions.getTitle().setFontSize("16pt").setTextAlign("right");
		
		salesChartOptions.getGrid().setBackground("#D9EBFC").setBorderWidth(2.5);
		
		// Chart type
		salesChartOptions.getSeriesDefaults().setRenderer("$.jqplot.BarRenderer");
		PlotBarRendererOptions renderOptions = new PlotBarRendererOptions();
		renderOptions.setBarMargin(50.0);
		salesChartOptions.getSeriesDefaults().setRendererOptions(renderOptions);
		
		salesChartOptions.setStackSeries(true);
		
		// X axis
		salesChartOptions.getAxes().getXaxis().setRenderer("$.jqplot.CategoryAxisRenderer");
		salesChartOptions.getAxes().getXaxis().setTicks(Q1_TICK, Q2_TICK, Q3_TICK, Q4_TICK);
		
		PlotCanvasAxisTickRendererOptions xTickOptions = new PlotCanvasAxisTickRendererOptions()
				.setFontSize("10pt")
				.setFontWeight("normal")
				.setFontStretch(1.0);
		salesChartOptions.getAxes().getXaxis().setTickOptions(xTickOptions);
		
		// Y axis
		salesChartOptions.getAxes().getYaxis().setMin(0).setAutoscale(true);
		salesChartOptions.getAxes().getYaxis().getTickOptions().setFontSize("10pt");
		
		// Legend
		salesChartOptions.getLegend().setShow(true).setLocation(PlotLegendLocation.nw);
		salesChartOptions.addNewSeries().setLabel(P1_LEGEND);
		salesChartOptions.addNewSeries().setLabel(P2_LEGEND);
		salesChartOptions.addNewSeries().setLabel(P3_LEGEND);
		
		add(salesChart);
	}
	
	private void addPieChart() {
		// Create series
		BaseSeries<String, Double> line = new BaseSeries<String, Double>();
		line.addEntry("frogs", 3.0);
		line.addEntry("buzzards", 7.0);
		line.addEntry("deer", 2.5);
		line.addEntry("turkeys", 6.0);
		line.addEntry("moles", 5.0);
		line.addEntry("ground hogs", 4.0);
		
		// Build JQPlot object with given series
		@SuppressWarnings("unchecked")
		JQPlot pieChart = new JQPlot("pieChart", new ListModel<BaseSeries<String, Double>>(Lists.newArrayList(line)));
		
		// Chart options
		PlotOptions pieChartOptions = pieChart.getOptions();
		pieChartOptions.setTitle("Pie Chart with Legend and sliceMargin");
		PlotPieRendererOptions renderOptions = new PlotPieRendererOptions();
		renderOptions.setSliceMargin(8.0);
		pieChartOptions.getSeriesDefaults().setRenderer("$.jqplot.PieRenderer").setRendererOptions(renderOptions);
		pieChartOptions.getLegend().setShow(true).setLocation(PlotLegendLocation.nw);
		
		add(pieChart);
	}
	
	private void addStackedLinesChart() {
		// Create series
		BaseSeries<Date, Double> product1series = new BaseSeries<Date, Double>();
		
		Calendar currentMonth = GregorianCalendar.getInstance();
		currentMonth.set(2011, 8, 1);
		currentMonth = DateUtils.truncate(currentMonth, Calendar.MONTH);
		
		Calendar dateMax = GregorianCalendar.getInstance();
		dateMax.set(2012, 8, 1);
		dateMax = DateUtils.truncate(dateMax, Calendar.MONTH);
		
		while (currentMonth.compareTo(dateMax) <= 0) {
			product1series.addEntry(currentMonth.getTime(), Math.random() * 8000 + 1000);
			currentMonth.add(Calendar.MONTH, 1);
		}
		
		BaseSeries<Date, Double> product2series = new BaseSeries<Date, Double>();
		currentMonth.set(2011, 8, 1);
		currentMonth = DateUtils.truncate(currentMonth, Calendar.MONTH);
		while (currentMonth.compareTo(dateMax) <= 0) {
			product2series.addEntry(currentMonth.getTime(), Math.random() * 18000);
			currentMonth.add(Calendar.MONTH, 1);
		}
		
		BaseSeries<Date, Double> product3series = new BaseSeries<Date, Double>();
		currentMonth.set(2011, 8, 1);
		currentMonth = DateUtils.truncate(currentMonth, Calendar.MONTH);
		while (currentMonth.compareTo(dateMax) <= 0) {
			product3series.addEntry(currentMonth.getTime(), Math.random() * 3000);
			currentMonth.add(Calendar.MONTH, 1);
		}
		
		// Build JQPlot object with given series
		@SuppressWarnings("unchecked")
		JQPlot stackedLinesChart = new JQPlot("stackedLinesChart", new ListModel<BaseSeries<Date, Double>>(
				Lists.newArrayList(product1series, product2series, product3series)));
		
		// Chart options
		PlotOptions stackedLinesOptions = stackedLinesChart.getOptions();
		stackedLinesOptions.setTitle("Results by month, 2011 - 2012");
		stackedLinesOptions.getTitle().setFontSize("16pt").setTextAlign("right");
		
		stackedLinesOptions.setStackSeries(true);
		stackedLinesOptions.getSeriesDefaults()
				.setShowMarker(false)
				.setShadow(false)
				.setFill(true)
				.setFillAndStroke(true);
		
		// Markers
		stackedLinesOptions.getSeriesDefaults().getMarkerOptions()
						.setShow(true)
						.setStyle(PlotMarkerStyle.filledSquare)
						.setSize(6.0)
						.setLineWidth(1.0);
		
		// X axis
		Calendar calendarMin = GregorianCalendar.getInstance();
		calendarMin.set(2011, 8, 1);
		calendarMin = DateUtils.truncate(calendarMin, Calendar.MONTH);
		
		Calendar calendarMax = GregorianCalendar.getInstance();
		calendarMax.set(2012, 8, 1);
		calendarMax = DateUtils.truncate(calendarMax, Calendar.MONTH);
		
		stackedLinesOptions.getAxes().getXaxis()
				.setRenderer("$.jqplot.DateAxisRenderer")
				.setTickRenderer("$.jqplot.CanvasAxisTickRenderer")
				.setTickInterval("1 month")
				.setMin(calendarMin.getTime())
				.setMax(calendarMax.getTime());
		
		stackedLinesOptions.getHighlighter()
			.setShow(true)
			.setShowTooltip(true)
			.setTooltipAxes(PlotTooltipAxes.y)
			.setFormatString("%d");
		
		PlotCanvasAxisTickRendererOptions xTickOptions = new PlotCanvasAxisTickRendererOptions()
				.setFormatString("%B, %Y")
				.setFontSize("10pt")
				.setAngle(-30.0)
				.setFontWeight("normal");
		stackedLinesOptions.getAxes().getXaxis().setTickOptions(xTickOptions);
		
		// Y axis
		stackedLinesOptions.getAxes().getYaxis().setMin(0.0).setAutoscale(true);
		stackedLinesOptions.getAxes().getYaxis().getTickOptions().setFontSize("10pt");
		
		// Legend
		stackedLinesOptions.getLegend().setShow(true).setLocation(PlotLegendLocation.nw);
		stackedLinesOptions.addNewSeries().setLabel(P1_LEGEND);
		stackedLinesOptions.addNewSeries().setLabel(P2_LEGEND);
		stackedLinesOptions.addNewSeries().setLabel(P3_LEGEND);
		
		add(stackedLinesChart);
	}
}
