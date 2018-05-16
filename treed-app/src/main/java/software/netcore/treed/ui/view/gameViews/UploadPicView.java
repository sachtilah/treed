package software.netcore.treed.ui.view.gameViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.business.PiktogramService;
import software.netcore.treed.data.schema.sim.Piktogram;
import software.netcore.treed.ui.view.HomeScreenView;
import software.netcore.treed.ui.view.LoginAttemptView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@SpringView(name = software.netcore.treed.ui.view.gameViews.UploadPicView.VIEW_NAME)
public class UploadPicView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/upload";
    private VerticalLayout mainLayout;
    private final PiktogramService piktogramService;

    public UploadPicView(PiktogramService piktogramService) {
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

        HorizontalLayout bar = new HorizontalLayout();
        bar.setWidth("100%");
        Label treed = new Label("<strong>treed</strong>", ContentMode.HTML);

        Button homeScreen = new Button(getString("homeScreen"));
        homeScreen.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(HomeScreenView.VIEW_NAME));

        Button createStory = new Button(getString("createStory"));
        createStory.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(CreateStoryView.VIEW_NAME));

        Label usernameField = new Label("username");

        Button logout = new Button(getString("logout"));
        logout.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(LoginAttemptView.VIEW_NAME));

        content.addComponent(bar);
        bar.addComponents(treed, homeScreen, createStory, usernameField, logout);

        Label text = new Label(getString("uploadText"));
        content.addComponent(text);
        content.setComponentAlignment(text, Alignment.MIDDLE_CENTER);

        TextField termField = new TextField(getString("term"));

        Iterable<Piktogram> pics = piktogramService.getPics();
        Collection<Piktogram> piktogramCollection = new ArrayList<>();
        for (Piktogram piktogram : pics) {
            piktogramCollection.add(piktogram);
        }
        Iterator<Piktogram> iteratorPiktogram = pics.iterator();

        final Embedded preview = new Embedded();
        preview.setVisible(false);

        final Upload upload = new Upload();
        upload.setCaption(getString("selectImage"));

        ImageUploader receiver = new ImageUploader();

        upload.setReceiver(receiver);
        upload.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            Image image = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(receiver.stream.toByteArray()), ""));
            content.addComponent(image);
            content.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            image.setWidth("300px");
            image.setHeight("300px");
        });
        HorizontalLayout layout = new HorizontalLayout();
        layout.removeAllComponents();
        layout.setMargin(true);
        layout.setSpacing(true);
        content.addComponent(layout);
        content.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        layout.addComponents(preview, upload);

        Button createButton = new MButton(getString("send")).withListener(clickEvent -> {
            if(termField.getValue().isEmpty())
                Notification.show(getString("ntfNoTerm"));
            else if(upload == null)
                Notification.show(getString("error"));
            else
                addNewPiktogram(receiver.stream.toByteArray(), termField.getValue());
        });
        layout.addComponents(termField, createButton);
        layout.setComponentAlignment(termField, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(createButton, Alignment.MIDDLE_CENTER);
    }

    private void addNewPiktogram (byte[] bytes, String term){

        Piktogram piktogramAdd = new Piktogram();
        piktogramAdd.setBytes(bytes);
        piktogramAdd.setTerm(term);
        piktogramAdd.setCreateTime(Date.from(Instant.now()));
        piktogramService.savePic(piktogramAdd);
    }

    private class ImageUploader implements Upload.Receiver {

        private ByteArrayOutputStream stream;

        @Override
        public OutputStream receiveUpload(String filename, String mimeType) {
            stream = new ByteArrayOutputStream();
            return stream;
        }
    }

}

