package org.iglooproject.wicket.more.config.spring.convert.converter;

import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.springframework.core.convert.converter.Converter;

public class PageIdStringToPageSpringConverter implements Converter<String, Page> {

  @Override
  public Page convert(String source) {
    if (source == null) {
      return null;
    }

    return new PageReference(Integer.parseInt(source)).getPage();
  }
}
