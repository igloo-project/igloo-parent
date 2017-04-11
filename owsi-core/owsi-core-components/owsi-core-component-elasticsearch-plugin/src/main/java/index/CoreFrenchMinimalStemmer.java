package index;

/**
 * Stemmer adapté depuis le FrenchMinimalStemmer qui a les particularités
 * suivantes :
 * - on ne désactive le stemming que si la chaîne fait moins de 4
 * caractères et pas 6 (typiquement, problème sur stage et stages, noir et noire)
 * - on enlève la suppression des doublements de lettres en fin de mot pour éviter marmot == marmotte
 * - on arrête le stemming si le mot devient trop court de manière à éviter le phénomène stage/stages sur des mots
 * très courts
 */
public class CoreFrenchMinimalStemmer {

	/**
	 * on analyse les mots dès qu'ils font plus de tant de caractères
	 * 
	 * /!\ certaines règles dépendent de ce choix de longueur : il faut donc faire attention à ne pas le descendre
	 * ou alors il faut intégrer des checks en plus ci-dessous quand on remonte dans les index
	 */
	private static final int MIN_LENGTH_HARD_LIMIT = 5;
	
	private static final int MIN_LENGTH_PLURAL_LIMIT = 4;

	public int stem(char s[], int len) {
		if (len < MIN_LENGTH_HARD_LIMIT) {
			if (len >= MIN_LENGTH_PLURAL_LIMIT) {
				return stemLetter(s, len, 's', MIN_LENGTH_PLURAL_LIMIT);
			}
			return len;
		}

		if (s[len - 1] == 'x') {
			if (s[len - 3] == 'a' && s[len - 2] == 'u' && s[len -4] != 'e') {
				s[len - 2] = 'l';
			}
			return len - 1;
		}
		
		int refLen = len;
		
		len = stemLetter(s, len, 's');
		if (len == refLen) {
			// on ne s'attaque au r que s'il n'y a pas eu remplacement avant
			len = stemLetterIfPreviousLetterIs(s, len, 'r', 'e');
		}
		len = stemLetter(s, len, 'e');
		len = stemLetter(s, len, 'é');

		return len;
	}

	private int stemLetter(char s[], int len, char letter) {
		return stemLetter(s, len, letter, MIN_LENGTH_HARD_LIMIT);
	}
	
	private int stemLetter(char s[], int len, char letter, int limit) {
		if (len >= limit && s[len - 1] == letter) {
			return len - 1;
		}
		return len;
	}
	
	private int stemLetterIfPreviousLetterIs(char s[], int len, char letter, char previousLetter) {
		if (len >= MIN_LENGTH_HARD_LIMIT && s[len - 1] == letter) {
			return len - 1;
		}
		return len;
	}
}