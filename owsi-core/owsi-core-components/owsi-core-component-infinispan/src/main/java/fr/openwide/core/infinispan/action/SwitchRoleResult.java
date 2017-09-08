package fr.openwide.core.infinispan.action;

/**
 * A role switch involves a reservation (to preserve role capture during rebalance),
 * then a release request (if needed, by current owner, then a release capture, by target)
 */
public enum SwitchRoleResult {

	/**
	 * step is done with success
	 */
	SWITCH_STEP_SUCCESS,

	/**
	 * Role switch is done
	 */
	SWITCH_SUCCESS,

	/**
	 * Role step inconsistency (state is not as expected)
	 */
	SWITCH_STEP_INCONSISTENCY,

	/**
	 * Role release timeouts
	 */
	SWITCH_RELEASE_TIMEOUT,

	/**
	 * Role not captured by target because not longer available
	 */
	SWITCH_CAPTURE_NOT_AVAILABLE,

	/**
	 * Role capture timeouts
	 */
	SWITCH_CAPTURE_TIMEOUT,

	/**
	 * Unknown error
	 */
	SWITCH_UNKNOWN_ERROR;

}
