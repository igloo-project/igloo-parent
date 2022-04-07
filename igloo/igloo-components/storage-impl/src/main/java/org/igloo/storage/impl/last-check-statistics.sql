SELECT
	s.id AS storageUnitId,
	s.type AS storageUnitType,
	lastCheck.checkFinishedOn AS "lastOn",
	lastChecksum.checkFinishedOn AS lastChecksumOn,
	extract (epoch from lastCheck.checkFinishedOn - lastCheck.checkStartedOn) * 1000000000 AS lastDuration,
	extract (epoch from lastChecksum.checkFinishedOn - lastChecksum.checkStartedOn) * 1000000000 AS lastChecksumDuration,
	case when lastCheck.checkFinishedOn is not null then extract (epoch from now() - lastCheck.checkFinishedOn) * 1000000000 else null end AS lastAge,
	case when lastChecksum.checkFinishedOn is not null then extract (epoch from now() - lastChecksum.checkFinishedOn) * 1000000000 else null end AS lastChecksumAge
	FROM StorageUnit s
	LEFT JOIN LATERAL (SELECT c.* FROM StorageConsistencyCheck c WHERE c.storageUnit_id = s.id AND c.checkType IN ('LISTING_SIZE', 'LISTING_SIZE_CHECKSUM') AND checkFinishedOn IS NOT NULL ORDER BY checkFinishedOn DESC LIMIT 1) lastCheck ON true
	LEFT JOIN LATERAL (SELECT c.* FROM StorageConsistencyCheck c WHERE c.storageUnit_id = s.id AND c.checkType IN ('LISTING_SIZE_CHECKSUM') AND checkFinishedOn IS NOT NULL ORDER BY checkFinishedOn DESC LIMIT 1) lastChecksum ON TRUE
ORDER BY
	s.id ASC, s.type ASC