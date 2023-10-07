package com.ilianazz.ai12poc.common.data.user;

import java.io.Serializable;
import java.util.UUID;

public class User extends UserLite implements Serializable {
    private String firstName;
    private String lastName;

    public User(UUID uuid, String pseudo, String firstName, String lastName) {
        super(uuid, pseudo);
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
