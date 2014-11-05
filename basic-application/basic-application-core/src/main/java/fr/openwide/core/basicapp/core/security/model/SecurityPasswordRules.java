package fr.openwide.core.basicapp.core.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.IllegalCharacterRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.NumericalSequenceRule;
import edu.vt.middleware.password.RegexRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.WhitespaceRule;
import fr.openwide.core.basicapp.core.business.user.model.User;

public class SecurityPasswordRules implements Serializable {

	private static final long serialVersionUID = -2309617143631151956L;

	private Set<Rule> rules = new HashSet<Rule>();

	public static final SecurityPasswordRules DEFAULT = new SecurityPasswordRules()
			.minMaxLength(User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH);

	public SecurityPasswordRules minLength(int min) {
		rules.add(new LengthRule(min, Integer.MAX_VALUE));
		return this;
	}

	public SecurityPasswordRules maxLength(int max) {
		rules.add(new LengthRule(0, max));
		return this;
	}

	public SecurityPasswordRules minMaxLength(int min, int max) {
		rules.add(new LengthRule(min, max));
		return this;
	}

	public SecurityPasswordRules mandatoryDigits(int min) {
		rules.add(new DigitCharacterRule(min));
		return this;
	}

	public SecurityPasswordRules mandatoryDigits() {
		rules.add(new DigitCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryNonAlphanumericCharacters() {
		rules.add(new NonAlphanumericCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryNonAlphanumericCharacters(int min) {
		rules.add(new NonAlphanumericCharacterRule(min));
		return this;
	}

	public SecurityPasswordRules mandatoryUpperCase() {
		rules.add(new UppercaseCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryUpperCase(int upperCase) {
		rules.add(new UppercaseCharacterRule(upperCase));
		return this;
	}

	public SecurityPasswordRules mandatoryLowerCase() {
		rules.add(new LowercaseCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryLowerCase(int minLowerCase) {
		rules.add(new LowercaseCharacterRule(minLowerCase));
		return this;
	}

	public SecurityPasswordRules mandatoryUpperLowerCase() {
		rules.add(new LowercaseCharacterRule());
		rules.add(new UppercaseCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryUpperLowerCase(int minUpperCase, int minLowerCase) {
		rules.add(new UppercaseCharacterRule(minUpperCase));
		rules.add(new LowercaseCharacterRule(minLowerCase));
		return this;
	}

	public SecurityPasswordRules forbiddenWhiteSpace() {
		rules.add(new WhitespaceRule());
		return this;
	}

	public SecurityPasswordRules forbiddenRegex(String regex) {
		rules.add(new RegexRule(regex));
		return this;
	}

	public SecurityPasswordRules forbiddenOrderedNumericalSequence(int sequenceMaxLength, boolean wrap) {
		rules.add(new NumericalSequenceRule(sequenceMaxLength, wrap));
		return this;
	}

	public SecurityPasswordRules forbiddenCharacters(String characters) {
		rules.add(new IllegalCharacterRule(characters.toCharArray()));
		return this;
	}

	public Set<Rule> getRules() {
		return rules;
	}

}
