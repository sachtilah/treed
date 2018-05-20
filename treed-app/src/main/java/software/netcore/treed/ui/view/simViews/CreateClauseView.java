package software.netcore.treed.ui.view.simViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.business.PiktogramService;
import software.netcore.treed.business.ClauseService;
import software.netcore.treed.data.schema.sim.Clause;
import software.netcore.treed.data.schema.sim.Piktogram;
import software.netcore.treed.ui.view.HomeScreenView;
import software.netcore.treed.ui.view.LoginAttemptView;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
@SpringView(name = software.netcore.treed.ui.view.simViews.CreateClauseView.VIEW_NAME)
public class CreateClauseView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/clause";
    private VerticalLayout mainLayout;
    private final ClauseService clauseService;
    private final PiktogramService piktogramService;

    public CreateClauseView(ClauseService clauseService, PiktogramService piktogramService) {
        this.clauseService = clauseService;
        this.piktogramService = piktogramService;
    }

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
        content.setSizeFull();

        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidth("100%");
        bar.setSizeFull();
        Label treed = new Label("<strong>treed</strong>", ContentMode.HTML);


        Button uploadButton = new Button(getString("navigationBar-upload-button"));
        uploadButton.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(UploadPicView.VIEW_NAME));

        Button homeScreen = new Button(getString("navigationBar-home-button"));
        homeScreen.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(HomeScreenView.VIEW_NAME));

        Button editClause = new Button(getString("navigationBar-edit-clause-button"));
        editClause.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(EditClauseView.VIEW_NAME));

        Label usernameField = new Label("username");

        Button logout = new Button(getString("navigationBar-logout-button"));
        logout.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        content.addComponent(bar);
        bar.addComponents(treed, uploadButton, homeScreen, editClause, usernameField, logout);

        TextField clauseNameField = new TextField(getString("createClause-clause-name-field"));
        TextField row = new TextField(getString("createClause-row-field"));
        TextField column = new TextField(getString("createClause-column-field"));
        TextField clauseField = new TextField(getString("createClause-clause-field"));

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.removeAllComponents();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        verticalLayout.setSizeFull();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.removeAllComponents();
        horizontalLayout.setMargin(true);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setSizeFull();

        Button generateButton = new MButton(getString("createClause-generate-button")).withListener(clickEvent -> {
            boolean isMissing = false;
            if(row.getValue().isEmpty() || column.getValue().isEmpty() || clauseNameField.getValue().isEmpty()
                    || clauseField.getValue().isEmpty()){
                Notification.show(getString("createClause-notification-empty-fields"));
            }
            else if((!row.getValue().matches("[0-9]+")) || (!column.getValue().matches("[0-9]+"))){
                Notification.show(getString("createClause-notification-invalid-fields"));
            }
            else {
                int rows = Integer.parseInt(row.getValue());
                int columns = Integer.parseInt(column.getValue());
                int numberOfWords = rows*columns;
                int counter = 0, counterImage = 0;
                Collection<Piktogram> collection = new ArrayList<>();

                String[] words = clauseField.getValue().split("\\s+");
                GridLayout grid = new GridLayout(columns, rows*2);
                grid.removeAllComponents();
                if(numberOfWords == words.length){
                    for (int j = 0; j < rows*2; j++) {
                        for (int i = 0; i < columns; i++) {
                            Iterable<Piktogram> piktograms = piktogramService.getPics();
                            Collection<Piktogram> piktogramCollection = new ArrayList<>();
                            for (Piktogram piktogram : piktograms) {
                                piktogramCollection.add(piktogram);
                            }
                            Iterator<Piktogram> iteratorPic = piktograms.iterator();

                            while((iteratorPic.hasNext())) {
                                Piktogram iterPic = iteratorPic.next();
                                if(j % 2 == 0){
                                    if ((iterPic.getTerm().equals(words[counterImage])) && (counterImage < numberOfWords)) {
                                        grid.addComponent(new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                                new ByteArrayInputStream(iterPic.getBytes()), "")), i, j);
                                        collection.add(iterPic);
                                        counterImage++;
                                        break;
                                    }
                                }
                                if (j % 2 == 1) {
                                    if((iterPic.getTerm().equals(words[counter])) && (counter < numberOfWords)){
                                        grid.addComponent(new Label(words[counter]), i, j);
                                        counter++;
                                        break;
                                    }
                                }
                                if(!iteratorPic.hasNext())
                                    isMissing = true;
                            }
                        }
                    }
                }
                else {
                    Notification.show(getString("createClause-notification-not-enough-words"));
                    isMissing = true;
                }
                if(isMissing){
                    Notification.show(getString("createClause-notification-upload-missing"));
                }
                else {

                    Button createButton = new MButton(getString("createClause-create-button")).withListener(click -> {
                        addClause(rows, columns, clauseNameField.getValue(), collection);
                        Notification.show(getString("createClause-notification-clause-saved"));
                        build();
                    });

                    Button reloadButton = new MButton(getString("createClause-reload-button")).withListener(click -> {
                        build();
                    });
                    
                    horizontalLayout.addComponents(createButton, reloadButton);

                    HorizontalLayout horizontalLayout1 = new HorizontalLayout();
                    content.addComponent(horizontalLayout1);
                    horizontalLayout1.addComponents(verticalLayout);
                    horizontalLayout1.addComponent(grid);
                    //horizontalLayout1.setComponentAlignment(grid, Alignment.BOTTOM_CENTER);
                    //build();
                }
            }
        });
        horizontalLayout.addComponent(generateButton);
        content.addComponents(verticalLayout);
        verticalLayout.addComponents(row, column, clauseNameField, clauseField, horizontalLayout);
        content.setComponentAlignment(verticalLayout, Alignment.TOP_LEFT);
        //verticalLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_LEFT);
    }

    private void addClause(int row, int column, String name, Collection<Piktogram> collection) {
        boolean isUsed = false;

        Iterable<Clause> clauses = clauseService.getClauses();
        Collection<Clause> clauseCollection = new ArrayList<>();
        for (Clause clause : clauses) {
            clauseCollection.add(clause);
        }
        Iterator<Clause> iteratorClause = clauses.iterator();

        while ((iteratorClause.hasNext())) {
            Clause iterClause = iteratorClause.next();
            if (iterClause.getName().equals(name)) {
                isUsed = true;
                Notification.show(getString("createClause-notification-clause-name-used"));
            }
        }
        if (!isUsed) {
            Clause clauseAdd = new Clause();
            clauseAdd.setRowCount(row);
            clauseAdd.setColumnCount(column);
            clauseAdd.setName(name);
            clauseAdd.setPiktograms(collection);
            clauseAdd.setCreateTime(Date.from(Instant.now()));
            clauseService.saveClause(clauseAdd);
        }
    }

}

