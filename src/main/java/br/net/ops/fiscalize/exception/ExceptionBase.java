package br.net.ops.fiscalize.exception;

import br.net.ops.fiscalize.util.Utilidade;

import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ExceptionBase extends Throwable {

    protected Logger logger = Utilidade.getLogger();

    public ExceptionBase(String mensagem) {
        super(mensagem);
    }

}
