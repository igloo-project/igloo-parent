package org.iglooproject.wicket.more.markup;

import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupParser;
import org.apache.wicket.markup.MarkupResourceStream;
import org.iglooproject.wicket.more.markup.parser.filter.InlineEnclosureComponentHandler;

public class CoreMarkupFactory extends MarkupFactory {

  public CoreMarkupFactory() {}

  @Override
  public MarkupParser newMarkupParser(MarkupResourceStream resource) {
    MarkupParser parser = super.newMarkupParser(resource);
    parser.add(new InlineEnclosureComponentHandler(resource));
    return parser;
  }
}
