package br.net.ops.fiscalize.exception;

import java.util.logging.Logger;

import br.net.ops.fiscalize.util.Utilidade;

@SuppressWarnings("serial")
public class ExceptionBase extends Throwable {

	protected Logger logger = Utilidade.getLogger();
	
	public ExceptionBase(String mensagem) {
		super(mensagem);
	}
	
}
