package software.netcore.treed.puzzle.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.event.DropEvent;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropTarget;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.puzzle.business.PictogramPartService;
import software.netcore.treed.puzzle.ui.PictogramShow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.*;

@Slf4j
@SpringView(name =CreatePuzzleView.VIEW_NAME)
public class CreatePuzzleView extends TreedCustomComponent implements View{

    public static final String VIEW_NAME = "/puzzle/create";
    private MVerticalLayout mainLayout;
    private final PictogramPartService pictogramPartService;
    //private final PictogramPuzzleService pictogramPuzzleService;

    public CreatePuzzleView(PictogramPartService pictogramPartService) {
        this.pictogramPartService = pictogramPartService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        log.info("run enter");
        this.mainLayout = new MVerticalLayout()
                .withFullSize();
        setCompositionRoot(this.mainLayout);
        setSizeFull();
        build();
    }


    /**
     * Build page.
     */
    private void build() {
        /*
         * Objects
         */
        log.info("run build");

        MVerticalLayout skuska = new MVerticalLayout();

        ImageUploader receiver = new ImageUploader();

        TextField pathField = new MTextField(getString("path-puzzle-part-pictogram"))
                .withFullSize()
                .withHeight("35px");

        TextField namePartField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        Label namePart = new Label(getString("name-puzzle-part-pictogram"));

        Upload uploadx = new Upload();
        uploadx.setCaption(getString("selectImage"));    //upload
        uploadx.setReceiver(receiver);

        uploadx.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            Image image = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(receiver.stream.toByteArray()), ""));
            skuska.addComponent(image);
            skuska.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            image.setWidth("280px");
            image.setHeight("280px");
        });

        Button uploadButton = new MButton(getString("upload-puzzle-part")).withListener(clickEvent -> { //nahrat
            if(namePartField.getValue().isEmpty())
                Notification.show(getString("ntfNoTerm"));
            else if(uploadx == null)
                Notification.show(getString("error"));
            else
                addNewPictogramPart(receiver.stream.toByteArray(), namePartField.getValue());
        });
    //upload
        GridLayout uploadLayout = new GridLayout(3,3);
            uploadLayout.setSpacing(true);
            uploadLayout.addComponent(uploadx,0,0);
            uploadLayout.addComponent(pathField,1,0);
            uploadLayout.addComponent(namePart,0,1);
            uploadLayout.setComponentAlignment(namePart, Alignment.MIDDLE_RIGHT);
            uploadLayout.addComponent(namePartField,1,1);
            uploadLayout.addComponent(uploadButton,2,1);

        Iterable<PictogramPart> pics = pictogramPartService.getPics();
        Collection<PictogramPart> pictogramPartCollection = new ArrayList<>();
        for (PictogramPart pictogramPart : pics) {
            pictogramPartCollection.add(pictogramPart);
        }
        int sizeOfSearchPictogram = Math.round(pictogramPartCollection.size()/5)+2;




        Label searchPart = new Label(getString("search-puzzle-part-pictogram"));

        //GridLayout searchPictogram = new GridLayout(10,sizeOfSearchPictogram);
        GridLayout searchPictogram = new GridLayout(10,10);
            searchPictogram.setSpacing(true);
            searchPictogram.setMargin(false);

        //searchGrid(searchPictogram,"all", pics);

        //Iterator<PictogramPart> iteratorPictogram = pics.iterator();

        //pictogramPartCollection.iterator();




        TextField searchField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        ((MTextField) searchField).withValueChangeListener(event -> {
            /*String sk = "35px";
            Notification.show(sk);*/

           /* if (searchField.getValue().isEmpty())
            searchGrid(searchPictogram, "all", pics);
            else
                searchGrid(searchPictogram, searchField.getValue(), pics);*/



            Iterable<PictogramPart> picss = pictogramPartService.getPics();
            searchPictogram.removeAllComponents();
        Iterator<PictogramPart> iteratorPictogram = picss.iterator();
        int k=0;
        while (iteratorPictogram.hasNext()) {
            k++;
            PictogramPart iterPic = iteratorPictogram.next();
            Image picture = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(iterPic.getBytes()), ""));

            log.info("added Image " + iterPic.getPictPart());

            picture.setWidth("80px");
            picture.setHeight("80px");
            searchPictogram.addComponent(picture, (0+k), 0);
            picture = null;
            //pictogramPartService.deletePic(iterPic);
        }




        });


        Panel pictogram = new Panel(getString("puzzle-pictograms"));
            pictogram.setWidth("500px");
            pictogram.setHeight("300px");

        pictogram.setContent(searchPictogram);

    //search
        MVerticalLayout searchLayout = new MVerticalLayout()
                .withMargin(false)
                .add(new MHorizontalLayout()
                        .add(searchPart)
                        .add(searchField)
                )
                .add(pictogram);



        Label namePictogram = new Label(getString("puzzle-pictogram-name"));

        TextField nameField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        //GRID
        GridLayout createPictogram = new GridLayout(10,10);
        createPictogram.addComponent(new Button("R/C 1"));
        for (int i = 0; i < 9; i++) {
            createPictogram.addComponent(new Button("Col " +
                    (createPictogram.getCursorX() + 1)));
        }
        createPictogram.addComponent(new Button("Row "), 9, 9);

        for (int i = 1; i < 10; i++) {
            createPictogram.addComponent(new Button("Row " + i), 0, i);
        }

        createPictogram.addComponent(new Button("3x1 button"), 1, 1, 3, 1);
        createPictogram.addComponent(new Label("1x2 cell"), 1, 2, 1, 3);
        //KONIEC GRID



        Button createButton = new MButton(getString("select-upload-puzzle-part"))   //vzbrat
                .withListener(clickEvent -> {
                    //vzbrat
                });
    //create
        MHorizontalLayout path = new MHorizontalLayout()
                .add(namePictogram)
                .add(nameField);

        MVerticalLayout createPictogramLayout = new MVerticalLayout()
                .add(path)
                .add(createPictogram)
                .add(createButton);
           createPictogramLayout.setComponentAlignment(path, Alignment.TOP_CENTER);
            createPictogramLayout.setComponentAlignment(createButton, Alignment.TOP_CENTER);




        /*//DND
        Label draggableLabel = new Label("You can grab and drag me");
        DragSourceExtension<Label> dragSource = new DragSourceExtension<>(draggableLabel);

    // set the allowed effect
        dragSource.setEffectAllowed(EffectAllowed.MOVE);
    // set the text to transfer
        dragSource.setDataTransferText("hello receiver");

        dragSource.addDragStartListener(event ->
                Notification.show("Drag event started")
        );

        dragSource.addDragEndListener(event -> {
            if (event.isCanceled()) {
                Notification.show("Drag event was canceled");
            } else {
                Notification.show("Drag event finished");
            }
        });

        dragSource.addDragStartListener(event ->
                dragSource.setDragData(draggableLabel)
        );

        VerticalLayout dropTargetLayout = new VerticalLayout();
        dropTargetLayout.setCaption("Drop things inside me");
        dropTargetLayout.addStyleName(ValoTheme.LAYOUT_CARD);

// make the layout accept drops
        DropTargetExtension<VerticalLayout> dropTarget = new DropTargetExtension<>(dropTargetLayout);
        dropTarget.setDropEffect(DropEffect.MOVE);
// catch the drops
        dropTarget.addDropListener(event -> {
            //dropTarget.get
            //DropEvent
            // if the drag source is in the same UI as the target
            //Optional<AbstractComponent> dragSource = event.getDragSourceComponent();

            Optional<AbstractComponent> dragSource1 = event.getDragSourceComponent();
            if (dragSource1.isPresent() && dragSource1.get() instanceof Label) {
                // move the label to the layout
                dropTargetLayout.addComponent(dragSource1.get());
            }

            // handle possible server side drag data, if the drag source was in the same UI

            //event.getDataTransferText();
            //event.getDragSourceComponent();
            //event.getDragData();
           //String lab = event.getDragData().toString();
            //lab=event.getDataTransferText();
            //dropTargetLayout.addComponent(new Label(lab));

        });
        //DND*/


        //------------------------------------------------------------------------testing


                //.withClickShortcut(ShortcutAction.KeyCode.ENTER);



    //main layout
        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                //.withSize()
                //.withSize("800px","600px")
                .add(new MHorizontalLayout()
                        .add(new MVerticalLayout()
                                .add(uploadLayout)
                                .add(searchLayout)
                        )
                        .add(createPictogramLayout)
                )
                .add(skuska);
            content.setWidth("100%");

        //
            //imag.receiveUpload("picture", image);
                //pictogram.setComponentAlignment(topcenter, Alignment.TOP_CENTER);









        uploadx.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            Image image = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(receiver.stream.toByteArray()), ""));

            content.addComponent(image);
            //content.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            image.setWidth("100px");
            image.setHeight("100px");
        });


        //content.add(draggableLabel);
        //content.add(dropTargetLayout);

        Panel panel = new Panel(getString("puzzle-pictograms"));
        panel.setHeight("600px");
        panel.setContent(content);
        mainLayout.removeAllComponents();
        mainLayout.add(panel, Alignment.TOP_LEFT);
    }


    private void searchGrid(GridLayout searchPictogram, String search, Iterable<PictogramPart> picso){
        //pics = pictogramPartService.getPics();
        Iterable<PictogramPart> pics = pictogramPartService.getPics();

        /*searchPictogram.removeAllComponents();
        Iterator<PictogramPart> iteratorPictogram = pics.iterator();
        int k=0;
        while (iteratorPictogram.hasNext()) {
            k++;
            PictogramPart iterPic = iteratorPictogram.next();
            Image picture = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(iterPic.getBytes()), ""));
            picture.setWidth("80px");
            picture.setHeight("80px");
            searchPictogram.addComponent(picture, (3+k), 3);
        }*/


        Iterator<PictogramPart> iteratorPictogramPart = pics.iterator();
        int j=0;
        searchPictogram.removeAllComponents();

        while (iteratorPictogramPart.hasNext()) {
            for (int i = 0; i < 5; i++) {

                if (iteratorPictogramPart.hasNext()) {
                    PictogramPart iterPic = iteratorPictogramPart.next();
                    if (iterPic.getPictPart().equals(search) || search == "all") {


                        Image picture = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                new ByteArrayInputStream(iterPic.getBytes()), ""));
                        picture.setWidth("80px");
                        picture.setHeight("80px");

                        searchPictogram.addComponent(picture, i, j);


                        
                        log.info("added Image " + iterPic.getPictPart() + " with counter: ");


                        searchPictogram.addComponent(new Label(iterPic.getPictPart()), i, (j + 1));

                        log.info("added Text " + iterPic.getPictPart() + " with counter: ");

                    }else {i--;}

                }
            }
            j=j+2;
        }
    }

    private void addNewPictogramPart(byte[] bytes, String pictPartName){

        PictogramPart pictogramPartAdd = new PictogramPart();
        pictogramPartAdd.setBytes(bytes);
        pictogramPartAdd.setPictPart(pictPartName);
        pictogramPartAdd.setCreateTime(Date.from(Instant.now()));
        pictogramPartService.savePic(pictogramPartAdd);
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

