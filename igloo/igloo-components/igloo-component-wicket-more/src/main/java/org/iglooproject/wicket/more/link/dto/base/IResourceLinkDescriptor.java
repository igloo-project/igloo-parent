package org.iglooproject.wicket.more.link.dto.base;

import java.io.Serializable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;

/**
 * Resource counterpart of {@link IPageLinkDescriptor}: link to a {@link ResourceReference} whose
 * parameters are built from a link data DTO.
 */
public interface IResourceLinkDescriptor<D extends ILinkDataDto> extends Serializable {

  ResourceReference getResourceReference();

  /**
   * @return a new, empty, data DTO from the descriptor factory.
   */
  D newDataDto();

  IResourceLinkGenerator generator(IModel<D> dataDtoModel);

  IPageLinkExtractor<D> extractor();
}
