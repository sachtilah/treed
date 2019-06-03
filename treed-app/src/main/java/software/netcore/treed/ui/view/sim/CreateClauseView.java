package software.netcore.treed.ui.view.sim;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.sim.PiktogramService;
import software.netcore.treed.business.sim.ClauseService;
import software.netcore.treed.data.schema.sim.Clause;
import software.netcore.treed.data.schema.sim.Piktogram;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
@SpringView(name = software.netcore.treed.ui.view.sim.CreateClauseView.VIEW_NAME)
public class CreateClauseView extends AbstractSimView implements View {

    public static final String VIEW_NAME = "/clause";
    private final ClauseService clauseService;
    private final PiktogramService piktogramService;

    public CreateClauseView(ClauseService clauseService, PiktogramService piktogramService) {
        this.clauseService = clauseService;
        this.piktogramService = piktogramService;
    }

    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        VerticalLayout content = new MVerticalLayout()
              .withSpacing(true);

        contentLayout.removeAllComponents();
        contentLayout.add(content);

        TextField clauseNameField = new TextField(getString("createClause-clause-name-field"));
        TextField row = new TextField(getString("createClause-row-field"));
        TextField column = new TextField(getString("createClause-column-field"));
        TextArea clauseField = new TextArea(getString("createClause-clause-field"));

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

        HorizontalLayout horizontalLayout1 = new HorizontalLayout();

        Button generateButton = new MButton(getString("createClause-generate-button")).withListener(clickEvent -> {
            boolean isMissing = false;
            if (row.getValue().isEmpty() || column.getValue().isEmpty() || clauseNameField.getValue().isEmpty()
                  || clauseField.getValue().isEmpty()) {
                Notification.show(getString("createClause-notification-empty-fields"));
                build(contentLayout, viewChangeEvent);
            } else if ((!row.getValue().matches("[0-9]+")) || (!column.getValue().matches("[0-9]+"))) {
                Notification.show(getString("createClause-notification-invalid-fields"));
                build(contentLayout, viewChangeEvent);
            } else {
                int rows = Integer.parseInt(row.getValue());
                int columns = Integer.parseInt(column.getValue());
                int numberOfWords = rows * columns;
                int counter = 0;
                int counterImage = 0;
                int counterAudio = 0;
                Collection<Piktogram> collection = new ArrayList<>();

                String[] words = clauseField.getValue().split("\\s+");
                GridLayout grid = new GridLayout(columns, rows * 3);
                grid.removeAllComponents();
                if (numberOfWords == words.length) {
                    for (int j = 1; j <= rows * 3; j++) {
                        for (int i = 0; i < columns; i++) {
                            Iterable<Piktogram> piktograms = piktogramService.getPics();
                            Collection<Piktogram> piktogramCollection = new ArrayList<>();
                            for (Piktogram piktogram : piktograms)
                                piktogramCollection.add(piktogram);
                            Iterator<Piktogram> iteratorPic = piktograms.iterator();

                            while (iteratorPic.hasNext() && (counterImage < numberOfWords || counterAudio < numberOfWords || counter < numberOfWords)) {
                                Piktogram iterPic = iteratorPic.next();
                                if (j % 3 == 1 ||j == 1) {
                                    if (iterPic.getTerm().equals(words[counterImage])) {
                                        grid.addComponent(new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                              new ByteArrayInputStream(iterPic.getBytesImage()), "")), i, j-1);
                                        collection.add(iterPic);
                                        counterImage++;
                                        break;
                                    }
                                }
                                if (j % 3 == 2 || j == 2) {
                                    if (iterPic.getTerm().equals(words[counterAudio])) {
                                        grid.addComponent(new Audio("", new StreamResource((StreamResource.StreamSource) () ->
                                              new ByteArrayInputStream(iterPic.getBytesAudio()), "")), i, j-1);
                                        counterAudio++;
                                        break;
                                    }
                                }
                                if (j % 3 == 0) {
                                    if (iterPic.getTerm().equals(words[counter])) {
                                        grid.addComponent(new Label(words[counter]), i, j-1);
                                        counter++;
                                        break;
                                    }
                                }
                                if (!iteratorPic.hasNext())
                                    isMissing = true;
                            }
                        }
                    }
                } else {
                    Notification.show(getString("createClause-notification-not-enough-words"));
                    isMissing = true;
                }
                if (isMissing) {
                    Notification.show(getString("createClause-notification-upload-missing"));
                } else {


                    Button createButton = new MButton(getString("createClause-create-button")).withListener(click -> {
                        addClause(rows, columns, clauseNameField.getValue(), collection);
                        Notification.show(getString("createClause-notification-clause-saved"));
                        build(contentLayout, viewChangeEvent);
                    });

                    Button reloadButton = new MButton(getString("createClause-reload-button")).withListener(click -> {
                        build(contentLayout, viewChangeEvent);
                    });
                    horizontalLayout.addComponents(createButton, reloadButton);
                    horizontalLayout1.addComponent(grid);
                }
            }
        });
        horizontalLayout.addComponent(generateButton);
        Listener listener = (Listener) event -> event.getComponent().setVisible(false);
        generateButton.addListener(listener);
        horizontalLayout.setComponentAlignment(generateButton, Alignment.TOP_LEFT);
        content.addComponent(horizontalLayout1);
        horizontalLayout1.addComponent(verticalLayout);

        verticalLayout.addComponents(row, column, clauseNameField, clauseField, horizontalLayout);
    }

    private void addClause(int row, int column, String name, Collection<Piktogram> collection) {
        boolean isUsed = false;

        Iterable<Clause> clauses = clauseService.getClauses();
        Collection<Clause> clauseCollection = new ArrayList<>();
        for (Clause clause : clauses)
            clauseCollection.add(clause);
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