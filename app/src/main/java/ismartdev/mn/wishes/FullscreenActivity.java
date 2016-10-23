package ismartdev.mn.wishes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.goka.walker.WalkerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ismartdev.mn.wishes.walker.FirstPageFragment;
import ismartdev.mn.wishes.walker.SecondPageFragment;
import ismartdev.mn.wishes.walker.ThirdPageFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private int currentPosition;
    private ImageView leftButton;
    private ImageView rightButton;
    private Button skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //facebook
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("ismartdev.mn.wishes", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            finish();
            startActivity(new Intent(FullscreenActivity.this, MainActivity.class));

        } else {
            setContentView(R.layout.splash);

            skip=(Button)findViewById(R.id.walk_skip_btn);
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(FullscreenActivity.this, LoginActivity.class));
                }
            });
            final WalkerFragment firstPageFragment = FirstPageFragment.newInstance();
            final WalkerFragment secondPageFragment = SecondPageFragment.newInstance();
            final WalkerFragment thirdPageFragment = ThirdPageFragment.newInstance();

            final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position) {
                        case FirstPageFragment.PAGE_POSITION:
                            return firstPageFragment;
                        case SecondPageFragment.PAGE_POSITION:
                            return secondPageFragment;
                        case ThirdPageFragment.PAGE_POSITION:
                            return thirdPageFragment;
                    }
                    return null;
                }

                @Override
                public int getCount() {
                    return 3;
                }
            });

            viewPager.addOnPageChangeListener(firstPageFragment);
            viewPager.addOnPageChangeListener(secondPageFragment);
            viewPager.addOnPageChangeListener(thirdPageFragment);
            viewPager.addOnPageChangeListener(this);

            currentPosition = FirstPageFragment.PAGE_POSITION;
            leftButton = (ImageView) findViewById(R.id.left);
            leftButton.setVisibility(View.GONE);
            rightButton = (ImageView) findViewById(R.id.right);
            leftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(currentPosition - 1, true);
                }
            });
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(currentPosition + 1, true);
                }
            });

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        switch (currentPosition) {
            case FirstPageFragment.PAGE_POSITION:
                leftButton.setVisibility(View.GONE);
                rightButton.setVisibility(View.VISIBLE);
                break;
            case ThirdPageFragment.PAGE_POSITION:
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.GONE);
                break;
            default:
                leftButton.setVisibility(View.VISIBLE);
                rightButton.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
