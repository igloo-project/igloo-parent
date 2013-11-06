package fr.openwide.core.jpa.more.business.link.service;

import java.util.List;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;

public interface IExternalLinkWrapperService extends IGenericEntityService<Long, ExternalLinkWrapper> {
	
	List<ExternalLinkWrapper> listActive();

}
