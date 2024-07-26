package test.wicket.more.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import igloo.wicket.component.PercentageBigDecimalConverter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import org.iglooproject.wicket.more.rendering.CoreRenderers;
import org.junit.jupiter.api.Test;
import test.wicket.more.AbstractWicketMoreTestCase;

class PercentageTests extends AbstractWicketMoreTestCase {

  @Test
  void percentageTest() {

    PercentageBigDecimalConverter bigDecimalConverterWithPS8 =
        new PercentageBigDecimalConverter(8, RoundingMode.HALF_UP, 10, true, true);
    PercentageBigDecimalConverter bigDecimalConverterWithPS2 =
        new PercentageBigDecimalConverter(2, RoundingMode.HALF_UP, 5, true, true);
    PercentageBigDecimalConverter bigDecimalConverterWithoutPS8 =
        new PercentageBigDecimalConverter(8, RoundingMode.HALF_UP, 10, true, false);
    PercentageBigDecimalConverter bigDecimalConverterWithoutPS2 =
        new PercentageBigDecimalConverter(2, RoundingMode.HALF_UP, 5, true, false);

    {
      BigDecimal bg = bigDecimalConverterWithPS2.convertToObject("51,256%", null);
      assertEquals(new BigDecimal("0.51"), bg);

      String s = bigDecimalConverterWithPS2.convertToString(new BigDecimal("0.51256"), null);
      assertEquals("51%", s);
      assertEquals(
          "51%",
          CoreRenderers.percent("#0%", RoundingMode.HALF_UP)
              .render(new BigDecimal("0.51256"), Locale.FRENCH));
    }

    {
      BigDecimal bg = bigDecimalConverterWithPS8.convertToObject("51,256%", null);
      assertEquals(new BigDecimal("0.51256000"), bg);

      String s = bigDecimalConverterWithPS8.convertToString(new BigDecimal("0.51256"), null);
      assertEquals("51,256%", s);
      assertEquals(
          "51,256%",
          CoreRenderers.percent("#0.######%", RoundingMode.HALF_UP)
              .render(new BigDecimal("0.51256"), Locale.FRENCH));
    }

    {
      BigDecimal bg = bigDecimalConverterWithoutPS2.convertToObject("51,256", null);
      assertEquals(new BigDecimal("0.51"), bg);

      String s = bigDecimalConverterWithoutPS2.convertToString(new BigDecimal("0.51256"), null);
      assertEquals("51", s);
      assertEquals(
          "51",
          CoreRenderers.percent("#0", RoundingMode.HALF_UP)
              .render(new BigDecimal("0.51256"), Locale.FRENCH));
    }

    {
      BigDecimal bg = bigDecimalConverterWithoutPS8.convertToObject("51,256", null);
      assertEquals(new BigDecimal("0.51256000"), bg);

      String s = bigDecimalConverterWithoutPS8.convertToString(new BigDecimal("0.51256"), null);
      assertEquals("51,256", s);
      assertEquals(
          "51,256",
          CoreRenderers.percent("#0.######", RoundingMode.HALF_UP)
              .render(new BigDecimal("0.51256"), Locale.FRENCH));
    }
  }
}
