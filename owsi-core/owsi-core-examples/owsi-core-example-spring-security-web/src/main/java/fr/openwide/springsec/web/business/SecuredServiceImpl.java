package fr.openwide.springsec.web.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service("securedService")
public class SecuredServiceImpl implements SecuredService {

	private static final Log LOGGER = LogFactory.getLog(SecuredServiceImpl.class);
	
	public SecuredServiceImpl() {
	}

	public String getDate() {
		LOGGER.info("Secured Service returning date");
		return (new Date()).toString();
	}
	
	public List<String> getList() {
		LOGGER.info("Secured Service returning list");
		List<String> list = new ArrayList<String>();
		list.add("User");
		list.add("Supervisor");
		
		return list;
	}
}
