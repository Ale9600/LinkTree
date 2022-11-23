package base.utility.module.utilities.entities;


import base.utility.module.utilities.responses.ResponseConstants;

import java.util.Date;

public abstract class BasicI18NDto {

    private String descrizione;

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}