package software.netcore.treed;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.ui.AuthenticationProvider;
import software.netcore.treed.ui.view.ExampleAccountView;

/**
 * @since v. 1.4.0
 */
@SpringUI
@Title("Treed")
@Push
@PreserveOnRefresh
@Viewport("user-scalable=no,initial-scale=1.0")
@RequiredArgsConstructor
public class RootUI extends UI {

    /* used to manipulate with accounts */
    private final AccountService accountService;
    private final SpringViewProvider springViewProvider;

    @Override
    protected void init(VaadinRequest request) {
        getUI().setResizeLazy(true);
        setContent(new MVerticalLayout());

        Navigator navigator = new Navigator(this, getContent());
        navigator.addProvider(springViewProvider);

        getNavigator().navigateTo(ExampleAccountView.VIEW_NAME);
    }

    @Override
    public MVerticalLayout getContent() {
        return (MVerticalLayout) super.getContent();
    }

    }
