package org.poo.entities;

import lombok.Getter;

import java.util.Objects;

@Getter
public class CurrencyPair {
    private final String from;
    private final String to;

    public CurrencyPair(final String from, final String to) {
        this.from = from;
        this.to = to;
    }

    /**
     *  Se suprascrie metoda equals pentru a compara obiectele
     * @param o obiectul cu care se compara instanta curenta
     * @return adevarat daca sunt la fel, fals altfel
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurrencyPair that = (CurrencyPair) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    /**
     *  Calculeaza un hash pentru acest tip de obiect
     * @return un hash calculat pe baza monedelor de origine si destinatie
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

}
