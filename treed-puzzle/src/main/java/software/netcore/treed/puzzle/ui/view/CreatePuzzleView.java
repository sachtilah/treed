package software.netcore.treed.puzzle.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.dnd.event.DropEvent;
import lombok.extern.slf4j.Slf4j;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropTarget;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.web.context.WebApplicationContext;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.puzzle.business.PictogramPartService;
import software.netcore.treed.puzzle.ui.PictogramShow;

import javax.lang.model.element.Element;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.*;
import java.util.List;

@Slf4j
@SpringView(name =CreatePuzzleView.VIEW_NAME)
public class CreatePuzzleView extends TreedCustomComponent implements View{
    private int nu=0;
    private String nameOfPiktogram="";
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

        /*WebApplicationContext ctx = (WebApplicationContext)treed.getContext();
        //int width Browser.getClientWidth();
        int width = ctx.getBrowser().getScreenWidth();
        int height = ctx.getBrowser().getScreenHeight();
        ctx.*/

        /*Button btn = new Button("The Button", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                event.getButton().setCaption(String.valueOf(getMainWindow().getWidth()));
            }
        });*/
       // ((WebApplicationContext)getContext()).getBrowser().getScreenWidth()

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





// Create a selection component with some items


        ComboBox<String> select1 = new ComboBox<>("My Select");
        select1.setItems("Io", "Europa", "Ganymedes", "Callisto");

// Handle selection event
        select1.addSelectionListener(evet ->{
                Notification.show("Selected " +
                        evet.getSelectedItem());
        }
        );




        String num = "N";
// Create the selection component
        NativeSelect<String> select2 =
                new NativeSelect<>("Native Selection");

// Add some items
        select2.setItems("Mercury", "Venus");

        select2.addSelectionListener(event ->{
                Notification.show("Selected " +
                        event.getSelectedItem().orElse("none"));
            if (num.equals(event.getSelectedItem()));
        });




        final Upload uploadx = new Upload();
        uploadx.setCaption(getString("selectImage"));    //upload
        //receiver.stream=null;
        uploadx.setReceiver(receiver);

        //Image obraz = new Image();

        uploadx.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            Image image = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(receiver.stream.toByteArray()), ""));

            skuska.addComponent(image);
            skuska.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            image.setWidth("280px");
            image.setHeight("280px");
            //uploadx.setButtonCaption("280px");
            //obraz.setData(image);
        });

        //skuska.add(obraz);

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

        uploadLayout.addComponent(select1);uploadLayout.addComponent(select2);


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


           /* DragSourceExtension<GridLayout> dragSource = new DragSourceExtension<>(searchPictogram);    //umozny drag
            dragSource.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

            dragSource.addDragStartListener(event -> {
                        dragSource.setDragData(searchPictogram.getComponent(0,0));
                        //dragSource.setDragData("bla");
                    }
                    //dragSource.setDataTransferData("",searchPictogram);
        );*/



        searchGrid(searchPictogram,"all", pics);

        //Iterator<PictogramPart> iteratorPictogram = pics.iterator();

        //pictogramPartCollection.iterator();




        TextField searchField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        ((MTextField) searchField).withValueChangeListener(event -> {
            /*String sk = "35px";
            Notification.show(sk);*/

            if (searchField.getValue().isEmpty())
            searchGrid(searchPictogram, "all", pics);
            else
                searchGrid(searchPictogram, searchField.getValue(), pics);



            Iterable<PictogramPart> picss = pictogramPartService.getPics();
           /* searchPictogram.removeAllComponents();
        Iterator<PictogramPart> iteratorPictogram = picss.iterator();
        int k=0;
        while (iteratorPictogram.hasNext()) {
            k++;
            PictogramPart iterPic = iteratorPictogram.next();
            Image picture = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(iterPic.getBytes()), ""));
            //Image picture = new Image("",new StreamResource((StreamResource.StreamSource) new ByteArrayInputStream(iterPic.getBytes()), ""));

            log.info("added Image " + iterPic.getPictPart());

            picture.setWidth("80px");
            picture.setHeight("80px");


            DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
            dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

            dragSourcex.addDragStartListener(e -> {
                        dragSourcex.setDragData(picture);
                        //dragSource.setDragData("bla");
                    }
                    //dragSource.setDataTransferData("",searchPictogram);
            );

            searchPictogram.addComponent(picture, (0+k), 0);
            //picture = null;
            //pictogramPartService.deletePic(iterPic);
        }*/








        });


        Panel pictogram = new Panel(getString("puzzle-pictograms"));
            pictogram.setWidth("500px");
            pictogram.setHeight("300px");

        pictogram.setContent(searchPictogram);
        //pictogram.setContent(new Label("puzzle-pictograms"));

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
        //List<GridLayout> createPictogramList = new ArrayList<>();



        GridLayout createPictogram = new GridLayout(10,10);





        createPictogram.setHeight("800px");
        createPictogram.setWidth("800px");

        DropTargetExtension<GridLayout> dropTarget = new DropTargetExtension<>(createPictogram);    //umozni prijat
                dropTarget.setDropEffect(DropEffect.MOVE);
                dropTarget.addDropListener(event -> {
                                Optional<AbstractComponent> dragSource = event.getDragSourceComponent();

                          if (dragSource.isPresent() && dragSource.get() instanceof Image) {

                                if (searchField.getValue().isEmpty())
                                    searchGrid(searchPictogram, "all", pics);
                                 else
                                     searchGrid(searchPictogram, searchField.getValue(), pics);
                    int xm = event.getMouseEventDetails().getClientX();
                    int ym = event.getMouseEventDetails().getClientY();
                    //createPictogram.setCursorX(4);
                    //createPictogram.setCursorY(4);
                    Component picture = dragSource.get();
                    picture.setWidth("80px");
                    picture.setHeight("80px");
                              int w = UI.getCurrent().getPage().getBrowserWindowWidth();
                              int h = UI.getCurrent().getPage().getBrowserWindowHeight();
                              //int h = UI.getCurrent().getPage().getWebBrowser().
                              //UI.getCurrent().getPage().getWebBrowser().getScreenWidth();
                              log.info("width " + w + "heigh" + h);
                              log.info(" nazov piktogramu" + nameOfPiktogram);
                              /*PointerInfo a = MouseInfo.getPointerInfo();
                              Point b = a.getLocation();
                              int xm = (int) b.getX();
                              int ym = (int) b.getY();*/
                             // System.out.print(ym + "jjjjjjjjj");
                             // System.out.print(xm);
                              /*Point p = MouseInfo.getPointerInfo().getLocation();
                              int xm = p.x;
                              int ym = p.y;*/
                    int x=createPictogram.getCursorX();
                    int y=createPictogram.getCursorY();
                    log.info("drop at x "+ (x) + " y " + y);
                    createPictogram.addComponent(picture,x,y);
                    log.info("drop at x "+ (x) + " y " + y);
                              log.info("mouse x "+ (xm) + " y " + ym);
                    //addcount(createPictogram);

                            }

                });
               /* dropTarget.addDropListener(e -> {
                        createPictogramList.add(createPictogram);
                }
                );*/


        AbsoluteLayout createPictograms = new AbsoluteLayout();

        /*createPictograms.getPosition();

        DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
        dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

        dragSourcex.addDragStartListener(e -> {
                    dragSourcex.setDragData(picture);
                    //dragSource.setDragData("bla");
                    //nameOfPiktogram;
                }
                //dragSource.setDataTransferData("",searchPictogram);
        );*/


        createPictograms.setHeight("800px");
        createPictograms.setWidth("800px");

        DropTargetExtension<AbsoluteLayout> dropTargets = new DropTargetExtension<>(createPictograms);    //umozni prijat
        dropTargets.setDropEffect(DropEffect.MOVE);
        dropTargets.addDropListener(event -> {
            Optional<AbstractComponent> dragSource = event.getDragSourceComponent();

            if (dragSource.isPresent() && dragSource.get() instanceof Image) {

                if (searchField.getValue().isEmpty())
                    searchGrid(searchPictogram, "all", pics);
                else
                    searchGrid(searchPictogram, searchField.getValue(), pics);
                int xm = event.getMouseEventDetails().getClientX();
                int ym = event.getMouseEventDetails().getClientY();
                //createPictograms.setCursorX(4);
                //createPictograms.setCursorY(4);

                Component picture = dragSource.get();
                picture.setWidth("80px");
                picture.setHeight("80px");
                int w = UI.getCurrent().getPage().getBrowserWindowWidth();
                int h = UI.getCurrent().getPage().getBrowserWindowHeight();
                //int h = UI.getCurrent().getPage().getWebBrowser().
                //UI.getCurrent().getPage().getWebBrowser().getScreenWidth();
                log.info("width " + w + "heigh" + h);
                log.info(" nazov piktogramu" + nameOfPiktogram);
                              /*PointerInfo a = MouseInfo.getPointerInfo();
                              Point b = a.getLocation();
                              int xm = (int) b.getX();
                              int ym = (int) b.getY();*/
                // System.out.print(ym + "jjjjjjjjj");
                // System.out.print(xm);
                              /*Point p = MouseInfo.getPointerInfo().getLocation();
                              int xm = p.x;
                              int ym = p.y;*/
                //int x=createPictograms.getCursorX();
                //int y=createPictograms.getCursorY();


                int x=250;
                int y=100;
                x=nu;
               addnum();
                log.info("drop def at x "+ (x) + " y " + y);
                createPictograms.addComponent(picture, "left: "+x+"px; top: "+y+"px;");
                createPictograms.getPosition(picture);
                log.info("drop at x "+ (x) + " y " + y);
                log.info("mouse x "+ (xm) + " y " + ym);
                //log.info();
                //addcount(createPictogram);

            }

        });




      /*  DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
        dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu-------------------------------------

        dragSourcex.addDragStartListener(e -> {
                    dragSourcex.setDragData(picture);
                    //dragSource.setDragData("bla");
                }
                //dragSource.setDataTransferData("",searchPictogram);
        );*/




        /*createPictogram.addComponent(new Button("R/C 1"));
        for (int i = 0; i < 9; i++) {
            createPictogram.addComponent(new Button("Col " +
                    (createPictogram.getCursorX() + 1)));
        }
        createPictogram.addComponent(new Button("Row "), 9, 9);

        for (int i = 0; i < 9; i++) {
            createPictogram.addComponent(new Button("Row " + i), 0, i+1);
        }*/

        //createPictogram.addComponent(new Button("3x1 button"), 1, 1, 3, 1);
        //createPictogram.addComponent(new Label("1x2 cell"), 1, 2, 1, 3);
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
                .add(createPictograms)
                //.add(createPictogram)
                .add(createButton);
           createPictogramLayout.setComponentAlignment(path, Alignment.TOP_CENTER);
            createPictogramLayout.setComponentAlignment(createButton, Alignment.TOP_CENTER);




        /*DragSourceExtension<GridLayout> dragSourceb = new DragSourceExtension<>(searchPictogram);    //umozny drag
        dragSourceb.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

        dragSourceb.addDragStartListener(event -> {
                    dragSourceb.setDragData(searchPictogram.getComponent(0,0));

                    //dragSource.setDragData("bla");
                }
                //dragSource.setDataTransferData("",searchPictogram);
        );*/

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
        //searchPictogram.addComponent(createPictogram.getComponent(0,0),4,4);
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

                        DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
                        dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

                        dragSourcex.addDragStartListener(e -> {
                                    dragSourcex.setDragData(picture);
                                    //dragSource.setDragData("bla");
                                    nameOfPiktogram=iterPic.getPictPart();
                                }
                                //dragSource.setDataTransferData("",searchPictogram);
                        );


                        searchPictogram.addComponent(picture, i, j);


                        
                        log.info("added Image " + iterPic.getPictPart());


                        searchPictogram.addComponent(new Label(iterPic.getPictPart()), i, (j + 1));

                        log.info("added Text " + iterPic.getPictPart());

                    }//else {i--;}

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

    private int addnum(){
        //int xa=createPictogram.getCursorX();
        //int ya=createPictogram.getCursorY();ya++;
        //createPictogram.setCursorX(xa);
        //createPictogram.setCursorY(ya);
        nu = nu+20;
        return nu;
    }

    private void addcount(GridLayout createPictogram){
        //int xa=createPictogram.getCursorX();
        int ya=createPictogram.getCursorY();ya++;
        //createPictogram.setCursorX(xa);
        createPictogram.setCursorY(ya);
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

