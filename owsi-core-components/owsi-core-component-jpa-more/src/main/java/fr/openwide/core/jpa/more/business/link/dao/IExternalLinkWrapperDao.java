package fr.openwide.core.jpa.more.business.link.dao;

import java.util.List;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;

public interface IExternalLinkWrapperDao extends IGenericEntityDao<Long, ExternalLinkWrapper> {

	List<ExternalLinkWrapper> listActive();
}
