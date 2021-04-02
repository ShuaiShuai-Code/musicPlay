package bjfu.it.panjue.mirrormusic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/28 0028.
 */

public class StringUtils {
    public static String getTime(int time) {
        String dateStr = "00:00";
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        try {
            Date date = format.parse(dateStr);
            long timestamp = date.getTime() + time;
            return format.format(new Date(timestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "00:00";
    }
}
