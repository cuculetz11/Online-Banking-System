package org.poo.command.debug.error;

import lombok.Getter;

@Getter
public class NotFoundError {
    private final String description;
    private final int timestamp;
    public NotFoundError(final String description, final int timestamp) {
        this.description = description;
        this.timestamp = timestamp;
    }

}
