package org.iglooproject.wicket.more.link.dto.base;

import java.io.Serializable;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.iglooproject.wicket.more.link.dto.dto.IPageLinkDataDto;

public interface IPageLinkDescriptor<P extends Page, D extends IPageLinkDataDto>
    extends Serializable {

  IPageLinkGenerator<P> generator(IModel<D> linkDto);

  IPageLinkExtractor<D> extractor();
}
