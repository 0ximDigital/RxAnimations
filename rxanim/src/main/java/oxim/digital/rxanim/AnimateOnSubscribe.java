package oxim.digital.rxanim;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Completable;
import rx.functions.Action1;

public final class AnimateOnSubscribe implements Completable.CompletableOnSubscribe {

    private static final int NONE = 0;

    private final WeakReference<View> viewWeakRef;

    private final List<Action1<ViewPropertyAnimatorCompat>> preTransformActions;
    private final List<Action1<ViewPropertyAnimatorCompat>> animationActions;

    public AnimateOnSubscribe(final WeakReference<View> viewWeakRef, final List<Action1<ViewPropertyAnimatorCompat>> animationActions) {
        this(viewWeakRef, null, animationActions);
    }

    public AnimateOnSubscribe(final WeakReference<View> viewWeakRef, @Nullable final List<Action1<ViewPropertyAnimatorCompat>> preAnimationActions,
                              final List<Action1<ViewPropertyAnimatorCompat>> animationActions) {
        this.viewWeakRef = viewWeakRef;
        this.preTransformActions = preAnimationActions;
        this.animationActions = animationActions;
    }

    @Override
    public void call(final Completable.CompletableSubscriber completableSubscriber) {
        final View view = viewWeakRef.get();
        if (view == null) {
            completableSubscriber.onCompleted();
            return;
        }

        final ViewPropertyAnimatorCompat animator = ViewCompat.animate(view);
        completableSubscriber.onSubscribe(new ClearSubscription(animator::cancel));

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

    private void runAnimation(final Completable.CompletableSubscriber completableSubscriber, final ViewPropertyAnimatorCompat animator) {
        applyActions(animationActions, animator);
        animator.withEndAction(completableSubscriber::onCompleted)
                .start();
    }
}
