package oxim.digital.rxanimations.colorpicker;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class BasePresenter<T extends BaseView> implements ScopedPresenter<T> {

    private CompositeDisposable compositeSubscription;
    private WeakReference<T> viewWeakReference;

    @Override
    public void bind(final T view) {
        this.viewWeakReference = new WeakReference<>(view);
        compositeSubscription = new CompositeDisposable();
    }

    @Override
    public void unbind() {
        if (compositeSubscription != null && !compositeSubscription.isDisposed()) {
            compositeSubscription.dispose();
            compositeSubscription = null;
        }
    }

    protected final void addSubscription(final Disposable subscription) {
        if (compositeSubscription != null && !compositeSubscription.isDisposed()) {
            compositeSubscription.add(subscription);
        }
    }

    protected void doIfViewNotNull(final Consumer<T> whenViewNotNull) throws Exception {
        final T view = getNullableView();
        if (view != null) {
            whenViewNotNull.accept(view);
        }
    }

    protected
    @Nullable
    T getNullableView() {
        return viewWeakReference == null ? null : viewWeakReference.get();
    }
}
