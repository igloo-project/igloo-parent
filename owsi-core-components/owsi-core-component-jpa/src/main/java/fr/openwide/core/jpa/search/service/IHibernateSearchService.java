package fr.openwide.core.jpa.search.service;

import java.util.List;

import org.apache.lucene.search.Query;

import fr.openwide.core.jpa.exception.ServiceException;

public interface IHibernateSearchService {



	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException;
	
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;
	
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException;
	
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException;
	
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException;
	
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException;
	
	void reindexAll() throws ServiceException;
	
}
