package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(AuthorityKey.class)
@Table(name = "authorities")
public class Authority implements Serializable {

    @Id
    @Column(nullable = false)
    private String username;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role authority;

    public Authority() {
    }

    public Authority(String username, Role authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Role getAuthority() {
        return authority;
    }

}