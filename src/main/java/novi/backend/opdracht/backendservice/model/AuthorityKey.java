package novi.backend.opdracht.backendservice.model;

import java.io.Serializable;
import java.util.Objects;

public class AuthorityKey implements Serializable {

    private String username;
    private Role authority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorityKey that = (AuthorityKey) o;
        return Objects.equals(username, that.username) &&
                authority == that.authority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authority);
    }
}
