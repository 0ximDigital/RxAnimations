package oxim.digital.rxanimations.colorpicker;

public interface ScopedPresenter<T extends BaseView> {

    void bind(T view);

    void unbind();
}
