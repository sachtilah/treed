package software.netcore.treed.ui.view.sim;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import software.netcore.treed.api.AbstractRestrictedEnterView;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.ui.AuthenticationProvider;
import software.netcore.treed.ui.view.TreedDashboard;

import java.util.Objects;

/**
 * @since v.1.8.0
 */
public abstract class AbstractSimView extends AbstractRestrictedEnterView {

    @Override
    protected Component getHeader() {
        Account account = AuthenticationProvider.getLoggedAccount();

        MenuBar menuBar = new MenuBar();
        menuBar.addItem(getString("navigationBar-home-button"), command -> getUI().getNavigator().navigateTo(SimHomeScreenView.VIEW_NAME));
        menuBar.addItem(getString("navigationBar-upload-button"), command -> getUI().getNavigator().navigateTo(UploadPicView.VIEW_NAME));

//        if (Objects.nonNull(account) && AccountRole.TEACHER.equals(account.getRole())) {
            menuBar.addItem(getString("navigationBar-create-clause-button"), command -> getUI().getNavigator().navigateTo(CreateClauseView.VIEW_NAME));
            menuBar.addItem(getString("navigationBar-edit-clause-button"), command -> getUI().getNavigator().navigateTo(EditClauseView.VIEW_NAME));
//        }

        return new MHorizontalLayout()
                .withStyleName("header")
                .withFullWidth()
                .withHeight(60, Unit.PIXELS)
                .add(new MHorizontalLayout()
                                .withWidth(80, Unit.PERCENTAGE)
                                .add(new MButton()
                                        .withCaption("Treed")
                                        .withStyleName(ValoTheme.BUTTON_LARGE, ValoTheme.BUTTON_BORDERLESS)
                                        .withListener(event -> getUI().getNavigator().navigateTo(TreedDashboard.VIEW_NAME)))
                                .add(menuBar)
                                .add(new MButton()
                                        .withCaption(getString("navigationBar-logout-button"))
                                        .withIcon(VaadinIcons.SIGN_OUT)
                                        .withStyleName(ValoTheme.BUTTON_BORDERLESS)
                                        .withListener(event -> AuthenticationProvider.logout()), Alignment.MIDDLE_RIGHT)
                        , Alignment.MIDDLE_CENTER);
    }

}
