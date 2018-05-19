package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import software.netcore.treed.ui.TreedCustomComponent;
import software.netcore.treed.ui.view.simViews.CreateSentenceView;
import software.netcore.treed.ui.view.simViews.EditSentenceView;
import software.netcore.treed.ui.view.simViews.UploadPicView;

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

        Button upload = new Button(getString("navigationBar-upload-button"));
        upload.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(UploadPicView.VIEW_NAME));

        Button createSentence = new Button(getString("navigationBar-create-sentence-button"));
        createSentence.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(CreateSentenceView.VIEW_NAME));

        Button editSentence = new Button(getString("navigationBar-edit-sentence-button"));
        editSentence.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(EditSentenceView.VIEW_NAME));


        Label usernameField = new Label("username");

        Button logout = new Button(getString("navigationBar-logout-button"));
        logout.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        content.addComponent(bar);
        bar.addComponents(treed, upload, createSentence, editSentence, usernameField, logout);

        setSizeUndefined();
    }

}

