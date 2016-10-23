package ismartdev.mn.wishes.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ulzii on 7/20/2016.
 */
@IgnoreExtraProperties
public class Sponsor {

    public String name;
    public String image_url;

    public Sponsor(){

    }

    public Sponsor(String image_url, String name) {
        this.image_url = image_url;
        this.name = name;
    }
}
