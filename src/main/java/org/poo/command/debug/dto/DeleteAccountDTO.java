package org.poo.command.debug.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DeleteAccountDTO implements AccountDeleteInfo {
    private final String success;
    @Setter
    private int timestamp;

    public DeleteAccountDTO(final String succes, final int timestamp) {
        this.success = succes;
        this.timestamp = timestamp;
    }

}
