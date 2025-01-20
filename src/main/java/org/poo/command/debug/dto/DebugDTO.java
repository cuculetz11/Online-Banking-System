package org.poo.command.debug.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class DebugDTO<T> {
    private String command;
    private ArrayList<T> output;
    private int timestamp;

    public DebugDTO(final String command, final ArrayList<T> output, final int timestamp) {
        this.command = command;
        this.output = output;
        this.timestamp = timestamp;
    }

}
