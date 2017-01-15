package oxim.digital.rxanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import rx.Completable;
import rx.CompletableSubscriber;
import rx.functions.Action1;

public final class RxValueAnimator implements Completable.OnSubscribe {

    private final ValueAnimator valueAnimator;
    private final Action1<ValueAnimator> valueUpdateAction;
    private final Action1<ValueAnimator> animationCancelAction;

    public static RxValueAnimator from(final ValueAnimator valueAnimator, final Action1<ValueAnimator> valueUpdateAction) {
        return new RxValueAnimator(valueAnimator, valueUpdateAction, aValueAnimator -> {});
    }

    public static RxValueAnimator from(final ValueAnimator valueAnimator, final Action1<ValueAnimator> valueUpdateAction,
                                       final Action1<ValueAnimator> animationCancelAction) {
        return new RxValueAnimator(valueAnimator, valueUpdateAction, animationCancelAction);
    }

    private RxValueAnimator(final ValueAnimator valueAnimator, final Action1<ValueAnimator> valueUpdateAction,
                            final Action1<ValueAnimator> animationCancelAction) {
        this.valueAnimator = valueAnimator;
        this.valueAnimator.addUpdateListener(valueUpdateAction::call);
        this.valueUpdateAction = valueUpdateAction;
        this.animationCancelAction = animationCancelAction;
    }

    @Override
    public void call(final CompletableSubscriber completableSubscriber) {
        completableSubscriber.onSubscribe(new ClearSubscription(valueAnimator::end));
        valueAnimator.addUpdateListener(valueUpdateAction::call);
        valueAnimator.start();
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationCancel(final Animator animation) {
                animationCancelAction.call(valueAnimator);
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                valueAnimator.removeAllListeners();
                completableSubscriber.onCompleted();
            }
        });
    }

    public Completable schedule() {
        return Completable.create(this);
    }
}

