package org.poo.command.debug.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DebugActionsDTO<T> {
    private String command;
    private T output;
    private int timestamp;

    public DebugActionsDTO(final String command, final T output, final int timestamp) {
        this.command = command;
        this.output = output;
        this.timestamp = timestamp;
    }

}
