package fr.openwide.core.test.wicket.more.model;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.Localizer;
import org.apache.wicket.model.IModel;
import org.junit.Assert;
import org.junit.Test;

import fr.openwide.core.test.wicket.more.AbstractWicketMoreJpaTestCase;
import fr.openwide.core.wicket.more.model.RangeModel;
import fr.openwide.core.wicket.more.rendering.Renderer;
import fr.openwide.core.wicket.more.util.DatePattern;
import fr.openwide.core.wicket.more.util.model.Models;

public class TestRangeRenderer extends AbstractWicketMoreJpaTestCase {

	private static final String KEY_BASE = "test.wicket.more.model.dates";
	private static final String KEY_SOLO = KEY_BASE + ".solo";
	private static final String KEY_BOTH = KEY_BASE + ".both";
	private static final String KEY_FROM = KEY_BASE + ".from";
	private static final String KEY_UPTO = KEY_BASE + ".upto";
	private static final String LOWER_TAG = "${start}";
	private static final String UPPER_TAG = "${end}";

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
		
	}
	
	private void testWithoutUnits() {
		IModel<Date> lowerDateModel;
		IModel<Date> upperDateModel;
		RangeModel<Date> dateRangeModel;
		/*
		 * Tests based upon the WicketMoreTestApplication.utf8.properties file.
		 */
		// Testing .solo suffix
		lowerDateModel = Models.transientModel(new Date(42L));
		upperDateModel = Models.transientModel(new Date(42L));
		dateRangeModel = new RangeModel<Date>(lowerDateModel, upperDateModel);
		Assert.assertEquals(
				Localizer.get().getString(KEY_SOLO, null)
						.replace(LOWER_TAG, UPPER_TAG)
						.replace(UPPER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(lowerDateModel.getObject(), Locale.FRANCE)),
				Renderer.range(KEY_BASE, Renderer.fromDatePattern(DatePattern.SHORT_DATE)).asModel(dateRangeModel).getObject()
				);
		// Testing .both suffix
		lowerDateModel = Models.transientModel(new Date(10000000000000L));
		upperDateModel = Models.transientModel(new Date(90000000000000L));
		dateRangeModel = new RangeModel<Date>(lowerDateModel, upperDateModel);
		Assert.assertEquals(
				Localizer.get().getString(KEY_BOTH, null)
						.replace(LOWER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(lowerDateModel.getObject(), Locale.FRANCE))
						.replace(UPPER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(upperDateModel.getObject(), Locale.FRANCE)),
				Renderer.range(KEY_BASE, Renderer.fromDatePattern(DatePattern.SHORT_DATE)).asModel(dateRangeModel).getObject()
				);
		// Testing .from suffix
		lowerDateModel = Models.transientModel(new Date(80000000000000L));
		upperDateModel = null;
		dateRangeModel = new RangeModel<Date>(lowerDateModel, upperDateModel);
		Assert.assertEquals(
				Localizer.get().getString(KEY_FROM, null)
						.replace(LOWER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(lowerDateModel.getObject(), Locale.FRANCE)),
				Renderer.range(KEY_BASE, Renderer.fromDatePattern(DatePattern.SHORT_DATE)).asModel(dateRangeModel).getObject()
				);
		// Testing .upto suffix
		lowerDateModel = null;
		upperDateModel = Models.transientModel(new Date(20000000000000L));
		dateRangeModel = new RangeModel<Date>(lowerDateModel, upperDateModel);
		Assert.assertEquals(
				Localizer.get().getString(KEY_UPTO, null)
						.replace(UPPER_TAG, Renderer.fromDatePattern(DatePattern.SHORT_DATE).render(upperDateModel.getObject(), Locale.FRANCE)),
				Renderer.range(KEY_BASE, Renderer.fromDatePattern(DatePattern.SHORT_DATE)).asModel(dateRangeModel).getObject()
				);
	}
}
