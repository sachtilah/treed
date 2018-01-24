package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

/**
 * @since v. 1.0.0
 */
@SpringView(name = CustomComponentView.VIEW_NAME)
public class CustomComponentView extends CustomComponent implements View {

    public static final String VIEW_NAME = "/custom/component";

    public CustomComponentView() {
        // A layout structure used for composition
        Panel panel = new Panel("My Custom Component");
        VerticalLayout panelContent = new VerticalLayout();
        panel.setContent(panelContent);

        // Compose from multiple components
        Label label = new Label("Some text");
        panelContent.addComponent(label);

        Button backButton = new Button("Go back");
        backButton.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(ExampleAccountView.VIEW_NAME));


        panelContent.addComponent(backButton);

        // The composition root MUST be set
        setCompositionRoot(panel);

        // Set the size as undefined at all levels
        panelContent.setSizeUndefined();
        panel.setSizeUndefined();
        // this is not needed for a Composite
        setSizeUndefined();
    }

}
