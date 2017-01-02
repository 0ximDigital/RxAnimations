package oxim.digital.rxanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Consumer;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

public final class RxValueAnimator extends Completable {

    private final ValueAnimator valueAnimator;
    private final Consumer<ValueAnimator> valueUpdateAction;

    public static RxValueAnimator from(final ValueAnimator valueAnimator, final Consumer<ValueAnimator> valueUpdateAction) {
        return new RxValueAnimator(valueAnimator, valueUpdateAction);
    }

    private RxValueAnimator(final ValueAnimator valueAnimator, final Consumer<ValueAnimator> valueUpdateAction) {
        this.valueAnimator = valueAnimator;
        this.valueUpdateAction = valueUpdateAction;
    }

    @Override
    protected void subscribeActual(CompletableObserver completableObserver) {
        verifyMainThread();
        Listener listener = new Listener(completableObserver, valueAnimator, valueUpdateAction);
        completableObserver.onSubscribe(listener);
        valueAnimator.addUpdateListener(listener);
        valueAnimator.addListener(listener.animatorListener);
        valueAnimator.start();
    }

    private class Listener extends MainThreadDisposable implements ValueAnimator.AnimatorUpdateListener {
        private final CompletableObserver observer;
        private final ValueAnimator animator;
        private final Consumer<ValueAnimator> valueUpdateAction;
        private final AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                animator.removeListener(this);
                observer.onComplete();
            }
        };

        private Listener(CompletableObserver observer, ValueAnimator valueAnimator, Consumer<ValueAnimator> valueUpdateAction) {
            this.observer = observer;
            this.animator = valueAnimator;
            this.valueUpdateAction = valueUpdateAction;
        }

        @Override
        protected void onDispose() {
            animator.removeUpdateListener(this);
            animator.removeListener(animatorListener);
            animator.end();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (!isDisposed()) {
                try {
                    valueUpdateAction.accept(animation);
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }
    }
}

