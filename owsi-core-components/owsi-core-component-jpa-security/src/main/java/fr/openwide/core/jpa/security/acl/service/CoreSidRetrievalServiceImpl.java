package fr.openwide.core.jpa.security.acl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;

import fr.openwide.core.jpa.security.acl.domain.PersonGroupSid;
import fr.openwide.core.jpa.security.business.person.model.IPerson;
import fr.openwide.core.jpa.security.business.person.model.IPersonGroup;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;

public class CoreSidRetrievalServiceImpl extends SidRetrievalStrategyImpl implements ISidRetrievalService {

	private static final int USERNAME_PERSONID_MAP_INITIAL_CAPACITY = 300;
	
	@Autowired
	protected IPersonService<? extends IPerson> personService;
	
	private boolean cacheEnabled = true;
	
	private Map<String, Integer> userNamePersonIdMap = new ConcurrentHashMap<String, Integer>(USERNAME_PERSONID_MAP_INITIAL_CAPACITY);
	
	@Override
	public List<Sid> getSids(Authentication authentication) {
		List<Sid> sids = super.getSids(authentication);
		
		Integer personId = getPersonIdByUserName(authentication.getName());
		if (personId != null) {
			sids.addAll(getAdditionalSidsFromPerson(personService.getById(personId)));
		}
			
		return sids;
	}
	
	protected List<Sid> getAdditionalSidsFromPerson(IPerson person) {
		List<Sid> sids = new ArrayList<Sid>();
		if (person != null) {
			List<IPersonGroup> groups = person.getPersonGroups();

			for (IPersonGroup group : groups) {
				sids.add(new PersonGroupSid(group));
			}
		}
		return sids;
	}
	
	private Integer getPersonIdByUserName(String userName) {
		Integer personId = null;
		
		if (userNamePersonIdMap.containsKey(userName) && cacheEnabled) {
			personId = userNamePersonIdMap.get(userName);
		} else {
			IPerson person = personService.getByUserName(userName);
			if (person != null) {
				personId = person.getId();
				userNamePersonIdMap.put(userName, personId);
			}
		}
		
		return personId;
	}
	
	protected void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}
	
}