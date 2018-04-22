package software.netcore.treed;

import com.vaadin.annotations.*;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.AccountService;
import software.netcore.treed.business.OtpService;
import software.netcore.treed.ui.view.LoginAttemptView;

import java.util.Locale;

/**
 * @since v. 1.0.0
 */
@Slf4j
@SpringUI
@Title("Treed")
@Push
@PreserveOnRefresh
@Viewport("user-scalable=no,initial-scale=1.0")
@RequiredArgsConstructor
public class RootUI extends UI {

    /* used to manipulate with accounts */
    private final AccountService accountService;
    private final OtpService otpService;
    private final SpringViewProvider springViewProvider;

    @Override
    protected void init(VaadinRequest request) {
        Locale locale = VaadinService.getCurrentRequest().getLocale();
        this.getSession().setLocale(locale);
        log.debug("Locale set to {}", locale);

        getUI().setResizeLazy(true);
        setContent(new MVerticalLayout());



        Navigator navigator = new Navigator(this, getContent());
        navigator.addProvider(springViewProvider);

        getNavigator().navigateTo(LoginAttemptView.VIEW_NAME);
    }

    @Override
    public MVerticalLayout getContent() {
        return (MVerticalLayout) super.getContent();
    }
}
