package com.thpiffer.myfin.core.exception;

/**
 * Exceção lançada quando um filtro OData é inválido.
 * Pode ocorrer quando:
 * - Um campo não existe na classe de entidade
 * - Um operador não é suportado ou inválido
 * - Um valor não corresponde ao tipo do campo
 */
public class InvalidFilterException extends RuntimeException {

    public InvalidFilterException(String message) {
        super(message);
    }

    public InvalidFilterException(String message, Throwable cause) {
        super(message, cause);
    }

}
