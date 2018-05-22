package software.netcore.treed.ui.view.simViews;

import com.vaadin.data.HasValue;
import com.vaadin.data.Result;
import com.vaadin.data.ValidationResult;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import software.netcore.treed.business.ClauseService;
import software.netcore.treed.business.PiktogramService;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.data.schema.sim.Clause;
import software.netcore.treed.data.schema.sim.Piktogram;
import software.netcore.treed.ui.TreedCustomComponent;
import com.vaadin.navigator.View;
import software.netcore.treed.ui.view.HomeScreenView;
import software.netcore.treed.ui.view.LoginAttemptView;

import javax.xml.bind.Binder;
import javax.xml.bind.ValidationEvent;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static com.vaadin.data.ValidationResult.ok;

@Slf4j
@SpringView(name = software.netcore.treed.ui.view.simViews.GameView.VIEW_NAME)
public class GameView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/game";
    private VerticalLayout mainLayout;
    public String parameter;
    private final ClauseService clauseService;

    public GameView(ClauseService clauseService) {
        this.clauseService = clauseService;
    }

    public void build(String parameter){
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

        Button createClause = new Button(getString("navigationBar-create-clause-button"));
        createClause.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(CreateClauseView.VIEW_NAME));

        Button editClause = new Button(getString("navigationBar-edit-clause-button"));
        editClause.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(EditClauseView.VIEW_NAME));

        Label usernameField = new Label("username");

        Button logout = new Button(getString("navigationBar-logout-button"));
        logout.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        content.addComponent(bar);
        bar.addComponents(treed, upload, createClause, editClause, usernameField, logout);


        Label clauseNameLabel = new Label("<h1>" + parameter + "</h1>", ContentMode.HTML);
        content.addComponent(clauseNameLabel);
        content.setComponentAlignment(clauseNameLabel, Alignment.MIDDLE_CENTER);

        Button backButton = new MButton(getString("gameView-back-button"));
        backButton.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(HomeScreenView.VIEW_NAME));

        content.addComponent(backButton);
        content.setComponentAlignment(backButton, Alignment.MIDDLE_CENTER);

        final int[] c = {0};

        Iterable<Clause> clauses = clauseService.getClauses();
        for (Clause clause : clauses) {
            if(clause.getName().equals(parameter)){
                int columns = clause.getColumnCount();
                int rows = clause.getRowCount();
                GridLayout grid = new GridLayout(columns, rows*2);
                grid.removeAllComponents();
                Iterable<Piktogram> piktograms = clause.getPiktograms();
                Collection<Piktogram> piktogramCollection = new ArrayList<>();
                for (Piktogram piktogram : piktograms) {
                    piktogramCollection.add(piktogram);
                }
                Iterator<Piktogram> iteratorPic = piktograms.iterator();
                int i = 0, j = 0, k = 1, l = 0;
                while (iteratorPic.hasNext()) {
                            Piktogram iterPic = iteratorPic.next();
                            if (j % 2 == 0 && i<columns) {
                                    grid.addComponent(new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                            new ByteArrayInputStream(iterPic.getBytes()), "")), i, j);
                                    i++;
                            }
                            if(i==columns){
                                j+=2;
                                i=0;
                            }
                            if (k % 2 == 1 && l<columns) {
                                grid.addComponent(new MTextField("").withValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                                    if(iterPic.getTerm().equals(valueChangeEvent.getValue())) {
                                        grid.removeComponent(valueChangeEvent.getComponent());
                                        grid.addComponent(new Label("<strong><center>" + iterPic.getTerm()
                                        + "</center></strong>", ContentMode.HTML));
                                        c[0]++;
                                        if(c[0] ==clause.getPiktograms().size())
                                            Notification.show(getString("gameView-notification-win"));
                                    }
                                    else Notification.show(getString("gameView-notification-wrong"), Notification.Type.ERROR_MESSAGE);
                                }), l, k);
                                l++;
                            }
                            if(l==columns){
                                k+=2;
                                l=0;
                            }
                }
                content.addComponent(grid);
                content.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
                log.info("Clause " + parameter + " loaded");
            }
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event){
        this.mainLayout = new VerticalLayout();
        setCompositionRoot(this.mainLayout);
        if(event.getParameters() != null){
            String[] params = event.getParameters().split("/");
            for(String param : params){
                parameter = param;
            }
        }
        build(parameter);
    }
}