package br.net.ops.fiscalize.exception;

import java.util.logging.Level;

@SuppressWarnings("serial")
public class ValorLiquidoException extends ExceptionBase {

	private static final String MENSAGEM = "Problemas ao resgatar a coluna Valor Líquido (vlrLiquido)!";
	
	public ValorLiquidoException() {
		super(MENSAGEM);
		logger.log(Level.SEVERE, MENSAGEM);
	}
	
	public ValorLiquidoException(Throwable e) {
		super(MENSAGEM);
		logger.log(Level.SEVERE, MENSAGEM);
		logger.log(Level.SEVERE, e.getMessage());
	}
	
}
