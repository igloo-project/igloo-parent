package org.iglooproject.wicket.more.link.dto.base;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.iglooproject.wicket.more.link.dto.dto.ILinkDataDto;
import org.iglooproject.wicket.more.link.dto.exception.PageLinkExtractionException;

public interface IPageLinkExtractor<D extends ILinkDataDto> {

  D get(PageParameters parameters) throws PageLinkExtractionException;

  D getSafely(PageParameters parameters, SerializableConsumer<Exception> onError);
}
