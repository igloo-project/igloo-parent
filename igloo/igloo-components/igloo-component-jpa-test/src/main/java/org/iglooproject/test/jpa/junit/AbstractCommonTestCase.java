package org.iglooproject.test.jpa.junit;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MapKey;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.Assertions;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hibernate.internal.SessionImpl;
import org.hibernate.metamodel.model.domain.internal.EmbeddableTypeImpl;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.referencedata.service.IGenericReferenceDataSubService;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/** This class contains junit4/junit5 common code. */
@ContextConfiguration(initializers = ExtendedTestApplicationContextInitializer.class)
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
  EntityManagerExecutionListener.class
})
@TestPropertySource(properties = "igloo.profile=test")
abstract class AbstractCommonTestCase {

  @Autowired private EntityManagerUtils entityManagerUtils;

  @Autowired private IHibernateSearchService hibernateSearchService;

  protected abstract void cleanAll() throws ServiceException, SecurityServiceException;

  protected static <E extends GenericEntity<?, ? super E>> void cleanEntities(
      IGenericEntityService<?, E> service) throws ServiceException, SecurityServiceException {
    for (E entity : service.list()) {
      service.delete(entity);
    }
  }

  protected static <E extends GenericReferenceData<?, ?>> void cleanReferenceData(
      IGenericReferenceDataSubService service, Class<E> clazz)
      throws ServiceException, SecurityServiceException {
    for (E entity : service.list(clazz)) {
      service.delete(entity);
    }
  }

  public void init() throws ServiceException, SecurityServiceException {
    cleanAll();
    checkEmptyDatabase();
    clearCaches();
  }

  public void close() throws ServiceException, SecurityServiceException {
    cleanAll();
    checkEmptyDatabase();
    clearCaches();
  }

  /**
   * This method serves only when the test threads are stopped abruptly (not for the nominal case)
   * The purpose is to clean Lucene indexes by reindexing an empty database It takes around 10
   * milliseconds if the database is empty (except the first time, it can take 200 millisecond)
   * otherwise it takes 500 milliseconds wheter there is 1000 or 10 000 objets to reindex
   */
  protected void emptyIndexes() throws ServiceException {
    Set<Class<?>> clazzes = hibernateSearchService.getIndexedRootEntities();
    FullTextEntityManager fullTextEntityManager =
        Search.getFullTextEntityManager(getEntityManager());
    for (Class<?> clazz : clazzes) {
      QueryBuilder builder =
          fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
      FullTextQuery query =
          fullTextEntityManager.createFullTextQuery(builder.all().createQuery(), clazz);
      if (query.getResultSize() > 0) {
        hibernateSearchService.reindexAll();
        break;
      }
    }
  }

  protected final <T extends GenericEntity<?, ?>> Matcher<T> isAttachedToSession() {
    return new TypeSafeMatcher<T>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("an entity already in the session");
      }

      @Override
      protected boolean matchesSafely(T item) {
        return entityManagerUtils.getCurrentEntityManager().contains(item);
      }
    };
  }

  protected <E extends GenericEntity<Long, E>> void testEntityStringFields(
      E entity, IGenericEntityService<Long, E> service)
      throws ServiceException, SecurityServiceException {
    BeanWrapper source = PropertyAccessorFactory.forBeanPropertyAccess(entity);
    PropertyDescriptor[] fields = source.getPropertyDescriptors();

    // creationDate est la date de création de l'objet donc indépendant entre les deux objets
    String[] ignoredFields = {};
    for (PropertyDescriptor field : fields) {
      String fieldName = field.getName();
      if (source.isWritableProperty(fieldName)
          && String.class.isAssignableFrom(field.getPropertyType())) {
        if (!ArrayUtils.contains(ignoredFields, fieldName)
            && source.isReadableProperty(fieldName)) {
          source.setPropertyValue(fieldName, fieldName);
        }
      }
    }

    service.create(entity);

    for (PropertyDescriptor field : fields) {
      String fieldName = field.getName();
      if (source.isWritableProperty(fieldName)
          && String.class.isAssignableFrom(field.getPropertyType())) {
        if (!ArrayUtils.contains(ignoredFields, fieldName)
            && source.isReadableProperty(fieldName)) {
          assertThat(source.getPropertyValue(fieldName)).isEqualTo(fieldName);
        }
      }
    }
  }

  private void checkEmptyDatabase() {
    Set<EntityType<?>> entityTypes =
        getEntityManager().getEntityManagerFactory().getMetamodel().getEntities();
    for (EntityType<?> entityType : entityTypes) {
      List<?> entities = listEntities(entityType.getBindableJavaType());

      Assertions.assertThat(entities).as("Il reste des objets de type %1$s", entityType).isEmpty();
    }
  }

  /**
   * With spring test caching mechanism, we use the same entity manager for all tests. Even if cache
   * are handled correctly for eviction when entities are removed at test end, it keeps in cache
   * SoftLock for read/write strategy cache.
   *
   * <p>If we reuse an identifier, then re-insert fails to insert cache entry as item is soft-lock,
   * and it can be cached only at first retrieval. This behavior lead to unexpected and inconsistent
   * result in TestCache.
   */
  private void clearCaches() {
    ((SessionImpl) getEntityManager().getDelegate())
        .getSessionFactory()
        .getCache()
        .evictAllRegions();
  }

  protected <E> List<E> listEntities(Class<E> clazz) {
    CriteriaBuilder cb = entityManagerUtils.getEntityManager().getCriteriaBuilder();
    CriteriaQuery<E> cq = cb.createQuery(clazz);
    cq.from(clazz);

    return entityManagerUtils.getEntityManager().createQuery(cq).getResultList();
  }

  protected <E extends GenericEntity<?, ?>> Long countEntities(Class<E> clazz) {
    CriteriaBuilder cb = entityManagerUtils.getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<E> root = cq.from(clazz);
    cq.select(cb.count(root));

    return (Long) entityManagerUtils.getEntityManager().createQuery(cq).getSingleResult();
  }

  @Deprecated(since = "4.1.0")
  protected void assertDatesWithinXSeconds(Date date1, Date date2, Integer delayInSeconds) {
    assertThat(Math.abs(date1.getTime() - date2.getTime()) < delayInSeconds * 1000l).isTrue();
    assertThat(date1).isCloseTo(date2, TimeUnit.SECONDS.toMillis(1000));
  }

  protected EntityManager getEntityManager() {
    return entityManagerUtils.getEntityManager();
  }

  protected void entityManagerOpen() {
    entityManagerUtils.openEntityManager();
  }

  protected void entityManagerClose() {
    entityManagerUtils.closeEntityManager();
  }

  protected void entityManagerReset() {
    entityManagerClose();
    entityManagerOpen();
  }

  protected void entityManagerClear() {
    entityManagerUtils.getEntityManager().clear();
  }

  protected void entityManagerDetach(Object object) {
    entityManagerUtils.getEntityManager().detach(object);
  }

  /** Méthode utilisée à des fins de tests. */
  protected void testMetaModel(Attribute<?, ?> attribute, List<Class<?>> classesAutorisees)
      throws NoSuchFieldException, SecurityException {
    testMetaModel(attribute, classesAutorisees, Collections.<Attribute<?, ?>>emptyList());
  }

  /** Méthode utilisée à des fins de tests. */
  protected void testMetaModel(
      Attribute<?, ?> attribute,
      List<Class<?>> classesAutorisees,
      List<Attribute<?, ?>> ignoredAttributes)
      throws NoSuchFieldException, SecurityException {
    for (Attribute<?, ?> ignoredAttribute : ignoredAttributes) {
      if (ignoredAttribute.getJavaMember().equals(attribute.getJavaMember())) {
        // champ ignoré
        return;
      }
    }

    Enumerated enumerated =
        attribute
            .getJavaMember()
            .getDeclaringClass()
            .getDeclaredField(attribute.getName())
            .getAnnotation(Enumerated.class);
    MapKeyEnumerated mapKeyEnumerated =
        attribute
            .getJavaMember()
            .getDeclaringClass()
            .getDeclaredField(attribute.getName())
            .getAnnotation(MapKeyEnumerated.class);
    MapKey mapKey =
        attribute
            .getJavaMember()
            .getDeclaringClass()
            .getDeclaredField(attribute.getName())
            .getAnnotation(MapKey.class);

    // cas des embeddable et des collectionOfElements d'embeddable
    if (attribute.getPersistentAttributeType().equals(PersistentAttributeType.ELEMENT_COLLECTION)
        && EmbeddableTypeImpl.class.isInstance(
            ((PluralAttribute<?, ?, ?>) attribute).getElementType())) {
      PluralAttribute<?, ?, ?> pluralAttribute = (PluralAttribute<?, ?, ?>) attribute;
      if (classesAutorisees.contains(pluralAttribute.getElementType().getJavaType())) {
        // type autorisé de manière explicite
        return;
      }
      for (Attribute<?, ?> embeddedAttribute :
          ((EmbeddableTypeImpl<?>) pluralAttribute.getElementType()).getAttributes()) {
        testMetaModel(embeddedAttribute, classesAutorisees, ignoredAttributes);
      }
      return;
    } else if (attribute.getPersistentAttributeType().equals(PersistentAttributeType.EMBEDDED)) {
      SingularAttribute<?, ?> singularAttribute = (SingularAttribute<?, ?>) attribute;
      if (classesAutorisees.contains(singularAttribute.getJavaType())) {
        // type autorisé de manière explicite
        return;
      }
      if (EmbeddableTypeImpl.class.isInstance(singularAttribute.getType())) {
        for (Attribute<?, ?> embeddedAttribute :
            ((EmbeddableTypeImpl<?>) singularAttribute.getType()).getAttributes()) {
          testMetaModel(embeddedAttribute, classesAutorisees, ignoredAttributes);
        }
        return;
      }
    }

    if (attribute.getPersistentAttributeType().equals(PersistentAttributeType.BASIC)
        && !classesAutorisees.contains(attribute.getJavaType())
        && (enumerated == null || EnumType.ORDINAL.equals(enumerated.value()))) {
      throw new IllegalStateException(
          "Champ \""
              + attribute.getName()
              + "\", de type "
              + attribute.getJavaType().getSimpleName()
              + " refusé");
    } else if (attribute
            .getPersistentAttributeType()
            .equals(PersistentAttributeType.ELEMENT_COLLECTION)
        && PluralAttribute.class.isInstance(attribute)
        && !classesAutorisees.contains(
            ((PluralAttribute<?, ?, ?>) attribute).getElementType().getJavaType())
        && (enumerated == null || EnumType.ORDINAL.equals(enumerated.value()))) {
      PluralAttribute<?, ?, ?> pluralAttribute = (PluralAttribute<?, ?, ?>) attribute;
      throw new IllegalStateException(
          "Collection \""
              + attribute.getName()
              + "\" de "
              + pluralAttribute.getElementType().getJavaType().getSimpleName()
              + " refusée");
    } else if (attribute instanceof MapAttribute) {
      MapAttribute<?, ?, ?> mapAttribute = (MapAttribute<?, ?, ?>) attribute;
      if (Enum.class.isAssignableFrom(mapAttribute.getKeyJavaType())
          && (mapKeyEnumerated == null || EnumType.ORDINAL.equals(mapKeyEnumerated.value()))
          && mapKey
              == null /* if @MapKey present, then field format is defined elsewhere and check is useless */) {
        throw new IllegalStateException(
            "Map \""
                + attribute.getName()
                + "\" de clés ordinales "
                + ((PluralAttribute<?, ?, ?>) attribute)
                    .getElementType()
                    .getJavaType()
                    .getSimpleName()
                + " refusée");
      }
      if (Enum.class.isAssignableFrom(mapAttribute.getElementType().getJavaType())
          && (enumerated == null || EnumType.ORDINAL.equals(enumerated.value()))) {
        throw new IllegalStateException(
            "Map \""
                + attribute.getName()
                + "\" de valeurs ordinales "
                + ((PluralAttribute<?, ?, ?>) attribute)
                    .getElementType()
                    .getJavaType()
                    .getSimpleName()
                + " refusée");
      }
    }
  }

  /**
   * Méthode permettant de s'assurer que les attributs des classes marquées @Entity ne seront pas
   * sérialisés en "bytea" lors de leur écriture en base.
   *
   * @param classesAutorisees : concerne uniquement des classes matérialisées. Si une enum fait
   *     péter le test, c'est qu'il manque l'annotation @Enumerated ou que celle-ci prend
   *     EnumType.ORDINAL en paramètre
   */
  protected void testMetaModel(
      List<Attribute<?, ?>> ignoredAttributes, Class<?>... classesAutorisees)
      throws NoSuchFieldException, SecurityException {
    List<Class<?>> listeAutorisee = Lists.newArrayList();
    listeAutorisee.add(String.class);
    listeAutorisee.add(Long.class);
    listeAutorisee.add(Double.class);
    listeAutorisee.add(Integer.class);
    listeAutorisee.add(Float.class);
    listeAutorisee.add(Date.class);
    listeAutorisee.add(Instant.class);
    listeAutorisee.add(LocalDate.class);
    listeAutorisee.add(LocalDateTime.class);
    listeAutorisee.add(LocalTime.class);
    listeAutorisee.add(Year.class);
    listeAutorisee.add(YearMonth.class);
    listeAutorisee.add(Duration.class);
    listeAutorisee.add(BigDecimal.class);
    listeAutorisee.add(Boolean.class);
    listeAutorisee.add(UUID.class);
    listeAutorisee.add(int.class);
    listeAutorisee.add(long.class);
    listeAutorisee.add(double.class);
    listeAutorisee.add(boolean.class);
    listeAutorisee.add(float.class);
    for (Class<?> clazz : classesAutorisees) {
      listeAutorisee.add(clazz);
    }

    for (EntityType<?> entityType : getEntityManager().getMetamodel().getEntities()) {
      for (Attribute<?, ?> attribute : entityType.getDeclaredAttributes()) {
        testMetaModel(attribute, listeAutorisee, ignoredAttributes);
      }
    }
  }

  protected void testMetaModel(Class<?>... classesAutorisees)
      throws NoSuchFieldException, SecurityException {
    testMetaModel(Collections.<Attribute<?, ?>>emptyList(), classesAutorisees);
  }
}
