package basicapp.front.pagelink.base;

import basicapp.front.pagelink.dto.IPageLinkDataDto;
import java.io.Serializable;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

public interface IPageLinkDescriptor<P extends Page, D extends IPageLinkDataDto>
    extends Serializable {

  IPageLinkGenerator<P> generator(IModel<D> linkDto);

  IPageLinkExtractor<D> extractor();
}
