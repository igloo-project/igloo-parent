package test.convert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import igloo.wicket.convert.EnumClassAwareConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TestConvertorLocator {

  @Test
  void test_enumConvertorLocator_object() {
    IConverterLocator delegate = mock(IConverterLocator.class);
    EnumClassAwareConverterLocator enumClassAwareConverterLocator =
        new EnumClassAwareConverterLocator(delegate);
    enumClassAwareConverterLocator.getConverter(Object.class);
    verify(delegate, Mockito.only()).getConverter(Object.class);
  }

  @Test
  void test_enumConvertorLocator_enumSimple() {
    IConverterLocator delegate = mock(IConverterLocator.class);
    EnumClassAwareConverterLocator enumClassAwareConverterLocator =
        new EnumClassAwareConverterLocator(delegate);
    Assertions.assertThat(ComplexEnum.SIMPLE.getClass().getName())
        .isEqualTo("test.convert.TestConvertorLocator$ComplexEnum");
    enumClassAwareConverterLocator.getConverter(ComplexEnum.SIMPLE.getClass());
    verify(delegate, Mockito.only()).getConverter(ComplexEnum.class);
  }

  @Test
  void test_enumConvertorLocator_enumBody() {
    IConverterLocator delegate = mock(IConverterLocator.class);
    EnumClassAwareConverterLocator enumClassAwareConverterLocator =
        new EnumClassAwareConverterLocator(delegate);
    Assertions.assertThat(ComplexEnum.BODY.getClass().getName())
        .isEqualTo("test.convert.TestConvertorLocator$ComplexEnum$1");
    enumClassAwareConverterLocator.getConverter(ComplexEnum.BODY.getClass());
    verify(delegate, Mockito.only()).getConverter(ComplexEnum.class);
  }

  public static enum ComplexEnum {
    SIMPLE,
    BODY {
      @Override
      public String getValue() {
        return "overriden";
      }
    };

    public String getValue() {
      return "default";
    }
  }
}
