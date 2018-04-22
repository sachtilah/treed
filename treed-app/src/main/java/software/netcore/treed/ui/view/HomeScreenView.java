package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @since v. 1.0.0
 */
@SpringView(name = HomeScreenView.VIEW_NAME)
public class HomeScreenView extends CustomComponent implements View {

    public static final String VIEW_NAME = "/loged/home";
    private VerticalLayout mainLayout;


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.mainLayout = new VerticalLayout();
        setCompositionRoot(this.mainLayout);
        build();
    }
    public void build() {
        VerticalLayout content = this.mainLayout;
        content.removeAllComponents();
        content.setMargin(true);
        content.setSpacing(true);

        Locale locale = VaadinService.getCurrentRequest().getLocale();
        ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

        HorizontalLayout bar = new HorizontalLayout();
        bar.addComponent(new Label("<strong>treed</strong>", ContentMode.HTML));
        bar.setWidth("100%");

        Button logout = new Button(messages.getString("logout"));
        logout.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        content.addComponent(bar);
        bar.addComponent(logout);

        setSizeUndefined();
    }

}

