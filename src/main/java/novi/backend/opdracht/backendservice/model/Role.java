package novi.backend.opdracht.backendservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Collection;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    private String rolename;
    @ManyToMany(mappedBy = "roles")
    private Collectionac<User> users;

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
