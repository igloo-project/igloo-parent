package fr.openwide.core.jpa.security.password.rule;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.passay.CharacterRule;
import org.passay.DictionaryRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalCharacterRule;
import org.passay.IllegalRegexRule;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.Rule;
import org.passay.UsernameRule;
import org.passay.WhitespaceRule;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;

import com.google.common.collect.Lists;

import fr.openwide.core.commons.util.collections.CollectionUtils;

public class SecurityPasswordRules implements Serializable {

	private static final long serialVersionUID = -2309617143631151956L;

	private final Set<Rule> rules = new HashSet<Rule>();

	protected SecurityPasswordRules(Collection<? extends Rule> rules) {
		super();
		CollectionUtils.replaceAll(this.rules, rules);
	}

	public Set<Rule> getRules() {
		return rules;
	}

	public static final Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		
		private Set<Rule> rules = new HashSet<Rule>();
		
		public Builder minLength(int min) {
			rules.add(new LengthRule(min, Integer.MAX_VALUE));
			return this;
		}
		
		public Builder maxLength(int max) {
			rules.add(new LengthRule(0, max));
			return this;
		}
		
		public Builder minMaxLength(int min, int max) {
			rules.add(new LengthRule(min, max));
			return this;
		}
		
		public Builder mandatoryDigits(int min) {
			rules.add(new CharacterRule(EnglishCharacterData.Digit, min));
			return this;
		}
		
		public Builder mandatoryDigits() {
			rules.add(new CharacterRule(EnglishCharacterData.Digit));
			return this;
		}
		
		public Builder mandatoryNonAlphanumericCharacters() {
			rules.add(new CharacterRule(EnglishCharacterData.Special));
			return this;
		}
		
		public Builder mandatoryNonAlphanumericCharacters(int min) {
			rules.add(new CharacterRule(EnglishCharacterData.Special, min));
			return this;
		}
		
		public Builder mandatoryUpperCase() {
			rules.add(new CharacterRule(EnglishCharacterData.UpperCase));
			return this;
		}
		
		public Builder mandatoryUpperCase(int minUpperCase) {
			rules.add(new CharacterRule(EnglishCharacterData.UpperCase, minUpperCase));
			return this;
		}
		
		public Builder mandatoryLowerCase() {
			rules.add(new CharacterRule(EnglishCharacterData.LowerCase));
			return this;
		}
		
		public Builder mandatoryLowerCase(int minLowerCase) {
			rules.add(new CharacterRule(EnglishCharacterData.LowerCase, minLowerCase));
			return this;
		}
		
		public Builder mandatoryUpperLowerCase() {
			rules.add(new CharacterRule(EnglishCharacterData.LowerCase));
			rules.add(new CharacterRule(EnglishCharacterData.UpperCase));
			return this;
		}
		
		public Builder mandatoryUpperLowerCase(int minUpperCase, int minLowerCase) {
			rules.add(new CharacterRule(EnglishCharacterData.UpperCase, minUpperCase));
			rules.add(new CharacterRule(EnglishCharacterData.LowerCase, minLowerCase));
			return this;
		}
		
		public Builder forbiddenWhiteSpace() {
			rules.add(new WhitespaceRule());
			return this;
		}
		
		public Builder forbiddenRegex(String regex) {
			rules.add(new IllegalRegexRule(regex));
			return this;
		}
		
		public Builder forbiddenOrderedNumericalSequence(int sequenceMaxLength, boolean wrap) {
			rules.add(new IllegalSequenceRule(EnglishSequenceData.Numerical, sequenceMaxLength, wrap));
			return this;
		}
		
		public Builder forbiddenCharacters(String characters) {
			rules.add(new IllegalCharacterRule(characters.toCharArray()));
			return this;
		}
		
		public Builder forbiddenUsername() {
			rules.add(new UsernameRule());
			return this;
		}
		
		public Builder forbiddenUsername(boolean matchBackwards, boolean caseInsensitive) {
			rules.add(new UsernameRule(matchBackwards, caseInsensitive));
			return this;
		}
		
		public Builder forbiddenPasswords(List<String> forbiddenPasswords) {
			return forbiddenPasswords(forbiddenPasswords, true);
		}
		
		public Builder forbiddenPasswords(List<String> forbiddenPasswords, boolean caseInsensitive) {
			if (forbiddenPasswords == null || forbiddenPasswords.isEmpty()) {
				return this;
			}
			rules.add(new DictionaryRule(new WordListDictionary(new ArrayWordList(forbiddenPasswords.toArray(new String[forbiddenPasswords.size()]), !caseInsensitive))));
			return this;
		}
		
		public Builder rules(Rule firstRule, Rule... otherRules) {
			return rules(Lists.asList(firstRule, otherRules));
		}
		
		private Builder rules(Collection<Rule> rules) {
			Objects.requireNonNull(rules);
			for (Rule rule : rules) {
				rule(rule);
			}
			return this;
		}
		
		public Builder rule(Rule rule) {
			Objects.requireNonNull(rule);
			rules.add(rule);
			return this;
		}
		
		public SecurityPasswordRules build() {
			return new SecurityPasswordRules(rules);
		}
	}

}
