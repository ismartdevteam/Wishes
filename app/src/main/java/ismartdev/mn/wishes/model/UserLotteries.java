package ismartdev.mn.wishes.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ulzii on 7/20/2016.
 */
@IgnoreExtraProperties
public class UserLotteries {
    public String lucky_number;
    public Map<String, String>  timestamp;

    public UserLotteries() {

    }

    public UserLotteries(String lucky_number,  Map<String, String>  timestamp) {
        this.lucky_number = lucky_number;
        this.timestamp = timestamp;


    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lucky_number", lucky_number);
        result.put("timestamp", timestamp);
    

        return result;
    }

}
