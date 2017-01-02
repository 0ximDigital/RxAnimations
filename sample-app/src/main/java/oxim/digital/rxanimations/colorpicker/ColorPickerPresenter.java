package oxim.digital.rxanimations.colorpicker;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public final class ColorPickerPresenter extends BasePresenter<ColorPickerContract.View> implements ColorPickerContract.Presenter {

    @Override
    public void refreshView() {
        final ColorPickerContract.View view = getNullableView();
        if (view == null) {
            return;
        }

        final Disposable animationDisposable = view.setupInitialAnimation()
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .concatWith(view.startInitialAnimation())
                .subscribe(this::onAnimationEnd, Throwable::printStackTrace);

        addSubscription(animationDisposable);
    }

    private void onAnimationEnd() {

    }

}
