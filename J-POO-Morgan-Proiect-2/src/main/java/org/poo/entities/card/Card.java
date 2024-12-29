package org.poo.entities.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.entities.bankAccount.Account;
import org.poo.utils.Utils;

@Setter
@Getter
public abstract class Card {
    private String cardNumber;
    private String status;
    @JsonIgnore
    private Account account;

    public Card() {
        this.cardNumber = Utils.generateCardNumber();
        this.status = "active";
    }

    /**
     * Metoda utilizata pentru a efectua o plata cu cardul
     * @param amount suma necesara platii
     */
    public abstract void pay(double amount);

}
