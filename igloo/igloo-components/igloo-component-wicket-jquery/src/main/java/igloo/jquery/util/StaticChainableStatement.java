package igloo.jquery.util;

import org.wicketstuff.wiquery.core.javascript.ChainableStatement;

/**
 * Classe permettant d'exploiter de manière statique un {@link ChainableStatement} existant. Les
 * valeurs sont générées au moment de l'initialisation.
 *
 * <p>Ne doit être utilisé que dans les situations où le contenu généré n'est pas susceptible de
 * changer.
 */
public class StaticChainableStatement implements ChainableStatement {

  private final String chainLabel;

  private final CharSequence[] statementArgs;

  public StaticChainableStatement(ChainableStatement chainableStatement) {
    super();
    this.chainLabel = chainableStatement.chainLabel();
    this.statementArgs = chainableStatement.statementArgs();
  }

  @Override
  public String chainLabel() {
    return chainLabel;
  }

  @Override
  public CharSequence[] statementArgs() {
    return statementArgs; // NOSONAR
  }
}
