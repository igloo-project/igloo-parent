package fr.openwide.core.hibernate.security.acl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;

import fr.openwide.core.hibernate.security.acl.domain.PersonGroupSid;
import fr.openwide.core.hibernate.security.business.person.model.Person;
import fr.openwide.core.hibernate.security.business.person.model.PersonGroup;
import fr.openwide.core.hibernate.security.business.person.service.PersonService;

public class CoreSidRetrievalServiceImpl extends SidRetrievalStrategyImpl implements SidRetrievalService {

	private static final int USERNAME_PERSONID_MAP_INITIAL_CAPACITY = 300;
	
	@Autowired
	protected PersonService<? extends Person> personService;
	
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

	protected List<Sid> getAdditionalSidsFromPerson(Person person) {
		List<Sid> sids = new ArrayList<Sid>();
		if (person != null) {
			List<PersonGroup> groups = person.getPersonGroups();
	
			for (PersonGroup group : groups) {
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
			Person person = personService.getByUserName(userName);
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