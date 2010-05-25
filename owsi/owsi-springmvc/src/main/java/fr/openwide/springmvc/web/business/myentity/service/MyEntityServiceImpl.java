package fr.openwide.springmvc.web.business.myentity.service;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import fr.openwide.springmvc.web.business.myentity.model.MyEntity;

@Service("myService")
public class MyEntityServiceImpl implements MyEntityService {

	private static final Log LOGGER = LogFactory.getLog(MyEntityServiceImpl.class);

	public MyEntityServiceImpl() {
	}

	@Override
	public MyEntity getEntity() {
		LOGGER.info("Returning an entity");
		return new MyEntity(10, "Entity returned by service", new Date());
	}

	@Override
	public void postEntity(MyEntity entity) {
		LOGGER.info("Posting an entity : id = " + entity.getId() 
				+ ", descr = " + entity.getDescr()
				+ ", date = " + entity.getDate());
	}
}
