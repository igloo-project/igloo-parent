/**
 * Provides utilities to setup an hibernate test context with jupiter.
 *
 * <pre><code>
 * class MyTestClass {
 *     {@literal @RegisterExtension}
 *     EntityManagerFactoryExtension extension = new EntityManagerFactoryExtension(
 *         AvailableSettings.DIALECT, org.hibernate.dialect.H2Dialect.class.getName(),
 *         AvailableSettings.HBM2DDL_AUTO, "create",
 *         AvailableSettings.JPA_JDBC_DRIVER, org.h2.Driver.class.getName(),
 *         AvailableSettings.JPA_JDBC_URL, "jdbc:h2:mem:schema_name;INIT=create schema if not exists schema_name",
 *         AvailableSettings.LOADED_CLASSES, Arrays.asList(OrphanOwner.class, OrphanItem.class),
 *         AvailableSettings.XML_MAPPING_ENABLED, Boolean.FALSE.toString()
 *     );
 *
 *     ...
 * }
 * </code></pre>
 */
package org.igloo.jpa.test;
