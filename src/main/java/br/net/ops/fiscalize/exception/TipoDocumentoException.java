package br.net.ops.fiscalize.exception;

import java.util.logging.Level;

@SuppressWarnings("serial")
public class TipoDocumentoException extends ExceptionBase {

	private static final String MENSAGEM = "Problemas ao resgatar a coluna Tipo Documento Fiscal (indTipoDocumento)!";
	
	public TipoDocumentoException() {
		super(MENSAGEM);
		logger.log(Level.SEVERE, MENSAGEM);
	}
	
	public TipoDocumentoException(Throwable e) {
		super(MENSAGEM);
		logger.log(Level.SEVERE, MENSAGEM);
		logger.log(Level.SEVERE, e.getMessage());
	}
	
}
