package anders.olsen.api.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * Role entity, repsenting roles a {@link User} can have.
 * Roles represented by enums, {@link RoleName}
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * NaturalId, for lookup by RoleName
     *
     * @see RoleName
     */
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;

    /* -- GETTERS AND SETTERS -- */

    public Role(RoleName name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}
