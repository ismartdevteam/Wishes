package ismartdev.mn.wishes.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import ismartdev.mn.wishes.LoginActivity;
import ismartdev.mn.wishes.R;

/**
 * Created by Ulzii on 7/20/2016.
 */
public class Utils {
    public static void logout(Context context) {

        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        context.startActivity(new Intent(context, LoginActivity.class));

    }
}
