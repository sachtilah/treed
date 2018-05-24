package software.netcore.treed.ui.view.simViews;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.PiktogramService;
import software.netcore.treed.data.schema.sim.Piktogram;
import software.netcore.treed.ui.view.LoginAttemptView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@SpringView(name = software.netcore.treed.ui.view.simViews.UploadPicView.VIEW_NAME)
public class UploadPicView extends AbstractSimView implements View {

    public static final String VIEW_NAME = "/upload";
    private final PiktogramService piktogramService;

    public UploadPicView(PiktogramService piktogramService) {
        this.piktogramService = piktogramService;
    }

    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        MVerticalLayout content = new MVerticalLayout();
        contentLayout.removeAllComponents();
        contentLayout.add(content);

        MLabel text = new MLabel(getString("uploadPic-upload-label"))
                .withStyleName(ValoTheme.LABEL_H2);
        content.addComponent(text);
        content.setComponentAlignment(text, Alignment.MIDDLE_CENTER);

        TextField termField = new TextField(getString("uploadPic-term-field"));

        Iterable<Piktogram> pics = piktogramService.getPics();
        Collection<Piktogram> piktogramCollection = new ArrayList<>();
        for (Piktogram piktogram : pics) {
            piktogramCollection.add(piktogram);
        }

        final Embedded preview = new Embedded();
        preview.setVisible(false);

        final Upload upload = new Upload();
        upload.setCaption(getString("uploadPic-select-image-caption"));

        ImageUploader receiver = new ImageUploader();

        upload.setReceiver(receiver);
        upload.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            Image image = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(receiver.stream.toByteArray()), ""));
            content.addComponent(image);
            content.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            image.setWidth("250px");
            image.setHeight("191px");
        });
        HorizontalLayout layout = new HorizontalLayout();
        layout.removeAllComponents();
        layout.setMargin(true);
        layout.setSpacing(true);
        content.addComponent(layout);
        content.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        layout.addComponents(preview, upload);

        Button createButton = new MButton(getString("uploadPic-create-button")).withListener(clickEvent -> {
            if (termField.getValue().isEmpty())
                Notification.show(getString("uploadPic-notification-no-term"));
            else {
                addNewPiktogram(receiver.stream.toByteArray(), termField.getValue());
                Notification.show(getString("uploadPic-notification-successfully-saved"), Notification.Type.ASSISTIVE_NOTIFICATION);
                build(contentLayout, viewChangeEvent);
            }
        });
        layout.addComponents(termField, createButton);
        layout.setComponentAlignment(termField, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(createButton, Alignment.BOTTOM_CENTER);
    }

    private void addNewPiktogram(byte[] bytes, String term) {

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

