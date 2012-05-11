package fr.openwide.core.jpa.security.acl.service;

import java.util.List;

import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;

public interface ISidRetrievalService extends SidRetrievalStrategy {

	@Override
	List<Sid> getSids(Authentication authentication);
	
}
