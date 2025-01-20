package org.poo.command.debug.error;

import lombok.Getter;

@Getter
public class ErrorCommand {
    private final  String error;

    public ErrorCommand(final String error) {
        this.error = error;
    }
}
