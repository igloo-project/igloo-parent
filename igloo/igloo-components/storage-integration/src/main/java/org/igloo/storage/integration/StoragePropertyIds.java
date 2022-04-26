package org.igloo.storage.integration;

import java.util.Set;

import org.igloo.storage.model.atomic.IStorageUnitType;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;

public class StoragePropertyIds extends AbstractPropertyIds {

	public static final ImmutablePropertyId<String> PATH = immutable("storage.path");
	public static final ImmutablePropertyId<Integer> TRANSACTION_SYNCHRONIZATION_ORDER = immutable("storage.transactionSynchronizationOrder");
	public static final ImmutablePropertyId<String> DB_FICHIER_SEQUENCE_NAME = immutable("storage.db.fichierSequenceName");
	public static final ImmutablePropertyId<String> DB_STORAGE_UNIT_SEQUENCE_NAME = immutable("storage.db.storageUnitSequenceName");
	public static final ImmutablePropertyId<Set<IStorageUnitType>> STORAGE_UNIT_TYPE_CANDIDATES = immutable("storage.storageUnitTypeCandidates");

}
