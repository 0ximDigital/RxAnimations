package oxim.digital.rxanim;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Consumer;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

public final class AnimateCompletable extends Completable {

    private static final int NONE = 0;

    private final View view;
    private final List<Consumer<ViewPropertyAnimatorCompat>> preTransformActions;
    private final List<Consumer<ViewPropertyAnimatorCompat>> animationActions;

    public AnimateCompletable(View view,
                              @Nullable final List<Consumer<ViewPropertyAnimatorCompat>> preAnimationActions,
                              final List<Consumer<ViewPropertyAnimatorCompat>> animationActions) {
        this.view = view;
        this.preTransformActions = preAnimationActions;
        this.animationActions = animationActions;
    }

    @Override
    protected void subscribeActual(CompletableObserver completableObserver) {
        verifyMainThread();
        final ViewPropertyAnimatorCompat animator = ViewCompat.animate(view);
        Listener listener = new Listener(animator);
        completableObserver.onSubscribe(listener);

        if (preTransformActions != null) {
            for (final Consumer<ViewPropertyAnimatorCompat> action1 : preTransformActions) {
                try {
                    action1.accept(animator);
                } catch (Exception ignore1) {
                }
            }
            animator.setDuration(NONE)
                    .setStartDelay(NONE)
                    .withEndAction(() -> callAnimateActions(completableObserver, animator))
                    .start();
        } else {
            callAnimateActions(completableObserver, animator);
        }
    }

    private void callAnimateActions(CompletableObserver completableObserver, ViewPropertyAnimatorCompat animator) {
        for (final Consumer<ViewPropertyAnimatorCompat> action : animationActions) {
            try {
                action.accept(animator);
            } catch (Exception e) {
                completableObserver.onError(e);
            }
        }
        animator.withEndAction(completableObserver::onComplete).start();
    }


    private class Listener extends MainThreadDisposable {
        private final ViewPropertyAnimatorCompat animator;

        private Listener(ViewPropertyAnimatorCompat propertyAnimator) {
            animator = propertyAnimator;
        }

        @Override
        protected void onDispose() {
            animator.cancel();
        }
    }
}
