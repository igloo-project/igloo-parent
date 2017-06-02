package fr.openwide.core.jpa.search.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IHibernateSearchService extends ITransactionalAspectAwareService {

	void reindexAll() throws ServiceException;

	void reindexClasses(Collection<Class<?>> classes) throws ServiceException;

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(E entity);

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(GenericEntityReference<K, E> reference);

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(Class<E> clazz, K id);

	Set<Class<?>> getIndexedRootEntities() throws ServiceException;

	Set<Class<?>> getIndexedRootEntities(Collection<Class<?>> classes) throws ServiceException;

	void flushToIndexes();
	
	/**
	 * @deprecated Implement your own search query instead, either through a custom DAO or
	 * through {@link fr.openwide.core.jpa.more.business.search.query.ISearchQuery<T, S>} as defined in
	 * owsi-core-component-jpa-more. See in particular
	 * {@link fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery<T, S>}.
	 */
	@Deprecated
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	/**
	 * @deprecated See {@link #search(Class, String[], String)}
	 */
	@Deprecated
	<T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException;

	Analyzer getAnalyzer(String analyzerName);

	Analyzer getAnalyzer(Class<?> entityType);
}
