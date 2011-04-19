package fr.openwide.core.jpa.search.dao;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;

import fr.openwide.core.jpa.exception.ServiceException;


public interface HibernateSearchDao {

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, Analyzer analyzer) throws ServiceException;

	void reindexAll() throws ServiceException;
	
}
