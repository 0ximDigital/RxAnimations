package oxim.digital.rxanimations;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import oxim.digital.rxanim.RxAnimations;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 2000;
    private static final int ANIMATION_CANCEL_POINT = 1000;

    @Bind(R.id.top_sample_view)
    View topSampleView;

    @Bind(R.id.bottom_sample_view)
    View bottomSampleView;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.root_view)
    public void animateSampleView() {
        final Subscription animateSubscription = RxAnimations.enterWithRotation(topSampleView, ANIMATION_DURATION, 200, 200, 100, 80).subscribe();
        final Subscription animateSubscriptionToCancel = RxAnimations.enterWithRotation(bottomSampleView, ANIMATION_DURATION, 200, 200, 100, 80).subscribe();

        handler.postDelayed(animateSubscriptionToCancel::unsubscribe, ANIMATION_CANCEL_POINT);
    }
}
