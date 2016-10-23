package ismartdev.mn.wishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ismartdev.mn.wishes.model.Lottery;
import ismartdev.mn.wishes.model.RegisteredLottery;
import ismartdev.mn.wishes.model.UserInfo;
import ismartdev.mn.wishes.model.UserLotteries;
import ismartdev.mn.wishes.util.Constants;
import ismartdev.mn.wishes.util.Utils;

public class LotteryRegAC extends BaseActivity {
    private EditText lucky_number;
    private String guid;
    private DatabaseReference mDataReference;
    private DatabaseReference mLotteryReference;
    private DatabaseReference mTimeReference;
    private ValueEventListener mDataListener;
    private String getTimeDate;
    private UserInfo userInfo;
    private DatabaseReference mUserReference;
    private ValueEventListener mUserListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_reg_ac);
        mDataReference = FirebaseDatabase.getInstance().getReference();

        mTimeReference = FirebaseDatabase.getInstance().getReference();
        guid = getIntent().getExtras().getString("id", "");
        mLotteryReference = FirebaseDatabase.getInstance().getReference().child(Constants.main_lotteries).child(guid);
        mUserReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.user_info).child(getUid());


        mTimeReference.child(Constants.now_time).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getTimeDate = dataSnapshot.getValue() + "";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LotteryRegAC.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        lucky_number = (EditText) findViewById(R.id.reg_lucky_number);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        FloatingActionButton reg = (FloatingActionButton) findViewById(R.id.reg_ac_btn);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lucky_number.getText().length() == 9)
                    isLuckyReg(lucky_number.getText() + "");
                else
                    Toast.makeText(LotteryRegAC.this, R.string.enter_lucky_number, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void isLuckyReg(final String number) {

        mDataReference.child(Constants.registered_main_lottery + getTimeDate).child(number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Toast.makeText(LotteryRegAC.this, R.string.already_registered_number, Toast.LENGTH_LONG).show();

                } else {
                    registerLottery(number);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LotteryRegAC.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void registerLottery(String number) {
        RegisteredLottery registeredLottery = new RegisteredLottery(guid, ServerValue.TIMESTAMP, getUid());
        UserLotteries userLotteries = new UserLotteries(number, ServerValue.TIMESTAMP);

        userInfo.shake_count = userInfo.shake_count + 1;
        userInfo.coin_ammount = userInfo.coin_ammount + 100;


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Constants.main_lottery + getTimeDate + "/" + Constants.registered_main_lottery + "/" + number, registeredLottery.toMap());
        childUpdates.put("/" + Constants.main_lotteries + "/" + guid+"/"+Constants.registered_timestamp, ServerValue.TIMESTAMP);
        childUpdates.put("/" + Constants.user_lotteries + "/" + getUid() + "/" + guid, userLotteries.toMap());
        childUpdates.put("/" + Constants.user_info + "/" + getUid(), userInfo.toMap());

        mDataReference.updateChildren(childUpdates);
        mLotteryReference.removeEventListener(mDataListener);
        Toast.makeText(LotteryRegAC.this,R.string.successfully_reg,Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener lotteryListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Lottery lot = dataSnapshot.getValue(Lottery.class);
                Log.e("onDataChange", lot.registered_timestamp + ""+lot.timestamp);
                if (lot != null) {
                    if (lot.registered_timestamp > 0) {
                        Toast.makeText(getApplicationContext(), R.string.already_reg_lottery, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Lottery", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mLotteryReference.addValueEventListener(lotteryListener);
        mDataListener = lotteryListener;


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                UserInfo user = dataSnapshot.getValue(UserInfo.class);

                if (user != null) {
                    userInfo = user;
                } else {
                    Toast.makeText(LotteryRegAC.this, R.string.error_user, Toast.LENGTH_LONG).show();
                    Utils.logout(LotteryRegAC.this);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(LotteryRegAC.this, databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        };
        mUserReference.addValueEventListener(userListener);

        mUserListener = userListener;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mDataListener != null) {
            mLotteryReference.removeEventListener(mDataListener);
        }

        if (mUserListener != null) {
            mUserReference.removeEventListener(mUserListener);
        }


    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return false;
        }
    }
}
