package software.netcore.treed.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.AbstractRestrictedEnterView;
import software.netcore.treed.ui.AuthenticationProvider;
import software.netcore.treed.ui.view.puzzle.SelectPuzzleGameView;
import software.netcore.treed.ui.view.sim.SimHomeScreenView;

/**
 * @since v.1.8.0
 */
@RequiredArgsConstructor
@SpringView(name = TreedDashboard.VIEW_NAME)
public class TreedDashboard extends AbstractRestrictedEnterView implements View {

    public static final String VIEW_NAME = "/dashboard";

    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent event) {
        contentLayout.add(new MVerticalLayout()
                        .withSizeUndefined()
                        .add(new MLabel("<h1>Welcome in Treed application</h1>")
                                .withContentMode(ContentMode.HTML))
                        .add(new MLabel("<p>Select the game to continue</p>")
                                .withContentMode(ContentMode.HTML), Alignment.MIDDLE_CENTER)
                        .add(new MHorizontalLayout()
                                        .withSizeUndefined()
                                        .add(new MButton()
                                                .withCaption("SimText")
                                                .withStyleName(ValoTheme.BUTTON_LARGE, ValoTheme.BUTTON_BORDERLESS_COLORED)
                                                .withListener(event1 -> getUI().getNavigator().navigateTo(SimHomeScreenView.VIEW_NAME)))
                                        .add(new MButton()
                                                .withCaption("Puzzle")
                                                .withStyleName(ValoTheme.BUTTON_LARGE, ValoTheme.BUTTON_BORDERLESS_COLORED)
                                                .withListener(event1 -> getUI().getNavigator().navigateTo(SelectPuzzleGameView.VIEW_NAME))),
                                Alignment.MIDDLE_CENTER),
                Alignment.TOP_CENTER);
    }

    @Override
    protected Component getHeader() {
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
                                .add(new MButton()
                                        .withCaption("Logout")
                                        .withIcon(VaadinIcons.SIGN_OUT)
                                        .withStyleName(ValoTheme.BUTTON_BORDERLESS)
                                        .withListener(event -> AuthenticationProvider.logout()), Alignment.MIDDLE_RIGHT)
                        , Alignment.MIDDLE_CENTER);
    }

}
