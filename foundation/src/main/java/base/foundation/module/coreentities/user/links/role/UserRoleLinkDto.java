package base.foundation.module.coreentities.user.links.role;

import base.foundation.module.coreentities.user.User;
import base.utility.module.utilities.entities.BasicLinkDto;
import org.hibernate.annotations.Immutable;


@Immutable
public class UserRoleLinkDto extends BasicLinkDto {

    public static final String TABLE_NAME = "user_role_link";
    public static final String ENTITY_NAME = "User Role link";

    private Long userId;
    private Long publishingHouseId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return publishingHouseId;
    }

    public void setRoleId(Long publishingHouseId) {
        this.publishingHouseId = publishingHouseId;
    }

    // Getter and setter

    // To string for log
    @Override
    public String toString(){

        return "Dto: Role - id = " + getId() + " userId =  " + getUserId() + " publishingHouseId" + getRoleId() +  " insertDate =  " + getInsertDate() + " lastUpdatedDate = " + getLastUpdatedDate();
    }

    public UserRoleLinkDto(){
    }

    public UserRoleLinkDto(String message){
        this.setMessage(message);
        this.setId(null);
    }

    public UserRoleLinkDto(UserRoleLink userRoleLink){
        this.setRoleId(userRoleLink.getRole().getId());
        this.setUserId(userRoleLink.getUser().getId());
        this.setInsertDate(userRoleLink.getInsertDate());
        this.setLastUpdatedDate(userRoleLink.getLastUpdatedDate());
    }

    public String obtainEntityName(){
        return User.ENTITY_NAME;
    }
}
