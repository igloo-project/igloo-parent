package fr.openwide.core.jpa.more.util.transaction.model;

public interface IAfterCommitOperation extends Runnable {
	
	@Override
	String toString();

}