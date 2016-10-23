package ismartdev.mn.wishes.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ulzii on 7/20/2016.
 */
@IgnoreExtraProperties
public class BankAccount {

    public String key;
    public String account_number;
    public String holder_name;

    public  BankAccount(){

    }
    public BankAccount(String key,String account_number,String holder_name){
        this.key=key;
        this.account_number=account_number;
        this.holder_name=holder_name;

    }

}
