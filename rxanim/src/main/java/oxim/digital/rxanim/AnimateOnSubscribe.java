package oxim.digital.rxanim;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Completable;
import rx.CompletableSubscriber;
import rx.Subscription;
import rx.functions.Action1;

public final class AnimateOnSubscribe implements Completable.OnSubscribe {

    private static final int NONE = 0;

    private final WeakReference<View> viewWeakRef;

    private final List<Action1<ViewPropertyAnimatorCompat>> preTransformActions;
    private final List<Action1<ViewPropertyAnimatorCompat>> animationActions;

    private final Action1<View> onAnimationCancelAction;

    public AnimateOnSubscribe(final WeakReference<View> viewWeakRef, final List<Action1<ViewPropertyAnimatorCompat>> animationActions) {
        this(viewWeakRef, null, animationActions, view -> {});
    }

    public AnimateOnSubscribe(final WeakReference<View> viewWeakRef, @Nullable final List<Action1<ViewPropertyAnimatorCompat>> preAnimationActions,
                              final List<Action1<ViewPropertyAnimatorCompat>> animationActions,
                              final Action1<View> onAnimationCancelAction) {
        this.viewWeakRef = viewWeakRef;
        this.preTransformActions = preAnimationActions;
        this.animationActions = animationActions;
        this.onAnimationCancelAction = onAnimationCancelAction;
    }

    @Override
    public void call(final CompletableSubscriber completableSubscriber) {
        final View view = viewWeakRef.get();
        if (view == null) {
            completableSubscriber.onCompleted();
            return;
        }

        final ViewPropertyAnimatorCompat animator = ViewCompat.animate(view);
        completableSubscriber.onSubscribe(createClearSubscription(animator));

        if (preTransformActions != null) {
            applyActions(preTransformActions, animator);
            animator.setDuration(NONE).setStartDelay(NONE)
                    .withEndAction(() -> runAnimation(completableSubscriber, animator))
                    .start();
        } else {
            runAnimation(completableSubscriber, animator);
        }
    }

    private void applyActions(final List<Action1<ViewPropertyAnimatorCompat>> actions, final ViewPropertyAnimatorCompat animator) {
        for (final Action1<ViewPropertyAnimatorCompat> action : actions) {
            action.call(animator);
        }
    }

    private void runAnimation(final CompletableSubscriber completableSubscriber, final ViewPropertyAnimatorCompat animator) {
        applyActions(animationActions, animator);
        animator.withEndAction(completableSubscriber::onCompleted)
                .start();
    }

    private Subscription createClearSubscription(final ViewPropertyAnimatorCompat animator) {
        return new ClearSubscription(() -> {
            animator.setListener(new ViewPropertyAnimatorListenerAdapter() {

                @Override
                public void onAnimationCancel(final View view) {
                    onAnimationCancelAction.call(view);
                }
            });
            animator.cancel();
            animator.setListener(null);
        });
    }
}
