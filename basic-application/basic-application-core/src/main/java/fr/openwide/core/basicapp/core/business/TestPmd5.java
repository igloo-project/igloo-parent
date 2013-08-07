package fr.openwide.core.basicapp.core.business;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.LockModeType;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public class TestPmd5 extends TestPmd4 {
	
	private LockModeType test1;
	
	@Enumerated
	private LockModeType test2;
	
	@Enumerated(EnumType.STRING)
	private LockModeType test3;
	
	@Enumerated(value = EnumType.STRING)
	private LockModeType test4;

	@Override
	public Long getId() {
		return null;
	}

	@Override
	public void setId(Long id) {
	}

	@Override
	public String getNameForToString() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}
	
}
