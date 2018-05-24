package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.AbstractFreeEnterView;

/**
 * This is an generic error view. Contains generic error message and button which redirect user
 * to the {@link LoginAttemptView}.
 *
 * @since v.1.0.0
 */
@RequiredArgsConstructor
@SpringView(name = "")
public class ErrorView extends AbstractFreeEnterView implements View {

    public static final String VIEW_NAME = "/error";

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        MVerticalLayout mainLayout = new MVerticalLayout()
                .withFullSize();
        setCompositionRoot(mainLayout);
        setSizeFull();

        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                .add(new MLabel()
                        .withContent("Sorry. Something went wrong."))
                .add(new MButton()
                        .withCaption("Go back to the application")
                        .withListener(event1 -> getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME)));

        mainLayout.add(content, Alignment.MIDDLE_CENTER);
    }

}
