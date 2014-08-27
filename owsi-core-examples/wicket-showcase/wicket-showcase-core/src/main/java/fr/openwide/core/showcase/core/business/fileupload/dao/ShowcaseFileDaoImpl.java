package fr.openwide.core.showcase.core.business.fileupload.dao;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.showcase.core.business.fileupload.model.ShowcaseFile;

@Repository("showcaseFileDao")
public class ShowcaseFileDaoImpl extends GenericEntityDaoImpl<Long, ShowcaseFile> implements IShowcaseFileDao {

}
