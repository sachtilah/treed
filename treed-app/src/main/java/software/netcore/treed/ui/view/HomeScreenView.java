package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import software.netcore.treed.ui.TreedCustomComponent;
import software.netcore.treed.ui.view.gameViews.CreateStoryView;
import software.netcore.treed.ui.view.gameViews.UploadPicView;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @since v. 1.0.0
 */
@SpringView(name = HomeScreenView.VIEW_NAME)
public class HomeScreenView extends TreedCustomComponent implements View {

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

        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidth("100%");
        Label treed = new Label("<strong>treed</strong>", ContentMode.HTML);

        Button upload = new Button(getString("upload"));
        upload.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(UploadPicView.VIEW_NAME));

        Button createStory = new Button(getString("createStory"));
        createStory.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(CreateStoryView.VIEW_NAME));

        Label usernameField = new Label("username");

        Button logout = new Button(getString("logout"));
        logout.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        content.addComponent(bar);
        bar.addComponents(treed, upload, createStory, usernameField, logout);

        setSizeUndefined();
    }

}

