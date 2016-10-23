package ismartdev.mn.wishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;

import ismartdev.mn.wishes.model.Lottery;
import ismartdev.mn.wishes.model.MainLottery;
import ismartdev.mn.wishes.model.Sponsor;
import ismartdev.mn.wishes.model.UserInfo;
import ismartdev.mn.wishes.util.CircleImageView;
import ismartdev.mn.wishes.util.Constants;
import ismartdev.mn.wishes.util.MySingleton;
import ismartdev.mn.wishes.util.Utils;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ValueEventListener mUserListener;
    private DatabaseReference mUserReference;
    private DatabaseReference mDataRef;
    private String TAG = "MainActivity";
    private NavigationView navigationView;
    private TextView budgetTv;
    private TextView nameTv;
    private CircleImageView image;
    private TextView coinTv;
    private ImageLoader imageLoader;
    private String getTimeDate;
    private TextView prizeTv;
    private LinearLayout sponsorsSc;
    private TextView dateTv;
    private TextView countTv;
    private TextView descTv;
    SimpleDateFormat formatter = new SimpleDateFormat("yyMM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageLoader = MySingleton.getInstance(this).getImageLoader();
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.user_info).child(getUid());
        mDataRef = FirebaseDatabase.getInstance().getReference();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton qrcodeBtn = (FloatingActionButton) findViewById(R.id.main_qr_scan_btn);
        qrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(MainActivity.this).setOrientationLocked(false).setCaptureActivity(QrCodeReader.class).initiateScan();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        prizeTv = (TextView) findViewById(R.id.main_prize_tv);
        dateTv = (TextView) findViewById(R.id.main_lottery_date_tv);
        countTv = (TextView) findViewById(R.id.main_reg_count_tv);
        descTv = (TextView) findViewById(R.id.main_desc_tv);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerVw = navigationView.getHeaderView(0);
        image = (CircleImageView) headerVw.findViewById(R.id.nav_user_img);
        sponsorsSc = (LinearLayout) findViewById(R.id.main_sponsors_sv);
        nameTv = (TextView) headerVw.findViewById(R.id.nav_username);
        budgetTv = (TextView) headerVw.findViewById(R.id.nav_user_budget);
        coinTv = (TextView) headerVw.findViewById(R.id.nav_user_coin);

//        updateNavView(new UserInfo());

        makeMainView();
    }

    private void makeMainView() {


        mDataRef.child(Constants.now_time).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getTimeDate = dataSnapshot.getValue() + "";

                getMainLottery();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getMainLottery() {

        mDataRef.child(Constants.main_lottery + getTimeDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MainLottery mainLottery = dataSnapshot.getValue(MainLottery.class);
                if (mainLottery != null) {
                    makeMainLotteryView(mainLottery);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, R.string.error_user, Toast.LENGTH_LONG).show();
            }
        });


    }

    private void makeMainLotteryView(MainLottery lottery) {
        prizeTv.setText(lottery.prize_amount);
        dateTv.setText(lottery.date);
        countTv.setText(getString(R.string.count_reg_lottery, lottery.reg_count));
        descTv.setText(lottery.description);
        sponsorsSc.removeAllViews();
        for (Sponsor sponsor : lottery.sponsors) {
            View spVw = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_sponsor, null);
            CircleImageView image = (CircleImageView) spVw.findViewById(R.id.sponsor_item_image);
            TextView name = (TextView) spVw.findViewById(R.id.sponsor_item_text);
            image.setImageUrl(sponsor.image_url, imageLoader);
            name.setText(sponsor.name);
            sponsorsSc.addView(spVw);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                UserInfo user = dataSnapshot.getValue(UserInfo.class);

                // [START_EXCLUDE]
                if (user != null) {
                    if (user.status == 0)
                        updateNavView(user);
                } else {
                    Toast.makeText(MainActivity.this, R.string.error_user,Toast.LENGTH_LONG).show();
                    Utils.logout(MainActivity.this);
                    finish();
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(MainActivity.this, databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mUserReference.addValueEventListener(userListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mUserListener = userListener;


    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mUserListener != null) {
            mUserReference.removeEventListener(mUserListener);
        }


    }

    private void updateNavView(UserInfo userInfo) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getDisplayName() != null)
                nameTv.setText("dsa");
            else
                nameTv.setText(user.getUid());
            if (user.getProviderData().size() > 0) {
                for (com.google.firebase.auth.UserInfo profile : user.getProviderData()) {
                    // Id of the provider (ex: google.com)
                    String providerId = profile.getProviderId();

                    // UID specific to the provider
                    String uid = profile.getUid();

                    // Name, email address, and profile photo Url
                    String name = profile.getDisplayName();
                    String email = profile.getEmail();
                    String url = profile.getPhotoUrl().toString();
                    nameTv.setText(name);
                    image.setImageUrl(url, imageLoader);

                }

            }
            budgetTv.setText("$" + userInfo.won_ammount);
            coinTv.setText(userInfo.coin_ammount + " " + getString(R.string.coin));
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_watch_video) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu_my_lotteries) {
            // Handle the camera action
        } else if (id == R.id.nav_menu_gift_lib) {

        } else if (id == R.id.nav_menu_winners) {

        } else if (id == R.id.nav_menu_buy_lottery) {

        } else if (id == R.id.nav_menu_ads_videos) {

        } else if (id == R.id.nav_menu_gift_card) {

        } else if (id == R.id.nav_menu_profile) {

        } else if (id == R.id.nav_menu_about) {

        } else if (id == R.id.nav_menu_share) {

        } else if (id == R.id.nav_menu_logout) {
            Utils.logout(MainActivity.this);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MainActivity.this,R.string.error_qrcode_read,Toast.LENGTH_LONG).show();
                Log.d("MainActivity", "Cancelled scan");
            } else {
                Log.d("MainActivity", "Scanned");

                mDataRef.child(Constants.main_lotteries).child(result.getContents()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Lottery lottery = dataSnapshot.getValue(Lottery.class);
                        if (lottery != null) {
                            Log.e("registered lottery", lottery.registered_timestamp + "");
                            if (lottery.registered_timestamp >0)
                                Toast.makeText(getApplicationContext(), R.string.already_reg_lottery, Toast.LENGTH_LONG).show();
                            else {
                                Bundle b = new Bundle();
                                b.putString("id", result.getContents());
                                Intent reqIntent = new Intent(MainActivity.this, LotteryRegAC.class);
                                reqIntent.putExtras(b);
                                startActivity(reqIntent);
                            }
                        } else
                            Toast.makeText(getApplicationContext(), R.string.no_lottery, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), R.string.error_try_again, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
