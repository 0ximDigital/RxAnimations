package oxim.digital.rxanimations.colorpicker;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class ColorPickerPresenter extends BasePresenter<ColorPickerContract.View> implements ColorPickerContract.Presenter {

    public ColorPickerPresenter() {

    }

    @Override
    public void refreshView() {
        final ColorPickerContract.View view = getNullableView();
        if (view == null) {
            return;
        }

        final Subscription animationSubscription = view.setupInitialAnimation()
                                                       .delay(500, TimeUnit.MILLISECONDS)
                                                       .observeOn(AndroidSchedulers.mainThread())
                                                       .concatWith(view.startInitialAnimation())
                                                       .subscribe(Throwable::printStackTrace, this::onAnimationEnd);

        addSubscription(animationSubscription);
    }

    private void onAnimationEnd() {

    }

}
