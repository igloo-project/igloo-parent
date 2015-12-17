package fr.openwide.core.basicapp.core.security.model;

import java.io.Serializable;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.jpa.security.password.rule.SecurityPasswordRules;


public class SecurityOptions implements Serializable {

	private static final long serialVersionUID = -1091828713116815464L;

	private SecurityOptionsMode passwordExpiration = SecurityOptionsMode.DISABLED;

	private SecurityOptionsMode passwordHistory = SecurityOptionsMode.DISABLED;

	private SecurityOptionsMode passwordUserUpdate = SecurityOptionsMode.DISABLED;

	private SecurityOptionsMode passwordAdminUpdate = SecurityOptionsMode.DISABLED;

	private SecurityOptionsMode passwordUserRecovery = SecurityOptionsMode.DISABLED;

	private SecurityOptionsMode passwordAdminRecovery = SecurityOptionsMode.DISABLED;

	private SecurityPasswordRules passwordRules;

	public static final SecurityOptions DEFAULT = new SecurityOptions()
				.passwordAdminRecovery()
				.passwordUserRecovery()
				.passwordAdminUpdate()
				.passwordUserUpdate()
				.passwordRules(
						SecurityPasswordRules.builder()
								.minMaxLength(User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH)
								.build()
				);

	public SecurityOptions passwordExpiration() {
		passwordExpiration = SecurityOptionsMode.ENABLED;
		return this;
	}

	public SecurityOptionsMode getPasswordExpiration() {
		return passwordExpiration;
	}

	public boolean isPasswordExpirationEnabled() {
		return SecurityOptionsMode.ENABLED.equals(getPasswordExpiration());
	}

	public SecurityOptions passwordHistory() {
		passwordHistory = SecurityOptionsMode.ENABLED;
		return this;
	}

	public SecurityOptionsMode getPasswordHistory() {
		return passwordHistory;
	}

	public boolean isPasswordHistoryEnabled() {
		return SecurityOptionsMode.ENABLED.equals(getPasswordHistory());
	}

	public SecurityOptions passwordUserUpdate() {
		passwordUserUpdate = SecurityOptionsMode.ENABLED;
		return this;
	}

	public SecurityOptionsMode getPasswordUserUpdate() {
		return passwordUserUpdate;
	}

	public boolean isPasswordUserUpdateEnabled() {
		return SecurityOptionsMode.ENABLED.equals(getPasswordUserUpdate());
	}

	public SecurityOptions passwordAdminUpdate() {
		passwordAdminUpdate = SecurityOptionsMode.ENABLED;
		return this;
	}

	public SecurityOptionsMode getPasswordAdminUpdate() {
		return passwordAdminUpdate;
	}

	public boolean isPasswordAdminUpdateEnabled() {
		return SecurityOptionsMode.ENABLED.equals(getPasswordAdminUpdate()) || SecurityOptionsMode.DISABLED.equals(getPasswordUserRecovery());
	}

	public SecurityOptions passwordUserRecovery() {
		passwordUserRecovery = SecurityOptionsMode.ENABLED;
		return this;
	}

	public SecurityOptionsMode getPasswordUserRecovery() {
		return passwordUserRecovery;
	}

	public boolean isPasswordUserRecoveryEnabled() {
		return SecurityOptionsMode.ENABLED.equals(getPasswordUserRecovery());
	}

	public SecurityOptions passwordAdminRecovery() {
		passwordAdminRecovery = SecurityOptionsMode.ENABLED;
		return this;
	}

	public SecurityOptionsMode getPasswordAdminRecovery() {
		return passwordAdminRecovery;
	}

	public boolean isPasswordAdminRecoveryEnabled() {
		return SecurityOptionsMode.ENABLED.equals(getPasswordAdminRecovery());
	}

	public SecurityOptions passwordRules(SecurityPasswordRules passwordRules) {
		this.passwordRules = passwordRules;
		return this;
	}

	public SecurityPasswordRules getPasswordRules() {
		return passwordRules;
	}

	private enum SecurityOptionsMode {
		ENABLED,
		DISABLED;
	}
}
