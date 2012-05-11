package fr.openwide.springsec.web.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("securedService")
public class SecuredServiceImpl implements SecuredService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SecuredServiceImpl.class);
	
	public SecuredServiceImpl() {
	}

	@Override
	public String getDate() {
		LOGGER.info("Secured Service returning date");
		return (new Date()).toString();
	}
	
	@Override
	public List<String> getList() {
		LOGGER.info("Secured Service returning list");
		List<String> list = new ArrayList<String>();
		list.add("User");
		list.add("Supervisor");
		
		return list;
	}
}
