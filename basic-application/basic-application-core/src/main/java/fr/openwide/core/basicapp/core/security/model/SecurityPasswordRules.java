package fr.openwide.core.basicapp.core.security.model;

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

public class SecurityPasswordRules {

	private Set<Rule> rules = new HashSet<Rule>();

	public static final SecurityPasswordRules DEFAULT = new SecurityPasswordRules()
			.minMaxLength(User.MIN_PASSWORD_LENGTH, User.MAX_PASSWORD_LENGTH);

	public SecurityPasswordRules minLength(int min) {
		rulesSet.add(new LengthRule(min, Integer.MAX_VALUE));
		return this;
	}

	public SecurityPasswordRules maxLength(int max) {
		rulesSet.add(new LengthRule(0, max));
		return this;
	}

	public SecurityPasswordRules minMaxLength(int min, int max) {
		rulesSet.add(new LengthRule(min, max));
		return this;
	}

	public SecurityPasswordRules mandatoryDigits(int min) {
		rulesSet.add(new DigitCharacterRule(min));
		return this;
	}

	public SecurityPasswordRules mandatoryDigits() {
		rulesSet.add(new DigitCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryNonAlphanumericCharacters() {
		rulesSet.add(new NonAlphanumericCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryNonAlphanumericCharacters(int min) {
		rulesSet.add(new NonAlphanumericCharacterRule(min));
		return this;
	}

	public SecurityPasswordRules mandatoryUpperCase() {
		rulesSet.add(new UppercaseCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryUpperCase(int upperCase) {
		rulesSet.add(new UppercaseCharacterRule(upperCase));
		return this;
	}

	public SecurityPasswordRules mandatoryLowerCase() {
		rulesSet.add(new LowercaseCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryLowerCase(int minLowerCase) {
		rulesSet.add(new LowercaseCharacterRule(minLowerCase));
		return this;
	}

	public SecurityPasswordRules mandatoryUpperLowerCase() {
		rulesSet.add(new LowercaseCharacterRule());
		rulesSet.add(new UppercaseCharacterRule());
		return this;
	}

	public SecurityPasswordRules mandatoryUpperLowerCase(int minUpperCase, int minLowerCase) {
		rulesSet.add(new UppercaseCharacterRule(minUpperCase));
		rulesSet.add(new LowercaseCharacterRule(minLowerCase));
		return this;
	}

	public SecurityPasswordRules forbiddenWhiteSpace() {
		rulesSet.add(new WhitespaceRule());
		return this;
	}

	public SecurityPasswordRules regex(String regexString) {
		rulesSet.add(new RegexRule(regexString));
		return this;
	}

	public SecurityPasswordRules forbiddenOrderedNumericalSequence(int sequenceMaxLength, boolean wrap) {
		rulesSet.add(new NumericalSequenceRule(sequenceMaxLength, wrap));
		return this;
	}

	public SecurityPasswordRules forbiddenCharacters(char... characters) {
		rulesSet.add(new IllegalCharacterRule(characters));
		return this;
	}

	public Set<Rule> getRules() {
		return rules;
	}

}
