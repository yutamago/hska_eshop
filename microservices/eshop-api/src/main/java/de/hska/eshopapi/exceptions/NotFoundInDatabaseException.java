package de.hska.eshopapi.exceptions;

public class NotFoundInDatabaseException extends Exception {
    public NotFoundInDatabaseException(Class dbEntity, Throwable cause) {
        super(dbEntity.getCanonicalName() + " not found in Database!", cause);
    }
}
