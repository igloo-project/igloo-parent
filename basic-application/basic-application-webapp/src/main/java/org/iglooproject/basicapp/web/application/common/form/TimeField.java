package org.iglooproject.basicapp.web.application.common.form;

import igloo.wicket.util.IDatePattern;
import java.util.Date;
import java.util.Locale;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.iglooproject.basicapp.web.application.common.util.Masks;
import org.iglooproject.basicapp.web.application.common.validator.TimeValidator;
import org.iglooproject.wicket.more.markup.html.template.js.jquery.plugins.mask.MaskBehavior;
import org.iglooproject.wicket.more.util.convert.converters.PatternDateConverter;

public class TimeField extends TextField<Date> {

  private static final long serialVersionUID = 6545624080685881341L;

  private final IDatePattern datePattern;

  private IConverter<Date> converter;

  public TimeField(String id, IModel<Date> model, IDatePattern datePattern) {
    super(id, model, Date.class);
    this.datePattern = datePattern;

    add(new MaskBehavior(Masks.TIME, Masks.timeOptions()));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <C> IConverter<C> getConverter(Class<C> type) {
    if (Date.class.isAssignableFrom(type)) {
      if (converter == null) {
        converter =
            new PatternDateConverter(datePattern, getString(datePattern.getJavaPatternKey())) {
              private static final long serialVersionUID = 1L;

              @Override
              public Date convertToObject(String value, Locale locale) {
                if (!TimeValidator.get().isValid(value)) {
                  throw newConversionException("Invalid time format", value, locale)
                      .setResourceKey("common.validator.time");
                }
                return super.convertToObject(value, locale);
              }
            };
      }
      return (IConverter<C>) converter;
    } else {
      return super.getConverter(type);
    }
  }
}
