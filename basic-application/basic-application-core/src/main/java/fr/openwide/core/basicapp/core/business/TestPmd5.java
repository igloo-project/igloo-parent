package fr.openwide.core.basicapp.core.business;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.LockModeType;

public enum TestPmd5 {
	;
	
	private LockModeType test1;
	
	@Enumerated
	private LockModeType test2;
	
	@Enumerated(EnumType.STRING)
	private LockModeType test3;
	
	@Enumerated(value = EnumType.STRING)
	private LockModeType test4;

	
}
