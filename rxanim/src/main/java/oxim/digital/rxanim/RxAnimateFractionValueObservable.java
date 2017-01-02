package oxim.digital.rxanim;

import android.animation.ValueAnimator;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

public final class RxAnimateFractionValueObservable extends Observable<Object> {

    private final ValueAnimator valueAnimator;
    private final boolean isReversed;

    public static RxAnimateFractionValueObservable from(final ValueAnimator valueAnimator) {
        return new RxAnimateFractionValueObservable(valueAnimator, true);
    }

    public static RxAnimateFractionValueObservable fromReversed(final ValueAnimator valueAnimator) {
        return new RxAnimateFractionValueObservable(valueAnimator, false);
    }

    private RxAnimateFractionValueObservable(final ValueAnimator valueAnimator, boolean isReversed) {
        this.valueAnimator = valueAnimator;
        this.isReversed = isReversed;
    }

    @Override
    protected void subscribeActual(Observer<? super Object> observer) {
        verifyMainThread();
        Listener listener = new Listener(observer, valueAnimator);
        observer.onSubscribe(listener);
        valueAnimator.addUpdateListener(listener);
        startAnimator();
    }

    private void startAnimator() {
        if (isReversed) {
            valueAnimator.reverse();
        } else {
            valueAnimator.start();
        }
    }

    private class Listener extends MainThreadDisposable implements ValueAnimator.AnimatorUpdateListener {
        private final Observer<Object> observer;
        private final ValueAnimator valueAnimator;

        private Listener(Observer<Object> observer, ValueAnimator valueAnimator) {
            this.observer = observer;
            this.valueAnimator = valueAnimator;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (!isDisposed()) {
                observer.onNext(animation.getAnimatedFraction());
            }
        }

        @Override
        protected void onDispose() {
            valueAnimator.removeUpdateListener(this);
            valueAnimator.end();
        }
    }
}

