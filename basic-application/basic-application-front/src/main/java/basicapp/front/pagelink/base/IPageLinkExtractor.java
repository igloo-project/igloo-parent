package basicapp.front.pagelink.base;

import basicapp.front.pagelink.dto.IPageLinkDataDto;
import basicapp.front.pagelink.exception.PageLinkExtractionException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.danekja.java.util.function.serializable.SerializableConsumer;

public interface IPageLinkExtractor<D extends IPageLinkDataDto> {

  D get(PageParameters parameters) throws PageLinkExtractionException;

  D getSafely(PageParameters parameters, SerializableConsumer<Exception> onError);
}
