package fr.openwide.core.hibernate.security.acl.domain;

import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.model.Sid;

import fr.openwide.core.hibernate.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.hibernate.security.business.person.model.PersonGroup;

public class PersonGroupSid extends GrantedAuthoritySid {

	private static final long serialVersionUID = -1418624664701634350L;
	
	private static final String SEPARATOR = "-";
	
	public PersonGroupSid(String groupName) {
		super(CoreAuthorityConstants.PERSON_GROUP_PREFIX + groupName);
	}

	public PersonGroupSid(PersonGroup personGroup) {
		super(CoreAuthorityConstants.PERSON_GROUP_PREFIX + personGroup.getId() + SEPARATOR + personGroup.getName());
	}
	
	public static Integer getGroupId(String grantedAuthority) {
		Integer groupId = null;
		try {
			groupId = Integer.parseInt(grantedAuthority.substring(CoreAuthorityConstants.PERSON_GROUP_PREFIX.length() , grantedAuthority.indexOf(SEPARATOR)));
		} catch(NumberFormatException e) {
		}
		return groupId;
	}
	
	public static boolean isPersonGroupSid(Sid sid) {
		if(sid instanceof GrantedAuthoritySid) {
			return ((GrantedAuthoritySid) sid).getGrantedAuthority().startsWith(CoreAuthorityConstants.PERSON_GROUP_PREFIX);
		} else {
			return false;
		}
	}
}
