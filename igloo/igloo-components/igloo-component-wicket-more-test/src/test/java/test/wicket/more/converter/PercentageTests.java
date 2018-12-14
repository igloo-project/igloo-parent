package test.wicket.more.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import org.iglooproject.wicket.more.markup.html.basic.PercentageBigDecimalConverter;
import org.iglooproject.wicket.more.rendering.CoreRenderers;
import org.junit.Assert;
import org.junit.Test;

import test.wicket.more.AbstractWicketMoreTestCase;

public class PercentageTests extends AbstractWicketMoreTestCase {

	@Test
	public void percentageTest() {
		
		PercentageBigDecimalConverter bigDecimalConverterWithPS8 = new PercentageBigDecimalConverter(8, RoundingMode.HALF_UP, 10, true, true);
		PercentageBigDecimalConverter bigDecimalConverterWithPS2 = new PercentageBigDecimalConverter(2, RoundingMode.HALF_UP, 5, true, true);
		PercentageBigDecimalConverter bigDecimalConverterWithoutPS8 = new PercentageBigDecimalConverter(8, RoundingMode.HALF_UP, 10, true, false);
		PercentageBigDecimalConverter bigDecimalConverterWithoutPS2 = new PercentageBigDecimalConverter(2, RoundingMode.HALF_UP, 5, true, false);
		
		{
			BigDecimal bg = bigDecimalConverterWithPS2.convertToObject("51,256%", null);
			Assert.assertEquals(new BigDecimal("0.51"), bg);
			
			String s = bigDecimalConverterWithPS2.convertToString(new BigDecimal("0.51256"), null);
			Assert.assertEquals("51%", s);
			Assert.assertEquals("51%", CoreRenderers.percent("#0%", RoundingMode.HALF_UP).render(new BigDecimal("0.51256"), Locale.FRENCH));
		}
		
		{
			BigDecimal bg = bigDecimalConverterWithPS8.convertToObject("51,256%", null);
			Assert.assertEquals(new BigDecimal("0.51256000"), bg);
			
			String s = bigDecimalConverterWithPS8.convertToString(new BigDecimal("0.51256"), null);
			Assert.assertEquals("51,256%", s);
			Assert.assertEquals("51,256%", CoreRenderers.percent("#0.######%", RoundingMode.HALF_UP).render(new BigDecimal("0.51256"), Locale.FRENCH));
		}
		
		{
			BigDecimal bg = bigDecimalConverterWithoutPS2.convertToObject("51,256", null);
			Assert.assertEquals(new BigDecimal("0.51"), bg);
			
			String s = bigDecimalConverterWithoutPS2.convertToString(new BigDecimal("0.51256"), null);
			Assert.assertEquals("51", s);
			Assert.assertEquals("51", CoreRenderers.percent("#0", RoundingMode.HALF_UP).render(new BigDecimal("0.51256"), Locale.FRENCH));
		}
		
		{
			BigDecimal bg = bigDecimalConverterWithoutPS8.convertToObject("51,256", null);
			Assert.assertEquals(new BigDecimal("0.51256000"), bg);
			
			String s = bigDecimalConverterWithoutPS8.convertToString(new BigDecimal("0.51256"), null);
			Assert.assertEquals("51,256", s);
			Assert.assertEquals("51,256", CoreRenderers.percent("#0.######", RoundingMode.HALF_UP).render(new BigDecimal("0.51256"), Locale.FRENCH));
		}
		
	}

}
