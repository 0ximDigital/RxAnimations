package oxim.digital.rxanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public final class RxObservableValueAnimator {

    private final ValueAnimator valueAnimator;

    private BehaviorSubject<Object> valueUpdateSubject;

    public static RxObservableValueAnimator from(final ValueAnimator valueAnimator) {
        return new RxObservableValueAnimator(valueAnimator);
    }

    private RxObservableValueAnimator(final ValueAnimator valueAnimator) {
        this.valueAnimator = valueAnimator;
        this.valueUpdateSubject = BehaviorSubject.create();
        this.valueAnimator.addUpdateListener(animation -> valueUpdateSubject.onNext(animation.getAnimatedValue()));
        this.valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(final Animator animation) {
                valueUpdateSubject.onCompleted();
            }
        });
    }

    public Observable<Object> schedule() {
        checkSubject();
        valueAnimator.start();
        return valueUpdateSubject;
    }

    public Observable<Object> scheduleReversed() {
        checkSubject();
        valueAnimator.reverse();
        return valueUpdateSubject;
    }

    private void checkSubject() {
        if (valueUpdateSubject.hasCompleted() || valueUpdateSubject.hasThrowable()) {
            valueUpdateSubject = BehaviorSubject.create();
        }
    }

    public float getAnimatedFraction() {
        return valueAnimator.getAnimatedFraction();
    }

    public void end() {
        valueAnimator.end();
        valueUpdateSubject.onCompleted();
    }
}

