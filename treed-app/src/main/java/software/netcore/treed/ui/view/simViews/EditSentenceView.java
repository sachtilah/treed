package software.netcore.treed.ui.view.simViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import software.netcore.treed.business.PiktogramService;
import software.netcore.treed.business.SentenceService;
import software.netcore.treed.data.schema.sim.Piktogram;
import software.netcore.treed.data.schema.sim.Sentence;
import software.netcore.treed.ui.TreedCustomComponent;
import software.netcore.treed.ui.view.HomeScreenView;
import software.netcore.treed.ui.view.LoginAttemptView;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.*;

@Slf4j
@SpringView(name = software.netcore.treed.ui.view.simViews.EditSentenceView.VIEW_NAME)
public class EditSentenceView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/edit";
    private VerticalLayout mainLayout;
    private final SentenceService sentenceService;
    private final PiktogramService piktogramService;

    public EditSentenceView(SentenceService sentenceService, PiktogramService piktogramService) {
        this.sentenceService = sentenceService;
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

        Button createSentence = new Button(getString("navigationBar-create-sentence-button"));
        createSentence.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(CreateSentenceView.VIEW_NAME));


        Button homeScreen = new Button(getString("navigationBar-home-button"));
        homeScreen.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(HomeScreenView.VIEW_NAME));


        Label usernameField = new Label("username");

        Button logout = new Button(getString("navigationBar-logout-button"));
        logout.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        content.addComponent(bar);
        bar.addComponents(treed, uploadButton, homeScreen, createSentence, usernameField, logout);

        HorizontalLayout editLayout = new HorizontalLayout();

        Label sentenceLabel = new Label(getString("editSentence-sentence-label"));
        TextField findSentenceField = new TextField();

        Button findButton = new Button(getString("editSentence-find-button"));
        findButton.addClickListener((Button.ClickListener) event -> {
            boolean isFound = false;
            Iterable<Sentence> sentences = sentenceService.getSentences();
            Collection<Sentence> sentenceCollection = new ArrayList<>();
            for (Sentence sentence : sentences) {
                sentenceCollection.add(sentence);
            }
            Iterator<Sentence> iteratorSentence = sentences.iterator();

            while ((iteratorSentence.hasNext()) && !isFound) {
                Sentence iterSentence = iteratorSentence.next();
                if ((iterSentence.getName().equals(findSentenceField.getValue())) && !findSentenceField.isEmpty()) {
                    isFound = true;

                    int c = 0;
                    String[] string = new String[iterSentence.getRowCount()*iterSentence.getColumnCount()];
                    Iterable<Piktogram> pictograms = iterSentence.getPiktograms();
                    Collection<Piktogram> pictogramCollection = new ArrayList<>();
                    for (Piktogram pictogram : pictograms) {
                        pictogramCollection.add(pictogram);
                    }
                    Iterator<Piktogram> iteratorPictogram = pictograms.iterator();
                    while(iteratorPictogram.hasNext()){
                        string[c] = iteratorPictogram.next().getTerm();
                        c++;
                    }

                    TextField sentenceNameField = new TextField(getString("createSentence-sentence-name-field"));
                    sentenceNameField.setValue(iterSentence.getName());
                    TextField row = new TextField(getString("createSentence-row-field"));
                    row.setValue(iterSentence.getRowCount().toString());
                    TextField column = new TextField(getString("createSentence-column-field"));
                    column.setValue(iterSentence.getColumnCount().toString());
                    TextField sentenceField = new TextField(getString("createSentence-sentence-field"));
                    sentenceField.setValue(String.join(" ", string));

                    HorizontalLayout hLayout = new HorizontalLayout();
                    hLayout.removeAllComponents();
                    hLayout.setMargin(true);
                    hLayout.setSpacing(true);
                    hLayout.setSizeFull();

                    HorizontalLayout hLayout2 = new HorizontalLayout();
                    hLayout2.removeAllComponents();
                    hLayout2.setMargin(true);
                    hLayout2.setSpacing(true);
                    hLayout2.setSizeFull();

                    content.addComponents(hLayout, hLayout2);
                    hLayout.addComponents(row, sentenceNameField);
                    hLayout2.addComponents(column, sentenceField);
                    content.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
                    content.setComponentAlignment(hLayout2, Alignment.MIDDLE_CENTER);

                    Button editButton = new MButton(getString("createSentence-edit-button")).withListener(clickEvent -> {

                        boolean isMissing = false;
                        if(row.getValue().isEmpty() || column.getValue().isEmpty() || sentenceNameField.getValue().isEmpty()
                                || sentenceField.getValue().isEmpty()){
                            Notification.show(getString("createSentence-notification-empty-fields"));
                        }
                        else if((!row.getValue().matches("[0-9]+")) || (!column.getValue().matches("[0-9]+"))){
                            Notification.show(getString("createSentence-notification-invalid-fields"));
                        }
                        else {
                            int rows = Integer.parseInt(row.getValue());
                            int columns = Integer.parseInt(column.getValue());
                            int numberOfWords = rows*columns;
                            int counter = 0, counterImage = 0;
                            Collection<Piktogram> collection = new ArrayList<>();

                            String[] words = sentenceField.getValue().split("\\s+");
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
                                                    piktogramCollection.add(iterPic);
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
                                            //    Notification.show("Neni tu take");
                                        }
                                    }
                                }
                            }
                            /*else
                                Notification.show(getString("createSentence-notification-not-enough-words"));*/
                            if(isMissing)
                                Notification.show(getString("createSentence-notification-upload-missing"));
                            else {
                                addSentence(rows, columns, sentenceNameField.getValue(), collection);
                                content.addComponent(grid);
                            }
                        }
                    });
                    content.addComponent(editButton);
                }
                /*else if(findSentenceField.isEmpty())
                    Notification.show(getString("editSentence-notification-find-sentence-empty"));*/
                else
                    Notification.show(getString("editSentence-notification-find-sentence-not-found"));
            }
            });

        content.addComponent(editLayout);
        editLayout.addComponents(sentenceLabel, findSentenceField, findButton);
    }

    private void addSentence (int row, int column, String name, Collection<Piktogram> collection) {
        boolean isUsed = false;

        Iterable<Sentence> sentences = sentenceService.getSentences();
        Collection<Sentence> sentenceCollection = new ArrayList<>();
        for (Sentence sentence : sentences) {
            sentenceCollection.add(sentence);
        }
        Iterator<Sentence> iteratorSentence = sentences.iterator();

        while ((iteratorSentence.hasNext())) {
            Sentence iterSentence = iteratorSentence.next();
            if (iterSentence.getName().equals(name)) {
                sentenceService.deleteSentence(iterSentence);
                Sentence sentenceAdd = new Sentence();
                sentenceAdd.setRowCount(row);
                sentenceAdd.setColumnCount(column);
                sentenceAdd.setName(name);
                sentenceAdd.setPiktograms(collection);
                sentenceAdd.setCreateTime(Date.from(Instant.now()));
                sentenceService.saveSentence(sentenceAdd);
                log.info("updated Sentence");
            }
            else
                Notification.show("createSentence-notification-sentence-name-used");
        }
    }
}

