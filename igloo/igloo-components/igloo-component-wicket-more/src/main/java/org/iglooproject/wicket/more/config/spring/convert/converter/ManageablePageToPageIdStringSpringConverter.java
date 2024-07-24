package org.iglooproject.wicket.more.config.spring.convert.converter;

import org.apache.wicket.page.IManageablePage;
import org.springframework.core.convert.converter.Converter;

public class ManageablePageToPageIdStringSpringConverter
    implements Converter<IManageablePage, String> {

  @Override
  public String convert(IManageablePage source) {
    return Integer.toString(source.getPageId());
  }
}
