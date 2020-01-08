package br.net.ops.fiscalize.exception;

import java.util.logging.Level;

@SuppressWarnings("serial")
public class MesException extends ExceptionBase {

    private static final String MENSAGEM = "Problemas ao resgatar a coluna Mes (numAno)!";

    public MesException() {
        super(MENSAGEM);
        logger.log(Level.SEVERE, MENSAGEM);
    }

    public MesException(Throwable e) {
        super(MENSAGEM);
        logger.log(Level.SEVERE, MENSAGEM);
        logger.log(Level.SEVERE, e.getMessage());
    }

}
