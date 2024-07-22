package org.iglooproject.wicket.more.markup.html.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import igloo.wicket.util.IDatePattern;
import java.util.Date;
import java.util.List;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.validator.DateValidator;
import org.iglooproject.wicket.more.util.convert.converters.CascadingConverter;
import org.iglooproject.wicket.more.util.convert.converters.PatternDateConverter;
import org.wicketstuff.wiquery.ui.datepicker.DateOption;

public class DatePicker extends AbstractDatePicker<Date> {

  private static final long serialVersionUID = 8051575483617364457L;

  private IConverter<Date> converter;

  private IDatePattern datePattern;

  private List<IDatePattern> datePatternsToTryBefore = Lists.newArrayList();
  private List<IDatePattern> datePatternsToTryAfter = Lists.newArrayList();

  public DatePicker(String id, IModel<Date> model, IDatePattern datePattern) {
    super(id, model, Date.class);
    this.datePattern = datePattern;
  }

  public DatePicker addDatePatternToTryBefore(IDatePattern datePatternToTryBefore) {
    datePatternsToTryBefore.add(datePatternToTryBefore);
    return this;
  }

  public DatePicker addDatePatternToTryAfter(IDatePattern datePatternToTryAfter) {
    datePatternsToTryAfter.add(datePatternToTryAfter);
    return this;
  }

  public DatePicker setRange(Date minDate, Date maxDate) {
    if (minDate != null) {
      setMinDate(new DateOption(minDate));
    }
    if (maxDate != null) {
      setMaxDate(new DateOption(maxDate));
    }
    add(new DateValidator(minDate, maxDate));
    return this;
  }

  @Override
  public void onInitialize() {
    super.onInitialize();
    this.setDateFormat(getString(datePattern.getJavascriptPatternKey()));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <C> IConverter<C> getConverter(Class<C> type) {
    if (Date.class.isAssignableFrom(type)) {
      if (converter == null) {
        converter = createConverter();
      }
      return (IConverter<C>) converter;
    } else {
      return super.getConverter(type);
    }
  }

  private IConverter<Date> createConverter() {
    IConverter<Date> mainConverter =
        new PatternDateConverter(datePattern, getString(datePattern.getJavaPatternKey()));
    if (datePatternsToTryAfter.isEmpty() && datePatternsToTryBefore.isEmpty()) {
      return mainConverter;
    } else {
      ImmutableList.Builder<IConverter<Date>> preemptiveConverters = ImmutableList.builder();
      for (IDatePattern datePatternToTryBefore : datePatternsToTryBefore) {
        preemptiveConverters.add(
            new PatternDateConverter(
                datePatternToTryBefore, getString(datePatternToTryBefore.getJavaPatternKey())));
      }
      ImmutableList.Builder<IConverter<Date>> alternativeConverters = ImmutableList.builder();
      for (IDatePattern datePatternToTryAfter : datePatternsToTryAfter) {
        alternativeConverters.add(
            new PatternDateConverter(
                datePatternToTryAfter, getString(datePatternToTryAfter.getJavaPatternKey())));
      }
      return new CascadingConverter<>(
          mainConverter, preemptiveConverters.build(), alternativeConverters.build());
    }
  }

  @Override
  protected void detachModel() {
    super.detachModel();
    converter = null;
  }
}
