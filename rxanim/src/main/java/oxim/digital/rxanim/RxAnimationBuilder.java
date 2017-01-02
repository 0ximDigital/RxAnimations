package oxim.digital.rxanim;

import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.functions.Consumer;

public final class RxAnimationBuilder {

    private static final float OPAQUE = 1f;
    private static final float TRANSPARENT = 0f;

    private static final int DEFAULT_DURATION = 300;
    private static final int DEFAULT_DELAY = 0;

    public static RxAnimationBuilder animate(final View view) {
        return new RxAnimationBuilder(view, DEFAULT_DURATION, DEFAULT_DELAY, defaultInterpolator());
    }

    public static RxAnimationBuilder animate(final View view, final int duration) {
        return new RxAnimationBuilder(view, duration, DEFAULT_DELAY,
                new AccelerateDecelerateInterpolator());
    }

    public static RxAnimationBuilder animate(final int delay, final View view) {
        return new RxAnimationBuilder(view, DEFAULT_DURATION, delay,
                new AccelerateDecelerateInterpolator());
    }

    public static RxAnimationBuilder animate(final View view, final int duration, final int delay) {
        return new RxAnimationBuilder(view, duration, delay, new AccelerateDecelerateInterpolator());
    }

    public static RxAnimationBuilder animate(final View view, final Interpolator interpolator) {
        return new RxAnimationBuilder(view, DEFAULT_DURATION, DEFAULT_DELAY, interpolator);
    }

    public static RxAnimationBuilder animate(final View view, final int duration, final int delay,
                                             final Interpolator interpolator) {
        return new RxAnimationBuilder(view, duration, delay, interpolator);
    }

    private RxAnimationBuilder(final View view, final int duration, final int delay,
                               final Interpolator interpolator) {
        this.view = view;
        this.preTransformActions = new LinkedList<>();
        this.animateActions = new LinkedList<>();

        this.animateActions.add(animate -> animate.setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(interpolator));
    }

    final List<Consumer<ViewPropertyAnimatorCompat>> preTransformActions;
    final List<Consumer<ViewPropertyAnimatorCompat>> animateActions;
    final View view;

    public RxAnimationBuilder duration(final int duration) {
        animateActions.add(animate -> animate.setDuration(duration));
        return this;
    }

    public RxAnimationBuilder delay(final int delay) {
        animateActions.add(animate -> animate.setStartDelay(delay));
        return this;
    }

    public RxAnimationBuilder interpolator(final Interpolator interpolator) {
        animateActions.add(animate -> animate.setInterpolator(interpolator));
        return this;
    }

    public RxAnimationBuilder fadeIn() {
        preTransformActions.add(preTransform -> preTransform.alpha(0f));
        animateActions.add(animate -> animate.alpha(OPAQUE));
        return this;
    }

    public RxAnimationBuilder fadeOut() {
        animateActions.add(animate -> animate.alpha(TRANSPARENT));
        return this;
    }

    public RxAnimationBuilder rotate(final float rotation) {
        preTransformActions.add(preTransform -> preTransform.rotation(rotation));
        animateActions.add(animate -> animate.rotation(0));
        return this;
    }

    public RxAnimationBuilder rotateBy(final float rotation) {
        preTransformActions.add(preTransform -> preTransform.rotationBy(rotation));
        animateActions.add(animate -> animate.rotationBy(rotation));
        return this;
    }

    public RxAnimationBuilder translateX(final int dX) {
        preTransformActions.add(preTransform -> preTransform.xBy(-dX));
        animateActions.add(animate -> animate.xBy(dX));
        return this;
    }

    public RxAnimationBuilder translateY(final int dY) {
        preTransformActions.add(preTransform -> preTransform.yBy(-dY));
        animateActions.add(animate -> animate.yBy(dY));
        return this;
    }

    public RxAnimationBuilder elevationBy(final int dZ) {
        preTransformActions.add(preTransform -> preTransform.zBy(-dZ));
        animateActions.add(animate -> animate.zBy(dZ));
        return this;
    }

    public RxAnimationBuilder translateBy(final int dX, final int dY) {
        preTransformActions.add(preTransform -> preTransform.xBy(-dX).yBy(-dY));
        animateActions.add(animate -> animate.xBy(dX).yBy(dY));
        return this;
    }

    public RxAnimationBuilder translateBy(final int dX, final int dY, final int dZ) {
        preTransformActions.add(preTransform -> preTransform.xBy(-dX).yBy(-dY).zBy(-dZ));
        animateActions.add(animate -> animate.xBy(dX).yBy(dY).zBy(dZ));
        return this;
    }

    public RxAnimationBuilder scaleX(final float dX) {
        animateActions.add(animate -> animate.scaleXBy(dX));
        return this;
    }

    public RxAnimationBuilder scaleY(final float dY) {
        animateActions.add(animate -> animate.scaleYBy(dY));
        return this;
    }

    public RxAnimationBuilder scale(final float dX, final float dY) {
        animateActions.add(animate -> animate.scaleXBy(dX).scaleYBy(dY));
        return this;
    }

    public Completable schedule() {
        return new AnimateCompletable(view, preTransformActions, animateActions);
    }

    public Completable schedule(final boolean preTransform) {
        return new AnimateCompletable(view, preTransform ? preTransformActions : null, animateActions);
    }

    private static Interpolator defaultInterpolator() {
        return new AccelerateDecelerateInterpolator();
    }
}
