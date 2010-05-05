package fr.openwide.springsec.web.business;

import java.util.List;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

public interface SecuredService {

	/*
	 * @PreAuthorize : Permits access if expression evaluates to true
	 * @PostAuthorize : Restricts access to a method’s return value
	 */             
	@PreAuthorize("hasRole('ROLE_SUPERVISOR')")
	public String getDate();
	
	/*
	 * @PreFilter : Filters collection method arguments according to expression evaluation
	 * @PostFilter : Filters a collection return value according to expression evaluation
	 */
	@PostFilter("filterObject == 'User' or hasRole('ROLE_SUPERVISOR')")
	public List<String> getList();
}
