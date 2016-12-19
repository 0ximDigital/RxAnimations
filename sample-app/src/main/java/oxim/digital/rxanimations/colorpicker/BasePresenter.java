package oxim.digital.rxanimations.colorpicker;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<T extends BaseView> implements ScopedPresenter<T> {

    private CompositeSubscription compositeSubscription;
    private WeakReference<T> viewWeakReference;

    @Override
    public void bind(final T view) {
        this.viewWeakReference = new WeakReference<>(view);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void unbind() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    protected final void addSubscription(final Subscription subscription) {
        if(compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.add(subscription);
        }
    }

    protected void doIfViewNotNull(final Action1<T> whenViewNotNull) {
        final T view = getNullableView();
        if (view != null) {
            whenViewNotNull.call(view);
        }
    }

    protected
    @Nullable
    T getNullableView() {
        return viewWeakReference == null ? null : viewWeakReference.get();
    }
}
