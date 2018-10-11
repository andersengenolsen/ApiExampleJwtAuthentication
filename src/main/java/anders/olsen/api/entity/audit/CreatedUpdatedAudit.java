package anders.olsen.api.entity.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

/**
 * Class providing audit columns for entities.
 * <p>
 * Auditing when the object is created, and when it is updated.
 *
 * @author Anders Engen Olsen
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdTime", "updatedTime"},
        allowGetters = true
)
public abstract class CreatedUpdatedAudit implements Serializable {

    /**
     * Time for creating audit column
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdTime;

    /**
     * Last updated audit column
     */
    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedTime;

    /* -- GETTERS & SETTERS -- */

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }
}
