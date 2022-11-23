package base.foundation.module.coreentities.user.links.role;

import base.foundation.module.coreentities.role.Role;
import base.foundation.module.coreentities.user.User;
import base.utility.module.utilities.entities.BasicLinkEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@Entity(name = "core_user_role_link")
public class UserRoleLink extends BasicLinkEntity{

    public static final String TABLE_NAME = "core_user_role_link";
    public static final String ENTITY_NAME = "User Role link";

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @Override
    public String toString(){

        return "Entity: ROLE - id = " + getId() + " userId =  " + getUser().getId() + " roleId " + getRole().getId() + " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public UserRoleLink(){

    }

    //Queries
    public static final String QUERY_LIST = """
                                                SELECT bk.id as id,
                                                       bk.title as title
                                                FROM %s bk
                                            """;

    public static Map<String, String> queries;
    static{
        queries = new HashMap<>();
        queries.put("list", String.format(QUERY_LIST, getTableName()));
    }

    //GETTER AND SETTER
    public static String getTableName(){
        return TABLE_NAME;
    }

    public static String getEntityName(){
        return ENTITY_NAME;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static Map<String, String> getQueries() {
        return queries;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
