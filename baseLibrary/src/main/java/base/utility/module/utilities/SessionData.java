package base.utility.module.utilities;

import base.utility.module.utilities.entities.BasicSessionData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.util.Objects.isNull;

public class SessionData extends BasicSessionData {

    public SessionData(String username, String companyCode, Long expireToken, String languageCode, List<String> roleList) {
        this.setUsername(username);
        this.setCompanyCode(companyCode);
        this.setExpireToken(expireToken);
        this.setLanguageCode(languageCode);
        this.setRolesList(roleList);
    }

    public SessionData(String username, String companyCode, Long expireToken, String languageCode) {
        this.setUsername(username);
        this.setCompanyCode(companyCode);
        this.setExpireToken(expireToken);
        this.setLanguageCode(languageCode);
    }

    @Override
    public String toString() {
        String expirationDate;
        SimpleDateFormat dateFor = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        if (!isNull(getExpireToken())){
            expirationDate = dateFor.format(new Date(getExpireToken()));
        } else {
            expirationDate = "";
        }
        return "Session Data - Username: " + getUsername() + " Company Code: " + getCompanyCode() + " Token Expiration: " + expirationDate + " Language Code: " + getLanguageCode();
    }
}
