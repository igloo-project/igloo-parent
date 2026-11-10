package org.iglooproject.wicket.more.link.dto.base;

import java.io.Serializable;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;

public interface IPageLinkDescriptor<P extends Page, D extends ILinkDataDto>
    extends Serializable {

  Class<P> getPageClass();

  /**
   * @return a new, empty, data DTO from the descriptor factory.
   */
  D newDataDto();

  IPageLinkGenerator<P> generator(IModel<D> linkDto);

  IPageLinkExtractor<D> extractor();
}
