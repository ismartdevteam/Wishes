package ismartdev.mn.wishes.walker;

import android.graphics.PointF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.felipecsl.gifimageview.library.GifImageView;
import com.goka.walker.WalkerFragment;
import com.goka.walker.WalkerLayout;

import java.util.Arrays;

import ismartdev.mn.wishes.R;

public class SecondPageFragment extends WalkerFragment {

    public static final String TAG = SecondPageFragment.class.getSimpleName();

    public static final int PAGE_POSITION = 1;
    private WalkerLayout walkerLayout;
    private GifImageView imageView;
    public static SecondPageFragment newInstance() {
        Bundle args = new Bundle();
        SecondPageFragment fragment = new SecondPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.second, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=(GifImageView)view.findViewById(R.id.shake_img) ;
        imageView.startAnimation();
        walkerLayout = (WalkerLayout) view.findViewById(R.id.walker);

        walkerLayout.setSpeed(new PointF(1.0f, 0.0f));
        walkerLayout.setSpeedVariance(new PointF(1.2f, 0.0f));
        walkerLayout.setEnableAlphaAnimation(true);
        walkerLayout.setIgnoredViewTags(Arrays.asList("1", "2"));
        walkerLayout.setup();
    }

    @Override
    protected int getPagePosition() {
        return PAGE_POSITION;
    }

    @Override
    protected WalkerLayout getWalkerLayout() {
        return walkerLayout;
    }
}
