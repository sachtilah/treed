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
import java.util.*;

@Slf4j
@SpringView(name = software.netcore.treed.ui.view.sim.EditClauseView.VIEW_NAME)
public class EditClauseView extends AbstractSimView implements View {

    public static final String VIEW_NAME = "/edit";
    private final ClauseService clauseService;
    private final PiktogramService piktogramService;

    public EditClauseView(ClauseService clauseService, PiktogramService piktogramService) {
        this.clauseService = clauseService;
        this.piktogramService = piktogramService;
    }

    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        VerticalLayout content = new MVerticalLayout();
        contentLayout.removeAllComponents();
        contentLayout.add(content);

        HorizontalLayout editLayout = new HorizontalLayout();

        Label clauseLabel = new Label(getString("editClause-clause-label"));
        TextField findClauseField = new TextField();

        Button findButton = new Button(getString("editClause-find-button"));
        findButton.addClickListener((Button.ClickListener) event -> {
            boolean isFound = false;
            Iterable<Clause> clauses = clauseService.getClauses();
            Collection<Clause> clauseCollection = new ArrayList<>();
            for (Clause clause : clauses) {
                clauseCollection.add(clause);
            }
            Iterator<Clause> iteratorClause = clauses.iterator();

            while ((iteratorClause.hasNext()) && !isFound) {
                Clause iterClause = iteratorClause.next();
                System.out.println(iterClause);

                if ((iterClause.getName().equals(findClauseField.getValue())) && !findClauseField.isEmpty()) {
                    isFound = true;

                    int c = 0;
                    String[] string = new String[iterClause.getRowCount() * iterClause.getColumnCount()];
                    Iterable<Piktogram> pictograms = iterClause.getPiktograms();
                    Collection<Piktogram> pictogramCollection = new ArrayList<>();
                    for (Piktogram pictogram : pictograms) {
                        pictogramCollection.add(pictogram);
                    }
                    Iterator<Piktogram> iteratorPictogram = pictograms.iterator();
                    while (iteratorPictogram.hasNext()) {
                        string[c] = iteratorPictogram.next().getTerm();
                        c++;
                    }

                    TextField clauseNameField = new TextField(getString("createClause-clause-name-field"));
                    clauseNameField.setValue(iterClause.getName());
                    TextField row = new TextField(getString("createClause-row-field"));
                    row.setValue(iterClause.getRowCount().toString());
                    TextField column = new TextField(getString("createClause-column-field"));
                    column.setValue(iterClause.getColumnCount().toString());
                    TextField clauseField = new TextField(getString("createClause-clause-field"));
                    clauseField.setValue(String.join(" ", string));

                    VerticalLayout verticalLayout = new VerticalLayout();
                    verticalLayout.removeAllComponents();
                    verticalLayout.setMargin(true);
                    verticalLayout.setSpacing(true);
                    verticalLayout.setSizeFull();

                    Button editButton = new MButton(getString("createClause-edit-button")).withListener(clickEvent -> {

                        boolean isMissing = false;
                        if (row.getValue().isEmpty() || column.getValue().isEmpty() || clauseNameField.getValue().isEmpty()
                                || clauseField.getValue().isEmpty()) {
                            Notification.show(getString("createClause-notification-empty-fields"));
                        } else if ((!row.getValue().matches("[0-9]+")) || (!column.getValue().matches("[0-9]+"))) {
                            Notification.show(getString("createClause-notification-invalid-fields"));
                        } else {
                            int rows = Integer.parseInt(row.getValue());
                            int columns = Integer.parseInt(column.getValue());
                            int numberOfWords = rows * columns;
                            int counter = 0, counterImage = 0;
                            Collection<Piktogram> collection = new ArrayList<>();

                            String[] words = clauseField.getValue().split("\\s+");
                            GridLayout grid = new GridLayout(columns, rows * 2);
                            grid.removeAllComponents();
                            if (numberOfWords == words.length) {
                                for (int j = 0; j < rows * 2; j++) {
                                    for (int i = 0; i < columns; i++) {
                                        Iterable<Piktogram> piktograms = piktogramService.getPics();
                                        Collection<Piktogram> piktogramCollection = new ArrayList<>();
                                        for (Piktogram piktogram : piktograms) {
                                            collection.add(piktogram);
                                        }
                                        Iterator<Piktogram> iteratorPic = piktograms.iterator();

                                        while ((iteratorPic.hasNext())) {
                                            Piktogram iterPic = iteratorPic.next();
                                            if (j % 2 == 0) {
                                                if ((iterPic.getTerm().equals(words[counterImage])) && (counterImage < numberOfWords)) {
                                                    grid.addComponent(new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                                            new ByteArrayInputStream(iterPic.getBytesImage()), "")), i, j);
                                                    piktogramCollection.add(iterPic);
                                                    counterImage++;
                                                    break;
                                                }
                                            }
                                            if (j % 2 == 1) {
                                                if ((iterPic.getTerm().equals(words[counter])) && (counter < numberOfWords)) {
                                                    grid.addComponent(new Label(words[counter]), i, j);
                                                    counter++;
                                                    break;
                                                }
                                            }
                                            if (!iteratorPic.hasNext())
                                                isMissing = true;
                                        }
                                    }
                                }
                            }
                            if (isMissing)
                                Notification.show(getString("createClause-notification-upload-missing"));
                            else {
                                addClause(rows, columns, clauseNameField.getValue(), collection);
                                HorizontalLayout horizontalLayout = new HorizontalLayout();
                                content.addComponent(horizontalLayout);
                                horizontalLayout.addComponents(verticalLayout, grid);
                            }
                        }
                    });
                    content.addComponent(verticalLayout);
                    verticalLayout.addComponents(row, column, clauseNameField, clauseField, editButton);
                    content.setComponentAlignment(verticalLayout, Alignment.MIDDLE_LEFT);
                } else if (findClauseField.isEmpty())
                    Notification.show(getString("editClause-notification-find-clause-empty"));
                /*else
                    Notification.show(getString("editClause-notification-find-clause-not-found"));*/
            }
        });
        content.addComponent(editLayout);
        editLayout.addComponents(clauseLabel, findClauseField, findButton);
    }

    private void addClause(int row, int column, String name, Collection<Piktogram> collection) {
        Iterable<Clause> clauses = clauseService.getClauses();
        Collection<Clause> clauseCollection = new ArrayList<>();
        for (Clause clause : clauses) {
            clauseCollection.add(clause);
        }
        Iterator<Clause> iteratorClause = clauses.iterator();

        while ((iteratorClause.hasNext())) {
            Clause iterClause = iteratorClause.next();
            if (iterClause.getName().equals(name)) {
                clauseService.deleteClause(iterClause);
                Clause clauseAdd = new Clause();
                clauseAdd.setRowCount(row);
                clauseAdd.setColumnCount(column);
                clauseAdd.setName(name);
                clauseAdd.setPiktograms(collection);
                clauseAdd.setCreateTime(Date.from(Instant.now()));
                clauseService.saveClause(clauseAdd);
                log.info("updated Clause");
            }
        }
    }
}

