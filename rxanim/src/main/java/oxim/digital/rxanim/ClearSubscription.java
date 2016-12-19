package oxim.digital.rxanim;

import rx.Subscription;
import rx.functions.Action0;

public final class ClearSubscription implements Subscription {

    private boolean isUnsubscribed = false;

    private final Action0 clearAction;

    public ClearSubscription(final Action0 clearAction) {
        this.clearAction = clearAction;
    }

    @Override
    public void unsubscribe() {
        isUnsubscribed = true;
        clearAction.call();
    }

    @Override
    public boolean isUnsubscribed() {
        return isUnsubscribed;
    }
}
