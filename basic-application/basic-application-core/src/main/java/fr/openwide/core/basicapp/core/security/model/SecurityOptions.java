package fr.openwide.core.basicapp.core.security.model;


public class SecurityOptions {

	private SecurityOptionsMode passwordExpiration = SecurityOptionsMode.DISABLE;

	private SecurityOptionsMode passwordHistory = SecurityOptionsMode.DISABLE;

	private SecurityOptionsMode passwordUserUpdate = SecurityOptionsMode.DISABLE;

	private SecurityOptionsMode passwordAdminUpdate = SecurityOptionsMode.DISABLE;

	private SecurityOptionsMode passwordUserRecovery = SecurityOptionsMode.DISABLE;

	private SecurityOptionsMode passwordAdminRecovery = SecurityOptionsMode.DISABLE;

	private SecurityPasswordRules passwordRules;

	public static SecurityOptions defaultOptions() {
		return new SecurityOptions()
				.passwordAdminRecovery()
				.passwordRules(SecurityPasswordRules.DEFAULT);
	}

	public SecurityOptions passwordExpires() {
		passwordExpiration = SecurityOptionsMode.ENABLE;
		return this;
	}

	public SecurityOptionsMode getPasswordExpiration() {
		return passwordExpiration;
	}

	public SecurityOptions passwordHistory() {
		passwordHistory = SecurityOptionsMode.ENABLE;
		return this;
	}

	public SecurityOptionsMode getPasswordHistory() {
		return passwordHistory;
	}

	public SecurityOptions passwordUserUpdate() {
		passwordUserUpdate = SecurityOptionsMode.ENABLE;
		return this;
	}

	public SecurityOptionsMode getPasswordUserUpdate() {
		return passwordUserUpdate;
	}

	public SecurityOptions passwordAdminUpdate() {
		passwordAdminUpdate = SecurityOptionsMode.ENABLE;
		return this;
	}

	public SecurityOptionsMode getPasswordAdminUpdate() {
		return passwordAdminUpdate;
	}

	public SecurityOptions passwordUserRecovery() {
		passwordUserRecovery = SecurityOptionsMode.ENABLE;
		return this;
	}

	public SecurityOptionsMode getPasswordUserRecovery() {
		return passwordUserRecovery;
	}

	public SecurityOptions passwordAdminRecovery() {
		passwordAdminRecovery = SecurityOptionsMode.ENABLE;
		return this;
	}

	public SecurityOptionsMode getPasswordAdminRecovery() {
		return passwordAdminRecovery;
	}

	public SecurityOptions passwordRules(SecurityPasswordRules passwordRules) {
		this.passwordRules = passwordRules;
		return this;
	}

	public SecurityPasswordRules getPasswordRules() {
		return passwordRules;
	}

	private enum SecurityOptionsMode {
		ENABLE,
		DISABLE;
	}
}
