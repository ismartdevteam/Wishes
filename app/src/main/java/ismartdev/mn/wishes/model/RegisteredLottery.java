package ismartdev.mn.wishes.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ulzii on 7/20/2016.
 */
@IgnoreExtraProperties
public class RegisteredLottery {
    public Map<String, String>  timestamp;

    public String lottery_guid;
    public String uid;

    public RegisteredLottery() {

    }

    public RegisteredLottery(String lottery_guid, Map<String, String> timestamp, String uid) {
        this.lottery_guid = lottery_guid;
        this.timestamp = timestamp;
        this.uid = uid;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lottery_guid", lottery_guid);
        result.put("timestamp", timestamp);
        result.put("uid", uid);


        return result;
    }
}
