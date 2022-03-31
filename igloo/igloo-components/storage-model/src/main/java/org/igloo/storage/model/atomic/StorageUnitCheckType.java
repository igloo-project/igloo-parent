package org.igloo.storage.model.atomic;

/**
 * Configure automatic check behavior for a {@link StorageUnit}
 */
public enum StorageUnitCheckType {

	/**
	 * No automatic check.
	 */
	NONE,
	/**
	 * Listing and file size checks.
	 */
	LISTING_SIZE,
	/**
	 * Listing, file size and checksum check. (checksum can be checked only if available on <code>Fichier</code>)
	 */
	LISTING_SIZE_CHECKSUM;

}
