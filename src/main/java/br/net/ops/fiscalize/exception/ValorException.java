package br.net.ops.fiscalize.exception;

import java.util.logging.Level;

@SuppressWarnings("serial")
public class ValorException extends ExceptionBase {

	private static final String MENSAGEM = "Problemas ao resgatar a coluna Valor Glosa (vlrGlosa)!";
	
	public ValorException() {
		super(MENSAGEM);
		logger.log(Level.SEVERE, MENSAGEM);
	}
	
	public ValorException(Throwable e) {
		super(MENSAGEM);
		logger.log(Level.SEVERE, MENSAGEM);
		logger.log(Level.SEVERE, e.getMessage());
	}
	
}
