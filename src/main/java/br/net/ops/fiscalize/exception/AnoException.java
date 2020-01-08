package br.net.ops.fiscalize.exception;

import java.util.logging.Level;

@SuppressWarnings("serial")
public class AnoException extends ExceptionBase {

    private static final String MENSAGEM = "Problemas ao resgatar a coluna Ano (numAno)!";

    public AnoException() {
        super(MENSAGEM);
        logger.log(Level.SEVERE, MENSAGEM);
    }

    public AnoException(Throwable e) {
        super(MENSAGEM);
        logger.log(Level.SEVERE, MENSAGEM);
        logger.log(Level.SEVERE, e.getMessage());
    }

}
