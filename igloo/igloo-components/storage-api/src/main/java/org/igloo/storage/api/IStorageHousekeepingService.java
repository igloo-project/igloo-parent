package org.igloo.storage.api;

public interface IStorageHousekeepingService {

	/**
	 * Perform housekeeping job:
	 * <ul>
	 * <li>Check storage units consistency (each storage unit can disable or delay consistency check as needed).</li>
	 * <li>Perform overflowing storage unit creation (each storage unit can disable automatic switch).</li>
	 * </ul>
	 */
	void housekeeping();

}