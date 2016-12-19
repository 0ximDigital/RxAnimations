package oxim.digital.rxanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import rx.Completable;
import rx.functions.Action1;

public final class RxValueAnimator implements Completable.CompletableOnSubscribe {

    private final ValueAnimator valueAnimator;
    private final Action1<ValueAnimator> valueUpdateAction;

    public static RxValueAnimator from(final ValueAnimator valueAnimator, final Action1<ValueAnimator> valueUpdateAction) {
        return new RxValueAnimator(valueAnimator, valueUpdateAction);
    }

    private RxValueAnimator(final ValueAnimator valueAnimator, final Action1<ValueAnimator> valueUpdateAction) {
        this.valueAnimator = valueAnimator;
        this.valueAnimator.addUpdateListener(valueUpdateAction::call);
        this.valueUpdateAction = valueUpdateAction;
    }

    @Override
    public void call(final Completable.CompletableSubscriber completableSubscriber) {
        completableSubscriber.onSubscribe(new ClearSubscription(valueAnimator::end));
        valueAnimator.addUpdateListener(valueUpdateAction::call);
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(final Animator animation) {
                valueAnimator.removeAllListeners();
                completableSubscriber.onCompleted();
            }
        });
    }
}

