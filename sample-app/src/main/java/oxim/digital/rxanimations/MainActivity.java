package oxim.digital.rxanimations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import oxim.digital.rxanim.RxAnimationBuilder;

public class MainActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 2000;

    @Bind(R.id.sample_view)
    View sampleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.root_view)
    public void animateSampleView() {
        RxAnimationBuilder.animate(sampleView, ANIMATION_DURATION)
                          .counterRotateBy(60)
                          .fadeIn()
                          .schedule()
                          .subscribe();
    }
}
