package software.netcore.treed.ui.view.gameViews;

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
import software.netcore.treed.business.SentenceService;
import software.netcore.treed.data.schema.sim.Piktogram;
import software.netcore.treed.data.schema.sim.Sentence;
import software.netcore.treed.ui.view.HomeScreenView;
import software.netcore.treed.ui.view.LoginAttemptView;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
@SpringView(name = software.netcore.treed.ui.view.gameViews.CreateStoryView.VIEW_NAME)
public class CreateStoryView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/story";
    private VerticalLayout mainLayout;
    private final SentenceService sentenceService;
    private final PiktogramService piktogramService;

    public CreateStoryView(SentenceService sentenceService, PiktogramService piktogramService) {
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


        Button uploadButton = new Button(getString("upload"));
        uploadButton.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        Button homeScreen = new Button(getString("homeScreen"));
        homeScreen.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(HomeScreenView.VIEW_NAME));


        Label usernameField = new Label("username");

        Button logout = new Button(getString("logout"));
        logout.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        content.addComponent(bar);
        bar.addComponents(treed, uploadButton, homeScreen, usernameField, logout);

        TextField storyNameField = new TextField(getString("createStory-story-name-field"));
        TextField row = new TextField("Number of rows");
        TextField column = new TextField("Number of columns");
        TextField sentenceField = new TextField("Sentence");

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
        hLayout.addComponents(row, storyNameField);
        hLayout2.addComponents(column, sentenceField);
        content.setComponentAlignment(hLayout, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(hLayout2, Alignment.MIDDLE_CENTER);

        Button generateButton = new MButton(getString("createStory-generate-button")).withListener(clickEvent -> {

            if(row.getValue().isEmpty() || column.getValue().isEmpty() || storyNameField.getValue().isEmpty()
                    || sentenceField.getValue().isEmpty()){
                Notification.show("createStory-notification-empty-fields");
            }else {

                int rows = Integer.parseInt(row.getValue());
                int columns = Integer.parseInt(column.getValue());
                int numberOfWords = rows*columns;
                int counter = 0;

                String[] words = sentenceField.getValue().split("\\s+");
                GridLayout grid = new GridLayout(columns, rows*2);
                grid.removeAllComponents();
                if(numberOfWords == words.length){

                    for (int j = 0; j < rows*2; j++) {
                        for (int i = 0; i < columns; i++) {
                            boolean wasThere = false, wasThereImage = false;
                            Iterable<Piktogram> piktograms = piktogramService.getPics();
                            Collection<Piktogram> piktogramCollection = new ArrayList<>();
                            for (Piktogram piktogram : piktograms) {
                                piktogramCollection.add(piktogram);
                            }
                            Iterator<Piktogram> iteratorPic = piktograms.iterator();

                            while(iteratorPic.hasNext() && (counter < numberOfWords)) {
                                Piktogram iterPic = iteratorPic.next();
                                if (iterPic.getTerm().equals(words[counter])) {
                                    if (!wasThereImage &&(j % 2 == 0)) {
                                        grid.addComponent(new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                                new ByteArrayInputStream(iterPic.getBytes()), "")), i, j);
                                        wasThereImage = true;
                                        log.info("added Image " + iterPic.getTerm() + " with counter: " + counter);
                                    }
                                    if(!wasThere && (j % 2 == 1)){
                                        grid.addComponent(new TextField(words[counter]), i, j);
                                        counter++;
                                        wasThere = true;
                                        log.info("added Text " + iterPic.getTerm() + " with counter: " + counter);
                                    }
                                }
                            }
                        }
                    }
                }
                content.addComponent(grid);
            }
        });
        content.addComponent(generateButton);

    }

    private void addStory (int row, int column, String name, Collection<Piktogram> story){

        Sentence sentenceAdd = new Sentence();
        sentenceAdd.setRowCount(row);
        sentenceAdd.setColumnCount(column);
        sentenceAdd.setName(name);
        sentenceAdd.setPiktograms(story);
        sentenceAdd.setCreateTime(Date.from(Instant.now()));
        sentenceService.saveStory(sentenceAdd);
    }

}

