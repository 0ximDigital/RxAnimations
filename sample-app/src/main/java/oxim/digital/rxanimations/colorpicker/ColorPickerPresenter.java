package oxim.digital.rxanimations.colorpicker;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class ColorPickerPresenter extends BasePresenter<ColorPickerContract.View> implements ColorPickerContract.Presenter {

    private Subscription animationSubscription;

    public ColorPickerPresenter() {

    }

    @Override
    public void refreshView() {
        final ColorPickerContract.View view = getNullableView();
        if (view == null) {
            return;
        }

        if (animationSubscription != null && animationSubscription.isUnsubscribed()) {
            animationSubscription.unsubscribe();
        }

        animationSubscription = view.setupInitialAnimation()
                                    .delay(500, TimeUnit.MILLISECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .concatWith(view.startInitialAnimation())
                                    .subscribe(this::onAnimationEnd, Throwable::printStackTrace);
    }

    private void onAnimationEnd() {

    }
}
