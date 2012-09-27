package fr.openwide.core.jpa.more.util.search.analysis.fr;

/**
 * Stemmer adapté depuis le FrenchMinimalStemmer qui a les particularités
 * suivantes :
 * - on ne désactive le stemming que si la chaîne fait moins de 4
 * caractères et pas 6 (typiquement, problème sur stage et stages, noir et noire)
 * - on enlève la suppression des doublements de lettres en fin de mot pour éviter marmot == marmotte
 * - on arrête le stemming si le mot devient trop court de manière à éviter le phénomène stage/stages sur des mots
 * très courts
 */
public class CoreMinimalFrenchStemmer {

	/**
	 * on analyse les mots dès qu'ils font plus de tant de caractères
	 */
	private static final int MIN_LENGTH = 4;

	public int stem(char s[], int len) {
		if (len < MIN_LENGTH) {
			return len;
		}

		if (s[len - 1] == 'x') {
			if (s[len - 3] == 'a' && s[len - 2] == 'u') {
				s[len - 2] = 'l';
			}
			return len - 1;
		}

		len = stemLetter(s, len, 's');
		len = stemLetter(s, len, 'r');
		len = stemLetter(s, len, 'e');
		len = stemLetter(s, len, 'é');

		return len;
	}

	private int stemLetter(char s[], int len, char letter) {
		if (len >= MIN_LENGTH && s[len - 1] == letter) {
			return len - 1;
		}
		return len;
	}
}