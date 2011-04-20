package fr.openwide.core.hibernate.security.acl.service;

import java.util.List;

import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;

public interface SidRetrievalService extends SidRetrievalStrategy {

	List<Sid> getSids(Authentication authentication);
	
}
