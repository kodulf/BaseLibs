package kodulf.baselibs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kodulf on 16-1-22.
 */
public class DateUtil {
    public static String formateDate(Date date, String pattern) {
        if(StringUtils.isBlank(pattern)){
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formateDate(Date date) {
       return formateDate(date,"");
    }
}
