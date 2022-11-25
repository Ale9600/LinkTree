package base.utility.module.utilities.general;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

public class GeneralUtility {

    /**
     * Maps the single result of the query in a key value object
     * @param fields The fields of the query
     * @param result The single record of the query result
     * @return The mapped record
     */
    public static LinkedHashMap<String, Object> map(List<String> fields, Object[] result){
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy/MM/dd hh:mm:ss");
        Date date;
        String dateStr;
        int i = 0;

        for (String field: fields) {
            if (result[i] instanceof Timestamp){
                date = new Date(((Timestamp) result[i]).getTime());
                dateStr = sdf.format(date);
                map.put(field, dateStr);
            }else {
                map.put(field, result[i]);
            }
            i++;
        }

        return map;
    }

    /**
     * Checks if a string is castable to a number
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static boolean isValidEmail(String pEmailAddress) {

        System.out.println("EMAIL " + pEmailAddress);

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return true;
        //!(Pattern.compile(regexPattern).matcher(pEmailAddress).matches());
    }
}
