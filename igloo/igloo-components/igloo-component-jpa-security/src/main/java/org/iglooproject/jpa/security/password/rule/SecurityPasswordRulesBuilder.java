package org.iglooproject.jpa.security.password.rule;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.passay.UnicodeString;
import org.passay.data.EnglishCharacterData;
import org.passay.data.EnglishSequenceData;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.passay.rule.CharacterRule;
import org.passay.rule.DictionaryRule;
import org.passay.rule.IllegalCharacterRule;
import org.passay.rule.IllegalRegexRule;
import org.passay.rule.IllegalSequenceRule;
import org.passay.rule.LengthRule;
import org.passay.rule.Rule;
import org.passay.rule.UsernameRule;
import org.passay.rule.WhitespaceRule;

public final class SecurityPasswordRulesBuilder {

  public static final Set<Rule> create(Consumer<SecurityPasswordRulesBuilder> consumer) {
    SecurityPasswordRulesBuilder builder = new SecurityPasswordRulesBuilder();
    consumer.accept(builder);
    return builder.build();
  }

  private final ImmutableSet.Builder<Rule> rules = ImmutableSet.builder();

  private SecurityPasswordRulesBuilder() {}

  private Set<Rule> build() {
    return rules.build();
  }

  public SecurityPasswordRulesBuilder minLength(int min) {
    rules.add(new LengthRule(min, Integer.MAX_VALUE));
    return this;
  }

  public SecurityPasswordRulesBuilder maxLength(int max) {
    rules.add(new LengthRule(0, max));
    return this;
  }

  public SecurityPasswordRulesBuilder minMaxLength(int min, int max) {
    rules.add(new LengthRule(min, max));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryDigits(int min) {
    rules.add(new CharacterRule(EnglishCharacterData.Digit, min));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryDigits() {
    rules.add(new CharacterRule(EnglishCharacterData.Digit));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryNonAlphanumericCharacters() {
    rules.add(new CharacterRule(EnglishCharacterData.Special));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryNonAlphanumericCharacters(int min) {
    rules.add(new CharacterRule(EnglishCharacterData.Special, min));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryUpperCase() {
    rules.add(new CharacterRule(EnglishCharacterData.UpperCase));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryUpperCase(int minUpperCase) {
    rules.add(new CharacterRule(EnglishCharacterData.UpperCase, minUpperCase));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryLowerCase() {
    rules.add(new CharacterRule(EnglishCharacterData.LowerCase));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryLowerCase(int minLowerCase) {
    rules.add(new CharacterRule(EnglishCharacterData.LowerCase, minLowerCase));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryUpperLowerCase() {
    rules.add(new CharacterRule(EnglishCharacterData.LowerCase));
    rules.add(new CharacterRule(EnglishCharacterData.UpperCase));
    return this;
  }

  public SecurityPasswordRulesBuilder mandatoryUpperLowerCase(int minUpperCase, int minLowerCase) {
    rules.add(new CharacterRule(EnglishCharacterData.UpperCase, minUpperCase));
    rules.add(new CharacterRule(EnglishCharacterData.LowerCase, minLowerCase));
    return this;
  }

  public SecurityPasswordRulesBuilder forbiddenWhiteSpace() {
    rules.add(new WhitespaceRule());
    return this;
  }

  public SecurityPasswordRulesBuilder forbiddenRegex(String regex) {
    rules.add(new IllegalRegexRule(regex));
    return this;
  }

  public SecurityPasswordRulesBuilder forbiddenOrderedNumericalSequence(
      int sequenceMaxLength, boolean wrap) {
    rules.add(new IllegalSequenceRule(EnglishSequenceData.Numerical, sequenceMaxLength, wrap));
    return this;
  }

  public SecurityPasswordRulesBuilder forbiddenCharacters(String characters) {
    rules.add(new IllegalCharacterRule(new UnicodeString(characters)));
    return this;
  }

  public SecurityPasswordRulesBuilder forbiddenUsername() {
    rules.add(new UsernameRule());
    return this;
  }

  public SecurityPasswordRulesBuilder forbiddenUsername(
      boolean matchBackwards, boolean caseInsensitive) {
    rules.add(new UsernameRule(matchBackwards, caseInsensitive));
    return this;
  }

  public SecurityPasswordRulesBuilder forbiddenPasswords(List<String> forbiddenPasswords) {
    return forbiddenPasswords(forbiddenPasswords, true);
  }

  public SecurityPasswordRulesBuilder forbiddenPasswords(
      List<String> forbiddenPasswords, boolean caseInsensitive) {
    if (forbiddenPasswords == null || forbiddenPasswords.isEmpty()) {
      return this;
    }
    rules.add(
        new DictionaryRule(
            new WordListDictionary(
                new ArrayWordList(
                    forbiddenPasswords.toArray(new String[forbiddenPasswords.size()]),
                    !caseInsensitive))));
    return this;
  }

  public SecurityPasswordRulesBuilder rules(Rule firstRule, Rule... otherRules) {
    return rules(Lists.asList(firstRule, otherRules));
  }

  private SecurityPasswordRulesBuilder rules(Collection<Rule> rules) {
    Objects.requireNonNull(rules);
    for (Rule rule : rules) {
      rule(rule);
    }
    return this;
  }

  public SecurityPasswordRulesBuilder rule(Rule rule) {
    Objects.requireNonNull(rule);
    rules.add(rule);
    return this;
  }
}
