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
public class MainLottery {
    public String date = "";
    public String description = "";
    public String prize_amount ="";
    public List<Sponsor> sponsors = new ArrayList<>();
    public int reg_count = 0;

    public MainLottery() {

    }

    public MainLottery(String date, String description, String prize_amount, int reg_count) {
        this.date = date;
        this.description = description;
        this.prize_amount = prize_amount;
        this.reg_count = reg_count;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("description", description);
        result.put("prize_amount", prize_amount);
        result.put("sponsors", sponsors);
        result.put("reg_count", reg_count);

        return result;
    }

}
