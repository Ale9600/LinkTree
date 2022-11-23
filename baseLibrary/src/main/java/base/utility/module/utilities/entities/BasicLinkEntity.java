package base.utility.module.utilities.entities;


import io.micronaut.data.annotation.DateCreated;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BasicLinkEntity{

    //@Inject
    //@Transient
    //protected EntityUtilityService<T1, R1, T2, R2> entityUtilityService;

    public static String TABLE_NAME;
    public static String ENTITY_NAME;

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    protected Long version = 0L;

    @Temporal(TemporalType.TIMESTAMP)
    @DateCreated
    private Date insertDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastUpdatedDate;

}
