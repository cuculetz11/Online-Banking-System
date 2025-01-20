package org.poo.entities.users;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.UserInput;

import java.util.*;

@Setter
@Getter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String birthDate;
    @JsonIgnore
    private String occupation;
    @JsonIgnore
    private UserPlan plan;
    private Map<String, Account> accounts;

    public User(final UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        this.birthDate = userInput.getBirthDate();
        this.occupation = userInput.getOccupation();
        this.plan = new UserPlan(occupation.equals("student") ? "student" : "standard");
        this.accounts = new LinkedHashMap<>();
    }

    /**
     * Retruneaza o lista de conturi
     * @return lista de conturi
     */
    @JsonGetter("accounts")
    public List<Account> getAccountsAsList() {
        return new ArrayList<>(new LinkedHashSet<>(accounts.values()));
    }

}
