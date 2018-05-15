package software.netcore.treed.ui.view.puzzle;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.ui.AuthenticationProvider;
import software.netcore.treed.ui.TreedCustomComponent;


@RequiredArgsConstructor
@SpringView(name =CreatePuzzleView.VIEW_NAME)
public class CreatePuzzleView extends TreedCustomComponent implements View {
    public static final String VIEW_NAME = "/login";

    private AuthenticationProvider authenticationProvider;
    private MVerticalLayout mainLayout;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.mainLayout = new MVerticalLayout()
                .withFullSize();
        setCompositionRoot(this.mainLayout);
        setSizeFull();
        build();
    }

    /**
     * Build page.
     */
    private void build() {

        Button selectButton = new MButton(getString("select-upload-puzzle-part"))
                .withListener(clickEvent -> {
                    uploadPath();
                    //login(pathField.getValue(), passwordField.getValue());
                });
        //.withClickShortcut(ShortcutAction.KeyCode.ENTER);

        TextField pathField = new MTextField(getString("path-puzzle-part-pictogram"))
                .withFullSize();

        TextField nameField = new MTextField(getString("name-puzzle-part-pictogram"))
                .withFullSize();

        Button uploadButton = new MButton(getString("upload-puzzle-part"))
                .withListener(clickEvent -> {
                    upload(pathField.getValue(), nameField.getValue());
                })
                .withListener(event -> getUI().getNavigator().navigateTo(CreatePuzzleView.VIEW_NAME));

        TextField searchField = new MTextField(getString("search-puzzle-part-pictogram"))
                .withFullSize();


        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                .add(new MHorizontalLayout()
                        .add(selectButton, pathField)
                        .add(new MVerticalLayout()
                                .withMargin(false)
                                .withSpacing(false)
                                .add(nameField, uploadButton))
                        .add(searchField, Alignment.MIDDLE_CENTER));





        /*MButton accountRegistrationButton = new MButton(getString("createAccount"))
                .withStyleName(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_TINY)
                .withListener(event -> getUI().getNavigator().navigateTo(CreatePuzzleView.VIEW_NAME));

        MButton resetPasswordButton = new MButton(getString("resetPassword"))
                .withStyleName(ValoTheme.BUTTON_LINK, ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_TINY)
                .withListener(event -> getUI().getNavigator().navigateTo(ResetPasswordView.VIEW_NAME));*/


        mainLayout.removeAllComponents();
        mainLayout.add(content, Alignment.MIDDLE_CENTER);
    }

    private void login(String username, String password) {
        boolean isLoggedIn = authenticationProvider.login(username, password);

        if (isLoggedIn) {
            getUI().getNavigator().navigateTo(CreatePuzzleView.VIEW_NAME);
        } else {
            Notification.show(getString("ntfWrongPassword"));
        }
    }

    /**
     * Upload puzzle part.
     *
     * @param path path to file
     * @param name name of uploaded file
     */
    private void upload(String path, String name){

    }

    /**
     * Choose path.
     *
     * //@param username username
     * //@param password password
     */
    private void uploadPath(){

    }
}

