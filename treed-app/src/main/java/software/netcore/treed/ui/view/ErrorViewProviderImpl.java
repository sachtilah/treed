package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import lombok.RequiredArgsConstructor;

/**
 * @since v.1.8.0
 */
@RequiredArgsConstructor
public class ErrorViewProviderImpl implements ViewProvider {

    private final ViewProvider springViewProvider;

    @Override
    public String getViewName(String viewAndParameters) {
        return ErrorView.VIEW_NAME;
    }

    @Override
    public View getView(String viewName) {
        return springViewProvider.getView(viewName);
    }

}
