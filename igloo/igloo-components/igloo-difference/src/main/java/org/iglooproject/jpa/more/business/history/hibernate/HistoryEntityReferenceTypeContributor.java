package org.iglooproject.jpa.more.business.history.hibernate;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.sql.spi.DdlTypeRegistry;
import org.iglooproject.jpa.more.business.history.hibernate.composite.HistoryEntityReferenceEnumCompositeType;
import org.iglooproject.jpa.more.business.history.hibernate.composite.HistoryEntityReferenceLegacyCompositeType;
import org.iglooproject.jpa.more.business.history.model.AbstractHistoryLog;
import org.springframework.util.StringUtils;

/**
 * This type contributor setup enum-stored types in {@link AbstractHistoryLog} tables, only if
 * historylog optimization is enabled (<code>igloo.historylog.optimization.enabled=true</code>).
 * {@link HistoryEntityReferenceEnumCompositeType} is used to ensure that enums are used to store
 * type information.
 *
 * <p>If optimization is not enabled, {@link HistoryValueLegacyCompositeType} is used to maintain
 * old behavior.
 */
public class HistoryEntityReferenceTypeContributor implements TypeContributor {

  private static final String PROPERTY_IGLOO_HISTORYLOG_OPTIMIZATION_ENABLED =
      "igloo.historylog.optimization.enabled";

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    // TODO: why is it called twice ?
    if (isOptimizationEnabled(serviceRegistry)) {
      typeContributions.contributeType(new HistoryEntityReferenceEnumCompositeType());
      typeContributions.contributeJdbcType(new HistoryEntityReferenceTypeJdbcType());

      final DdlTypeRegistry ddlTypeRegistry =
          typeContributions.getTypeConfiguration().getDdlTypeRegistry();
      ddlTypeRegistry.addDescriptor(
          HistoryEntityReferenceTypeJdbcType.SQL_TYPE_CODE, new HistoryEntityReferenceDdlType());
    } else {
      typeContributions.contributeType(new HistoryEntityReferenceLegacyCompositeType());
    }
  }

  public static boolean isOptimizationEnabled(ServiceRegistry serviceRegistry) {
    return serviceRegistry
        .getService(ConfigurationService.class)
        .<Boolean>getSetting(
            PROPERTY_IGLOO_HISTORYLOG_OPTIMIZATION_ENABLED,
            HistoryEntityReferenceTypeContributor::convertBoolean,
            true);
  }

  public static Boolean convertBoolean(Object value) {
    if (value instanceof Boolean booleanValue) {
      return booleanValue;
    } else if (value instanceof String stringValue) {
      return StringUtils.hasLength(stringValue) ? Boolean.valueOf(stringValue) : Boolean.TRUE;
    } else {
      throw new IllegalStateException("Unexpected value %s".formatted(value));
    }
  }

  @SuppressWarnings("rawtypes")
  public static Class forName(String className) {
    try {
      return Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Class not found for %s".formatted(className), e);
    }
  }
}
