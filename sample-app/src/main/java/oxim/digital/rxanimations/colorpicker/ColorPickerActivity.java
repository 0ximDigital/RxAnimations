package oxim.digital.rxanimations.colorpicker;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import oxim.digital.rxanimations.R;
import rx.Completable;
import rx.subjects.BehaviorSubject;

import static oxim.digital.rxanim.RxAnimations.animateTogether;
import static oxim.digital.rxanim.RxAnimations.enterViewsWithDelay;
import static oxim.digital.rxanim.RxAnimations.enterWithRotation;
import static oxim.digital.rxanim.RxAnimations.fadeIn;
import static oxim.digital.rxanim.RxAnimations.hide;
import static oxim.digital.rxanim.RxAnimations.hideViewGroupChildren;
import static oxim.digital.rxanim.RxAnimations.show;

public final class ColorPickerActivity extends AppCompatActivity implements ColorPickerContract.View {

    private static final int LANGUAGE_ICON_ANIMATION_DURATION = 800;
    private static final int ICONS_ANIMATION_DURATION = 600;
    private static final int ITEMS_HORIZONTAL_OFFSET = -56;

    private static final int CARDS_HORIZONTAL_OFFSET = -160;
    private static final int CARD_INITIAL_DELAY = 400;
    private static final int CARDS_DELAY = 260;
    private static final int CARDS_ANIMATION_DURATION = 400;

    private static final int LE_COLORS_GROUP_INITIAL_DELAY = 1000;
    private static final int LANGUAGE_DETAILS_DELAY = 200;
    private static final int WELCOME_TEXT_INITIAL_DELAY = 500;
    private static final int WELCOME_TEXT_DELAY = 600;

    private static final int SETTINGS_ICON_DELAY = 600;
    private static final int SETTINGS_ICON_ROTATION = 60;

    @Bind(R.id.palette_background)
    ImageView paletteBackground;

    @Bind(R.id.user_welcome_message)
    TextView userWelcomeMessage;

    @Bind(R.id.username)
    TextView username;

    @Bind(R.id.le_colors_label)
    TextView leColorsLabel;

    @Bind(R.id.hello_text_container)
    LinearLayout helloTextContainer;

    @Bind(R.id.see_more_button)
    TextView seeMoreColorsButton;

    @Bind(R.id.language_container)
    LinearLayout languageContainer;

    @Bind(R.id.settings_icon)
    ImageView settingsIcon;

    @Bind(R.id.color_cards_layout)
    ViewGroup colorCardsLayout;

    @Bind(R.id.color_card_one)
    CardView colorCardOne;

    @Bind(R.id.color_card_two)
    CardView colorCardTwo;

    @Bind(R.id.color_card_three)
    CardView colorCardThree;

    @Bind(R.id.color_card_four)
    CardView colorCardFour;

    private BehaviorSubject<Boolean> viewReadySignal = BehaviorSubject.create(false);

    private ColorPickerContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        ButterKnife.bind(this);
        setCardColors();

        presenter = new ColorPickerPresenter();
        presenter.bind(this);
    }

    private void setCardColors() {
        final Resources resources = getResources();
        colorCardOne.setCardBackgroundColor(resources.getColor(R.color.color_one));
        colorCardTwo.setCardBackgroundColor(resources.getColor(R.color.color_two));
        colorCardThree.setCardBackgroundColor(resources.getColor(R.color.color_three));
        colorCardFour.setCardBackgroundColor(resources.getColor(R.color.color_four));
    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!viewReadySignal.hasCompleted() && hasFocus) {
            viewReadySignal.onCompleted();
        }
    }

    @Override
    public Completable setupInitialAnimation() {
        return animateTogether(hideViewGroupChildren(helloTextContainer, languageContainer),
                               hide(paletteBackground, settingsIcon, colorCardsLayout));
    }

    @Override
    public Completable startInitialAnimation() {
        return viewReadyCompletable().concatWith(
                animateTogether(fadeIn(paletteBackground, LANGUAGE_ICON_ANIMATION_DURATION),
                                enterViewsWithDelay(CARD_INITIAL_DELAY, CARDS_DELAY, CARDS_ANIMATION_DURATION, CARDS_HORIZONTAL_OFFSET,
                                                    colorCardOne, colorCardTwo, colorCardThree, colorCardFour),
                                show(colorCardsLayout),
                                enterViewsWithDelay(WELCOME_TEXT_INITIAL_DELAY, WELCOME_TEXT_DELAY, WELCOME_TEXT_DELAY,
                                                    ITEMS_HORIZONTAL_OFFSET, userWelcomeMessage, username),
                                enterViewsWithDelay(LE_COLORS_GROUP_INITIAL_DELAY, LANGUAGE_DETAILS_DELAY, ICONS_ANIMATION_DURATION,
                                                    ITEMS_HORIZONTAL_OFFSET, leColorsLabel, seeMoreColorsButton),
                                enterWithRotation(settingsIcon, ICONS_ANIMATION_DURATION, ITEMS_HORIZONTAL_OFFSET,
                                                  0, SETTINGS_ICON_DELAY, SETTINGS_ICON_ROTATION)));
    }

    private Completable viewReadyCompletable() {
        return viewReadySignal.toCompletable();
    }

    @OnClick(R.id.root_container)
    public void onRootViewClick() {
        presenter.refreshView();
    }
}
