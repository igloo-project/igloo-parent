package fr.openwide.core.jpa.search.service;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;

import fr.openwide.core.jpa.exception.ServiceException;

public interface HibernateSearchService {

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, Analyzer analyzer) throws ServiceException;
	
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;
	
	void reindexAll() throws ServiceException;
	
}
