package software.netcore.treed.ui.view.sim;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.sim.ClauseService;
import software.netcore.treed.data.schema.sim.Clause;
import java.util.ArrayList;
import java.util.Collection;


/**
 * @since v. 1.0.0
 */
@SpringView(name = SimHomeScreenView.VIEW_NAME)
public class SimHomeScreenView extends AbstractSimView implements View {

    public static final String VIEW_NAME = "/loged/home";
    private final ClauseService clauseService;
    public SimHomeScreenView(ClauseService clauseService) {
        this.clauseService = clauseService;
    }

    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        contentLayout.removeAllComponents();
        contentLayout.setMargin(true);
        contentLayout.setSpacing(true);

//        HorizontalLayout bar = new HorizontalLayout();
//        bar.setWidth("100%");
//        Label treed = new Label("<strong>treed</strong>", ContentMode.HTML);
//
//        Button upload = new Button(getString("navigationBar-upload-button"));
//        upload.addClickListener((Button.ClickListener) event ->
//                getUI().getNavigator().navigateTo(UploadPicView.VIEW_NAME));
//
//        Button createClause = new Button(getString("navigationBar-create-clause-button"));
//        createClause.addClickListener((Button.ClickListener) event ->
//                getUI().getNavigator().navigateTo(CreateClauseView.VIEW_NAME));
//
//        Button editClause = new Button(getString("navigationBar-edit-clause-button"));
//        editClause.addClickListener((Button.ClickListener) event ->
//                getUI().getNavigator().navigateTo(EditClauseView.VIEW_NAME));
//
//
//        Label usernameField = new Label("username");
//
//        Button logout = new Button(getString("navigationBar-logout-button"));
//        logout.addClickListener((Button.ClickListener) event ->
//                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));
//
//        content.addComponent(bar);
//        bar.addComponents(treed, upload, createClause, editClause, usernameField, logout);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.removeAllComponents();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);

        Iterable<Clause> clauses = clauseService.getClauses();
        Collection<Clause> clauseCollection = new ArrayList<>();
        for (Clause clause : clauses) {
            clauseCollection.add(clause);
        }
        for (Clause clause : clauses) {
            Button clauseButton = new Button(clause.getName());
            clauseButton.addClickListener((Button.ClickListener) event ->
                    getUI().getNavigator().navigateTo(GameView.VIEW_NAME + "/" + clause.getName()));
            verticalLayout.addComponent(clauseButton);
            verticalLayout.setComponentAlignment(clauseButton, Alignment.MIDDLE_CENTER);
        }
        //Button clauseButton = new Button("kinect");
        //clauseButton.addClickListener((Button.ClickListener) event ->
          //    getUI().getNavigator().navigateTo(KinectView.VIEW_NAME));
        //verticalLayout.addComponent(clauseButton);
        //verticalLayout.setComponentAlignment(clauseButton, Alignment.MIDDLE_CENTER);

        if (verticalLayout.getComponentCount() == 0) {
            contentLayout.add(new MVerticalLayout()
                            .withSizeUndefined()
                            .add(new MLabel("No clause created yet."))
                    , Alignment.TOP_CENTER);
        } else {
            contentLayout.addComponent(verticalLayout);
        }
    }
}

