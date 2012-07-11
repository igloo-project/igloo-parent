package fr.openwide.core.jpa.hibernate.tool;

import java.io.Reader;
import java.util.List;

import org.hibernate.tool.hbm2ddl.ImportScriptException;
import org.hibernate.tool.hbm2ddl.ImportSqlCommandExtractor;

import fr.openwide.core.jpa.hibernate.antlr.ExtendedSqlStatementLexer;
import fr.openwide.core.jpa.hibernate.antlr.ExtendedSqlStatementParser;

public class ExtendedMultipleLinesSqlCommandExtractor implements ImportSqlCommandExtractor {

	private static final long serialVersionUID = -1279470120842345206L;

	@Override
	public String[] extractCommands(Reader reader) {
		final ExtendedSqlStatementLexer lexer = new ExtendedSqlStatementLexer( reader );
		final ExtendedSqlStatementParser parser = new ExtendedSqlStatementParser( lexer );
		try {
			parser.script(); // Parse script.
			parser.throwExceptionIfErrorOccurred();
		}
		catch ( Exception e ) {
			throw new ImportScriptException( "Error during import script parsing.", e );
		}
		List<String> statementList = parser.getStatementList();
		return statementList.toArray( new String[statementList.size()] );
	}

}
