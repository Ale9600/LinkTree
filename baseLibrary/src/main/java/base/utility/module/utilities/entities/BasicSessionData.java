package base.utility.module.utilities.entities;

import java.util.List;

public abstract class BasicSessionData {
    private String username;

    private String companyCode = null;

    private Long expireToken;

    private String languageCode;

    private List<String> rolesList;

    public List<String> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<String> rolesList) {
        this.rolesList = rolesList;
    }

    public String getUsername() {
        return username;
    }

    public BasicSessionData() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Long getExpireToken() {
        return expireToken;
    }

    public void setExpireToken(Long expireToken) {
        this.expireToken = expireToken;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
