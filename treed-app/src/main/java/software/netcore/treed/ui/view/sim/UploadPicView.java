package software.netcore.treed.ui.view.sim;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.sim.PiktogramService;
import software.netcore.treed.data.schema.sim.Piktogram;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@SpringView(name = software.netcore.treed.ui.view.sim.UploadPicView.VIEW_NAME)
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
        TextField termField_en = new TextField(getString("uploadPic-term-field-en"));
        TextField termField_ro = new TextField(getString("uploadPic-term-field-ro"));
        TextField termField_ru = new TextField(getString("uploadPic-term-field-ru"));

        termField.setWidth("300");
        termField_en.setWidth("300");
        termField_ro.setWidth("300");
        termField_ru.setWidth("300");

        Iterable<Piktogram> pics = piktogramService.getPics();
        Collection<Piktogram> piktogramCollection = new ArrayList<>();
        for (Piktogram piktogram : pics) {
            piktogramCollection.add(piktogram);
        }

        final Embedded preview = new Embedded();
        preview.setVisible(false);

        final Upload uploadImage = new Upload();
        uploadImage.setCaption(getString("uploadPic-select-image-caption"));

        ComponentUploader receiverImage = new ComponentUploader();

        uploadImage.setReceiver(receiverImage);
        uploadImage.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            Image image = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(receiverImage.stream.toByteArray()), succeededEvent.getFilename()));
            content.addComponent(image);
            content.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            image.setWidth("250px");
            image.setHeight("191px");
            uploadImage.setCaption(succeededEvent.getFilename());
        });
        content.addComponents(preview, uploadImage);
        content.setComponentAlignment(uploadImage, Alignment.MIDDLE_CENTER);

        final Upload uploadAudio = new Upload();
        uploadAudio.setCaption(getString("uploadPic-select-image-caption"));
        ComponentUploader receiverAudio = new ComponentUploader();
        uploadAudio.setReceiver(receiverAudio);
        uploadAudio.addSucceededListener((Upload.SucceededListener) succeededEvent ->{
           Audio audio = new Audio("", new StreamResource((StreamResource.StreamSource) () ->
                 new ByteArrayInputStream(receiverAudio.stream.toByteArray()), succeededEvent.getFilename()));
           content.addComponent(audio);
           content.setComponentAlignment(audio, Alignment.MIDDLE_CENTER);
           uploadAudio.setCaption(succeededEvent.getFilename());
           audio.play();
        });
        content.addComponent(uploadAudio);
        content.setComponentAlignment(uploadAudio, Alignment.MIDDLE_CENTER);

        Button createButton = new MButton(getString("uploadPic-create-button")).withListener(clickEvent -> {
            if (termField.getValue().isEmpty())
                Notification.show(getString("uploadPic-notification-no-term"));
            else {
                addNewPiktogram(receiverImage.stream.toByteArray(), receiverAudio.stream.toByteArray(), termField.getValue(), termField_en.getValue(),
                      termField_ro.getValue(), termField_ru.getValue());
                Notification.show(getString("uploadPic-notification-successfully-saved"), Notification.Type.ASSISTIVE_NOTIFICATION);
                build(contentLayout, viewChangeEvent);
            }
        });

        content.addComponents(termField, termField_en, termField_ro, termField_ru);
        content.addComponent(createButton);

        content.setComponentAlignment(termField_en, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(termField_ro, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(termField_ru, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(termField, Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(createButton, Alignment.BOTTOM_CENTER);
    }

    private void addNewPiktogram(byte[] bytesImage, byte[] bytesAudio, String term, String term_en, String term_ro, String term_ru) {

        Piktogram piktogramAdd = new Piktogram();
        piktogramAdd.setBytesImage(bytesImage);
        piktogramAdd.setBytesAudio(bytesAudio);
        piktogramAdd.setTerm(term);
        piktogramAdd.setTerm_en(term_en);
        piktogramAdd.setTerm_ro(term_ro);
        piktogramAdd.setTerm_ru(term_ru);
        piktogramAdd.setCreateTime(Date.from(Instant.now()));
        piktogramService.savePic(piktogramAdd);
    }

    private class ComponentUploader implements Upload.Receiver {

        private ByteArrayOutputStream stream;

        @Override
        public OutputStream receiveUpload(String filename, String mimeType) {
            stream = new ByteArrayOutputStream();
            return stream;
        }
    }

}

