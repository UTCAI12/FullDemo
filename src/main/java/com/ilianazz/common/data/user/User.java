package main.java.com.ilianazz.common.data.user;

import java.io.Serializable;
import java.util.UUID;

public class User extends UserLite implements Serializable {
    private final String firstName;
    private final String lastName;

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
