package fr.openwide.core.jpa.search.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IHibernateSearchService extends ITransactionalAspectAwareService {

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

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	void reindexAll() throws ServiceException;

	void reindexClasses(List<Class<?>> classes) throws ServiceException;

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(E entity);

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(Class<E> clazz, K id);

	Set<Class<?>> getIndexedRootEntities() throws ServiceException;

	Set<Class<?>> getIndexedRootEntities(List<Class<?>> classes) throws ServiceException;
}
