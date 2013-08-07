package fr.openwide.core.basicapp.core.business;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.LockModeType;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class TestPmd1 {
	
	private LockModeType test1;
	
	@Enumerated
	private LockModeType test2;
	
	@Enumerated(EnumType.STRING)
	private LockModeType test3;
	
	@Enumerated(value = EnumType.STRING)
	private LockModeType test4;
	
}
