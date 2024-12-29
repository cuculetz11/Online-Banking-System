package org.poo.utils;

import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.command.debug.error.ErrorCommand;
import org.poo.command.debug.error.NotFoundError;

public final class ErrorManager {
    private ErrorManager() { }
    /**
     * Adauga in output eroarea ce arata ca nu s-a gasit in banca lucrul pe care-l cautam
     * @param description descrierea specifica erorii
     * @param command numele comenzii
     * @param timestamp timpul cand comanda a avut loc
     */
    public static void notFound(final String description, final String command,
                                final int timestamp) {
        DebugActionsDTO<NotFoundError> notFound = new DebugActionsDTO<>(command,
                new NotFoundError(description, timestamp), timestamp);

        JsonOutManager.getInstance().addToOutput(notFound);
    }

    /**
     * Adauga in output eroarea ce arata ca tipul contului selectat nu e corect
     * @param command comanda ce a fost executata
     * @param timestamp timpul cand a aparut eroarea
     */
    public static void wrongType(final String command, final int timestamp) {
        DebugActionsDTO<ErrorCommand> error = new DebugActionsDTO<>(command,
                new ErrorCommand(Constants.NOT_CLASSIC_ACCOUNT), timestamp);

        JsonOutManager.getInstance().addToOutput(error);
    }
}
