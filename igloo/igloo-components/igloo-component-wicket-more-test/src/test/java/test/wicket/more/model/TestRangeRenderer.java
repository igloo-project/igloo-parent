package test.wicket.more.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.Localizer;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.model.RangeModel;
import org.junit.jupiter.api.Test;

import igloo.wicket.model.Models;
import igloo.wicket.renderer.Renderer;
import igloo.wicket.util.DatePattern;
import test.wicket.more.AbstractWicketMoreTestCase;

public class TestRangeRenderer extends AbstractWicketMoreTestCase {
	
	private static final String LOWER_TAG = "${start}";
	private static final String UPPER_TAG = "${end}";
	private static final String LOWER_UNIT_TAG = "${startUnit}";
	private static final String UPPER_UNIT_TAG = "${endUnit}";
	
	private static final String KEY_DATES_BASE = "test.wicket.more.model.renderer.range.dates";
	private static final String KEY_DATES_SOLO = KEY_DATES_BASE + ".solo";
	private static final String KEY_DATES_BOTH = KEY_DATES_BASE + ".both";
	private static final String KEY_DATES_FROM = KEY_DATES_BASE + ".from";
	private static final String KEY_DATES_UPTO = KEY_DATES_BASE + ".upto";
	
	private static final String KEY_MONEY_BASE = "test.wicket.more.model.renderer.range.money";
	private static final String KEY_MONEY_SOLO = KEY_MONEY_BASE + ".solo";
	private static final String KEY_MONEY_BOTH = KEY_MONEY_BASE + ".both";
	private static final String KEY_MONEY_FROM = KEY_MONEY_BASE + ".from";
	private static final String KEY_MONEY_UPTO = KEY_MONEY_BASE + ".upto";
	
	private static final String KEY_COUNT_BASE = "test.wicket.more.model.renderer.count.money";

	/**
	 * <p>JUnit non-regression tests on Range renderers.
	 * <p><b>Tests:</b><ul>
	 * <li>Whether the base of the resource key is correctly used
	 * <li>Whether the right resource key suffix is used, regarding the bounds of the given range
	 * <li>Whether the four tags which may be found within a string designated by a resource key are correctly replaced
	 * </ul>
	 */
	@Test
	public void testRangeRenderer() {
		testWithUnits();
		testWithoutUnits();
	}
	
	private void testWithUnits() {
		String expected;
		IModel<Integer> lowerBoundModel;
		IModel<Integer> upperBoundModel;
		RangeModel<Integer> moneyRangeModel;
		// Testing .solo suffix
		lowerBoundModel = Models.transientModel(42);
		upperBoundModel = null;
		moneyRangeModel = RangeModel.singleton(lowerBoundModel);
		expected = Localizer.get().getString(KEY_MONEY_SOLO, null)
				.replace(LOWER_UNIT_TAG, UPPER_UNIT_TAG)
				.replace(UPPER_UNIT_TAG, Renderer.count(KEY_COUNT_BASE).render(lowerBoundModel.getObject(), Locale.FRANCE));
		assertThat(
				Renderer.range(KEY_MONEY_BASE, Renderer.<Integer>count(KEY_COUNT_BASE)).asModel(moneyRangeModel).getObject()
		).isEqualTo(expected);
		// Testing .both suffix
		lowerBoundModel = Models.transientModel(0);
		upperBoundModel = Models.transientModel(1);
		moneyRangeModel = RangeModel.closed(lowerBoundModel, upperBoundModel);
		expected = Localizer.get().getString(KEY_MONEY_BOTH, null)
				.replace(LOWER_UNIT_TAG, Renderer.count(KEY_COUNT_BASE).render(lowerBoundModel.getObject(), Locale.FRANCE))
				.replace(UPPER_UNIT_TAG, Renderer.count(KEY_COUNT_BASE).render(upperBoundModel.getObject(), Locale.FRANCE));
		assertThat(
				Renderer.range(KEY_MONEY_BASE, Renderer.<Integer>count(KEY_COUNT_BASE)).asModel(moneyRangeModel).getObject()
		).isEqualTo(expected);
		// Testing .from suffix
		lowerBoundModel = Models.transientModel(1);
		upperBoundModel = null;
		moneyRangeModel = RangeModel.atLeast(lowerBoundModel);
		expected = Localizer.get().getString(KEY_MONEY_FROM, null)
				.replace(LOWER_UNIT_TAG, Renderer.count(KEY_COUNT_BASE).render(lowerBoundModel.getObject(), Locale.FRANCE));
		assertThat(
				Renderer.range(KEY_MONEY_BASE, Renderer.<Integer>count(KEY_COUNT_BASE)).asModel(moneyRangeModel).getObject()
		).isEqualTo(expected);
		// Testing .upto suffix
		lowerBoundModel = null;
		upperBoundModel = Models.transientModel(90);
		moneyRangeModel = RangeModel.atMost(upperBoundModel);
		expected = Localizer.get().getString(KEY_MONEY_UPTO, null)
				.replace(UPPER_UNIT_TAG, Renderer.count(KEY_COUNT_BASE).render(upperBoundModel.getObject(), Locale.FRANCE));
		assertThat(
				Renderer.range(KEY_MONEY_BASE, Renderer.<Integer>count(KEY_COUNT_BASE)).asModel(moneyRangeModel).getObject()
		).isEqualTo(expected);
	}
	
	private void testWithoutUnits() {
		String expected;
		IModel<Date> lowerBoundModel;
		IModel<Date> upperBoundModel;
		RangeModel<Date> dateRangeModel;
		/*
		 * Tests based upon the WicketMoreTestApplication.utf8.properties file.
		 */
		// Testing .solo suffix
		lowerBoundModel = Models.transientModel(new Date(42L));
		upperBoundModel = null;
		dateRangeModel = RangeModel.singleton(lowerBoundModel);
		expected = Localizer.get().getString(KEY_DATES_SOLO, null)
				.replace(LOWER_TAG, UPPER_TAG)
				.replace(UPPER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(lowerBoundModel.getObject(), Locale.FRANCE));
		assertThat(
				Renderer.range(KEY_DATES_BASE, Renderer.fromDatePattern(DatePattern.SHORT_DATE)).asModel(dateRangeModel).getObject()
		).isEqualTo(expected);
		// Testing .both suffix
		lowerBoundModel = Models.transientModel(new Date(10000000000000L));
		upperBoundModel = Models.transientModel(new Date(90000000000000L));
		dateRangeModel = RangeModel.closed(lowerBoundModel, upperBoundModel);
		expected = 
				Localizer.get().getString(KEY_DATES_BOTH, null)
				.replace(LOWER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(lowerBoundModel.getObject(), Locale.FRANCE))
				.replace(UPPER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(upperBoundModel.getObject(), Locale.FRANCE));
		assertThat(
				Renderer.range(KEY_DATES_BASE, Renderer.fromDatePattern(DatePattern.SHORT_DATE)).asModel(dateRangeModel).getObject()
		).isEqualTo(expected);
		// Testing .from suffix
		lowerBoundModel = Models.transientModel(new Date(80000000000000L));
		upperBoundModel = null;
		dateRangeModel = RangeModel.atLeast(lowerBoundModel);
		expected = Localizer.get().getString(KEY_DATES_FROM, null)
				.replace(LOWER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(lowerBoundModel.getObject(), Locale.FRANCE));
		assertThat(
				Renderer.range(KEY_DATES_BASE, Renderer.fromDatePattern(DatePattern.SHORT_DATE)).asModel(dateRangeModel).getObject()
		).isEqualTo(expected);
		// Testing .upto suffix
		lowerBoundModel = null;
		upperBoundModel = Models.transientModel(new Date(20000000000000L));
		dateRangeModel = RangeModel.atMost(upperBoundModel);
		expected = Localizer.get().getString(KEY_DATES_UPTO, null)
				.replace(UPPER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(upperBoundModel.getObject(), Locale.FRANCE));
		assertThat(
				Renderer.range(KEY_DATES_BASE, Renderer.fromDatePattern(DatePattern.SHORT_DATE)).asModel(dateRangeModel).getObject()
		).isEqualTo(expected);
	}
}
