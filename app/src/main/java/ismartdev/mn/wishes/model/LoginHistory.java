package ismartdev.mn.wishes.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ulzii on 7/20/2016.
 */
@IgnoreExtraProperties
public class LoginHistory {
    public Map<String, String>  timestamp;
    public String imei;
    public String uid;

    public  LoginHistory(){

    }
    public LoginHistory(String uid,Map<String, String> timestamp, String imei){
            this.uid=uid;
        this.timestamp=timestamp;
        this.imei=imei;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("timestamp", timestamp);

        result.put("imei", imei);

        return result;
    }
}
