package software.netcore.treed;

import com.vaadin.annotations.*;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.ui.SecurityViewChangeListener;
import software.netcore.treed.ui.view.ErrorViewProviderImpl;
import software.netcore.treed.ui.view.LoginAttemptView;




import java.util.Locale;

/**
 * @since v. 1.0.0
 */
@Slf4j
@SpringUI
@Title("Treed")
@Theme("treed")
@Push(transport = Transport.LONG_POLLING)
@PreserveOnRefresh
@Viewport("user-scalable=no,initial-scale=1.0")
@RequiredArgsConstructor
public class RootUI extends UI {

    private final MessageSource messageSource;

    private final SpringViewProvider springViewProvider;

    @Override
    protected void init(VaadinRequest request) {
        getUI().setResizeLazy(true);
        setContent(new MVerticalLayout()
                .withMargin(false)
                .withFullSize());

        configureSession();

        Navigator navigator = new Navigator(this, getContent());
        navigator.addProvider(springViewProvider);
        navigator.setErrorProvider(new ErrorViewProviderImpl(springViewProvider));
        navigator.addViewChangeListener(new SecurityViewChangeListener());

        //getNavigator().navigateTo(CreatePuzzleView.VIEW_NAME);
        getNavigator().navigateTo(LoginAttemptView.VIEW_NAME);
    }

    /**
     * Store {@link Locale} and {@link MessageSource} in the current session.
     */
    private void configureSession() {
        log.debug("Configuring Vaadin session");
        Locale locale = VaadinService.getCurrentRequest().getLocale();
        getSession().setLocale(locale);
        log.debug("Locale set to {}", locale);

        getSession().setAttribute(MessageSource.class, messageSource);
        log.debug("MessageSource set to {}", messageSource.getClass());
    }

    @Override
    public MVerticalLayout getContent() {
        return (MVerticalLayout) super.getContent();
    }
}
