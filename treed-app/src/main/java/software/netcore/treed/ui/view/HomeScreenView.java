package software.netcore.treed.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

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
        // A layout structure used for composition
        VerticalLayout content = this.mainLayout;
        content.removeAllComponents();

        Panel panel = new Panel("treed");
        panel.setSizeFull();
        content.addComponent(panel);

        // Compose from multiple components
        Label label = new Label("Some text");
        content.addComponent(label);

        Button backButton = new Button("Go back");
        backButton.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(ExampleAccountView.VIEW_NAME));


        content.addComponent(backButton);

        // this is not needed for a Composite
        setSizeUndefined();
    }

}

