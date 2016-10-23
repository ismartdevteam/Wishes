package ismartdev.mn.wishes.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ulzii on 7/20/2016.
 */
@IgnoreExtraProperties
public class UserInfo {
    public int coin_ammount=0;
    public int shake_count=0;
    public double won_ammount=0.0;
//    public List<BankAccount> bank_accounts = new ArrayList<>();
//    public List<LoginHistory> loginHistories = new ArrayList<>();
    public int status=0;
    public UserInfo() {

    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("coin_ammount", coin_ammount);
        result.put("won_ammount", won_ammount);
        result.put("shake_count", shake_count);

//        result.put("bank_accounts", bank_accounts);
//        result.put("login_histories", loginHistories);

        return result;
    }

}
