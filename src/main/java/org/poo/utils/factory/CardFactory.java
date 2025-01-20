package org.poo.utils.factory;

import org.poo.entities.card.Card;
import org.poo.entities.card.ClassicCard;
import org.poo.entities.card.OneTimePayCard;
import org.poo.fileio.CommandInput;

public final class CardFactory {
    private CardFactory() { }

    /**
     * Un mic factory pentru carduri
     * @param input comanda de input ce contine ce tip de card se vrea
     * @return cardul cerut
     */
    public static Card getCard(final CommandInput input) {
        return switch (input.getCommand()) {
            case "createCard" -> new ClassicCard();
            case "createOneTimeCard" -> new OneTimePayCard();
            default -> null;
        };
    }
}
