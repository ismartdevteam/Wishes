package ismartdev.mn.wishes.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ulzii on 7/20/2016.
 */
@IgnoreExtraProperties
public class UpdateUser {
    public int shake_count=0;
    public double coin_ammount=0.0;

    public UpdateUser() {

    }

    public UpdateUser(double coin_ammount, int shake_count) {
        this.coin_ammount = coin_ammount;
        this.shake_count = shake_count;


    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("shake_count", shake_count);
        result.put("coin_ammount", coin_ammount);


        return result;
    }

}
