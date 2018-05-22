package software.netcore.treed.puzzle.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
//import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
//import com.vaadin.ui.dnd.event.DropEvent;
import lombok.extern.slf4j.Slf4j;
//import com.vaadin.ui.components.grid.GridDragSource;
//import com.vaadin.ui.components.grid.GridDropTarget;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
//import com.vaadin.ui.themes.ValoTheme;
//import org.springframework.web.context.WebApplicationContext;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.data.schema.puzzle.PictogramPuzzle;
import software.netcore.treed.puzzle.business.PictogramPartService;
import software.netcore.treed.puzzle.business.PictogramPuzzleService;
//import software.netcore.treed.puzzle.ui.PictogramShow;

//import javax.lang.model.element.Element;
//import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.*;
//import java.util.List;

@Slf4j
@SpringView(name =CreatePuzzleView.VIEW_NAME)
public class CreatePuzzleView extends TreedCustomComponent implements View{
    //private int nu=0;
    private int zpx=0;
    private String nameOfPiktogram="";
    private int widthOfPiktogram=1;
    private int heightOfPiktogram=1;
    public static final String VIEW_NAME = "/puzzle/create";
    private MVerticalLayout mainLayout;
    private final PictogramPuzzleService pictogramPuzzleService;
    private final PictogramPartService pictogramPartService;
    //private final PictogramPuzzleService pictogramPuzzleService;

    public CreatePuzzleView(PictogramPartService pictogramPartService, PictogramPuzzleService pictogramPuzzleService) {
        this.pictogramPartService = pictogramPartService;
        this.pictogramPuzzleService = pictogramPuzzleService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.mainLayout = new MVerticalLayout()
                .withFullSize();
        setCompositionRoot(this.mainLayout);
        setSizeFull();
        this.mainLayout.setMargin(false);
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


        //MVerticalLayout skuska = new MVerticalLayout();

        ImageUploader receiver = new ImageUploader();

        TextField pathField = new MTextField()
                .withFullSize()
                .withWidth("150px")
                .withHeight("35px");


        TextField namePartField = new MTextField()
                .withFullSize()
                .withWidth("150px")
                .withHeight("35px");

        Label namePart = new Label(getString("createPuzzle-name-part-label"));

        /*TextField sizeXPartField = new MTextField()
                .withFullSize()
                .withHeight("35px");
        TextField sizeYPartField = new MTextField()
                .withFullSize()
                .withHeight("35px");*/


/*
// Create a selection component with some items


        ComboBox<String> select1 = new ComboBox<>("My Select");
        select1.setItems("Io", "Europa", "Ganymedes", "Callisto");

// Handle selection event
        select1.addSelectionListener(evet ->{
                Notification.show("Selected " +
                        evet.getSelectedItem());
        }
        );*/


        NativeSelect<String> selectWidthPart =
                new NativeSelect<>(getString("createPuzzle-select-width-part-native-select"));
        selectWidthPart.setEmptySelectionAllowed(false);
        selectWidthPart.setItems("1","2","3","4","5","6","7","8","9","10");

        NativeSelect<String> selectHeightPart =
                new NativeSelect<>(getString("createPuzzle-select-height-part-native-select"));
        selectHeightPart.setEmptySelectionAllowed(false);
        selectHeightPart.setItems("1","2","3","4","5","6","7","8","9","10");

      /*  selectWidthPart.addSelectionListener(event ->{
                Notification.show("Selected " +
                        event.getSelectedItem().orElse("none"));
                //String ite = event.getSelectedItem().toString();
            //ite = event.getSelectedItem().get();

           // if (num.equals(event.getSelectedItem()));
        });*/




        final Upload uploadx = new Upload();
        //uploadx.setCaption(getString("createPuzzle-upload-button"));    //upload
        //receiver.stream=null;
        uploadx.setReceiver(receiver);

        //Image obraz = new Image();
        uploadx.addStartedListener(event -> {
            pathField.setValue(event.getFilename());
        });

        uploadx.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            Image image = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(receiver.stream.toByteArray()), ""));
            succeededEvent.getFilename();
            //receiver.stream.getFilename();
            //skuska.addComponent(image);
            //skuska.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
            //image.setWidth("280px");
            //image.setHeight("280px");
            //uploadx.setButtonCaption("280px");
            //obraz.setData(image);
        });


        //uploadx.StartedEvent();
        //skuska.add(obraz);

        //pathField.setValue("150px");
        uploadx.setButtonCaption(getString("createPuzzle-caption-upload-button"));
        //String infoi = uploadx.getData().toString();
        //uploadx.getDescription().
        //log.info(infoi);


        Button uploadButton = new MButton(getString("createPuzzle-upload-button-button")).withListener(clickEvent -> { //nahrat
            if (selectWidthPart.getValue()==null)
            {selectWidthPart.setSelectedItem("1");}
            if (selectHeightPart.getValue()==null)
            {selectHeightPart.setSelectedItem("1");}

            Iterable<PictogramPart> pics = pictogramPartService.getPics();
            Iterator<PictogramPart> iteratorPictogramPart = pics.iterator();
            boolean allowed=true;


            if(namePartField.getValue().isEmpty())
                Notification.show(getString("createPuzzle-name-part-field-empty-text-field"));
            else if(uploadx == null)
                Notification.show(getString("createPuzzle-name-part-upload-empty-text-field"));
            else {
                while (iteratorPictogramPart.hasNext()) {
                    PictogramPart iterPic = iteratorPictogramPart.next();
                    if (iterPic.getPictPart().equals(namePartField.getValue()))
                        allowed = false;
                }
                if(!allowed)
                    Notification.show(getString("createPuzzle-name-part-field-name-exist"));
                else
                    addNewPictogramPart(receiver.stream.toByteArray(), namePartField.getValue(),
                            Integer.valueOf(selectWidthPart.getValue()), Integer.valueOf(selectWidthPart.getValue()));
            }
        });
    //upload
        GridLayout uploadLayout = new GridLayout(4,4);
            uploadLayout.setSpacing(true);
            uploadLayout.addComponent(uploadx,0,0);
            uploadLayout.addComponent(pathField,1,0);
            uploadLayout.addComponent(namePart,0,1);
            uploadLayout.setComponentAlignment(namePart, Alignment.MIDDLE_RIGHT);
            uploadLayout.addComponent(namePartField,1,1);
            uploadLayout.addComponent(uploadButton,2,1);
            //uploadLayout.addComponent(sizeXPartField,0,2);
            //uploadLayout.addComponent(sizeYPartField,1,2);
            uploadLayout.addComponent(selectWidthPart,0,2);
            uploadLayout.addComponent(selectHeightPart,1,2);


        Iterable<PictogramPart> pics = pictogramPartService.getPics();
        Collection<PictogramPart> pictogramPartCollection = new ArrayList<>();
        for (PictogramPart pictogramPart : pics) {
            pictogramPartCollection.add(pictogramPart);
        }
        /*double c = pictogramPartCollection.size()/5;
        Math.floor(c);*/
        int sizeOfSearchPictogram = (Math.round(pictogramPartCollection.size()/5)+1)*2;
        //if (sizeOfSearchPictogram<2) sizeOfSearchPictogram=2;




        Label searchPart = new Label(getString("createPuzzle-search-part-label"));

        GridLayout searchPictogram = new GridLayout(5,sizeOfSearchPictogram);
        //GridLayout searchPictogram = new GridLayout(10,10);
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



        searchGrid(searchPictogram,"all");

        //Iterator<PictogramPart> iteratorPictogram = pics.iterator();

        //pictogramPartCollection.iterator();




        TextField searchField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        ((MTextField) searchField).withValueChangeListener(event -> {
            /*String sk = "35px";
            Notification.show(sk);*/

            if (searchField.getValue().isEmpty())
            searchGrid(searchPictogram, "all");
            else
                searchGrid(searchPictogram, searchField.getValue());



            //Iterable<PictogramPart> picss = pictogramPartService.getPics();
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


        Panel pictogram = new Panel();
            pictogram.setWidth("450px");
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



        Label namePictogram = new Label(getString("createPuzzle-name-pictogram-label"));

        TextField nameField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        //GRID
        //List<GridLayout> createPictogramList = new ArrayList<>();



        /*GridLayout createPictogram = new GridLayout(10,10);





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
                    picture.setWidth("160px");
                    picture.setHeight("160px");
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
                              int ym = p.y;
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





        //pictograms.

        //poloha
        //namePict[][];
       // int redComponent = colorImage[x][y][RED];
        //namePict[0][0]=("800px","800px");

        //pictograms.add();

        AbsoluteLayout createPictograms = new AbsoluteLayout();
        createPictograms.removeAllComponents();

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

        //List<String[]> pictograms = new ArrayList<>();

       // String[] position = new String[4];
        //Collection<String[]> pictogramsPartPosition = new ArrayList<>();

        String[][] pictograms = new String[10][4];

        DropTargetExtension<AbsoluteLayout> dropTargets = new DropTargetExtension<>(createPictograms);    //umozni prijat
        dropTargets.setDropEffect(DropEffect.MOVE);

        dropTargets.addDropListener(event -> {
            Optional<AbstractComponent> dragSource = event.getDragSourceComponent();

            if (dragSource.isPresent() && dragSource.get() instanceof Image) {

                if (searchField.getValue().isEmpty())
                    searchGrid(searchPictogram, "all");
                else
                    searchGrid(searchPictogram, searchField.getValue());
                //int xm = event.getMouseEventDetails().getClientX();
                //int ym = event.getMouseEventDetails().getClientY();
                /*isfirstitme
                String name=nameOfPiktogram;*/
                int x = event.getMouseEventDetails().getRelativeX();
                int y = event.getMouseEventDetails().getRelativeY();
                //createPictograms.setCursorX(4);
                //createPictograms.setCursorY(4);

                Component picture = dragSource.get();
                //picture.setWidth(80*Integer.valueOf(sizeXPartField.getValue())+"px");
                //picture.setHeight(80*Integer.valueOf(sizeYPartField.getValue())+"px");
                picture.setWidth((80*widthOfPiktogram)+"px");
                picture.setHeight((80*heightOfPiktogram)+"px");
                //int w = UI.getCurrent().getPage().getBrowserWindowWidth();
                //int h = UI.getCurrent().getPage().getBrowserWindowHeight();
                //int h = UI.getCurrent().getPage().getWebBrowser().
                //UI.getCurrent().getPage().getWebBrowser().getScreenWidth();
                //log.info("width " + w + "heigh" + h);
                //log.info(" nazov piktogramu " + picture.getCaption());
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
                int xp =Math.round(x/80);
                int yp =Math.round(y/80);
                //int x=250;
                //int y=100;
                //x=nu+1;
                //y= nu+1;
               //addnum();

                /*log.info(Integer.toString(Math.round(createPictograms.getWidth())));
                log.info(Integer.toString(Math.round(picture.getWidth())));
                log.info(Integer.toString(Math.round(x)));*/

                if (createPictograms.getWidth()>=xp*80+picture.getWidth() && createPictograms.getHeight()>= yp*80+picture.getHeight()){

                //odstranenie

                    /*if (removePictogramPart) {
                        Iterator<String[]> itr = pictogramsPartPosition.iterator();
                        while (itr.hasNext()){
                            if (picture.getCaption()==((ArrayList<String[]>) pictogramsPartPosition).get())
                        }


                        for (String[] elem : pictogramsPartPosition) {
                            log.info(elem[0]);
                            if (elem[0].equals(picture.getCaption())){
                                //pictogramsPartPosition.
                                pictogramsPartPosition.remove();
                            }
                        }*/


                       /* String removeElem = "Perl";
                        List<String> myList = new ArrayList<String>();
                        myList.add("Java");
                        myList.add("Unix");
                        myList.add("Oracle");
                        myList.add("C++");
                        myList.add("Perl");
                        System.out.println("Before remove:");
                        System.out.println(myList);
                        Iterator<String> itr = myList.iterator();
                        while(itr.hasNext()){
                            if(removeElem.equals(itr.next())){
                                itr.remove();
                            }
                        }
                        System.out.println("After remove:");
                        System.out.println(myList);


                        createPictograms.removeComponent(picture);
                    //}*/





                zpx=createPictograms.getComponentCount();
                    if (zpx < 10){


                createPictograms.addComponent(picture, "left: "+80*xp+"px; top: "+80*yp+"px;");
                //int zp = createPictograms.getPosition(picture).getZIndex();

                int zp=createPictograms.getComponentCount();


                //int fuk = Integer.valueOf(namePict[1]);



                       /* if (delete){


                        }else {*/







                  /*      if (zpx != zp) {
                            position[0]=(nameOfPiktogram);
                            position[1]=(Integer.toString(xp));
                            position[2]=(Integer.toString(yp));
                            position[3]=(Integer.toString(zp - 1));
                            pictogramsPartPosition.add(position);
                    } else {


                        for (int i = 0; zp - 1 < i; i++) {
                            if (pictograms[i][0].equals(nameOfPiktogram)) {
                                pictograms[i][1] = (Integer.toString(xp));
                                pictograms[i][2] = (Integer.toString(yp));
                            }
                        }



                    }*/


                            if (zpx != zp) {
                                pictograms[zp - 1][0] = (nameOfPiktogram);
                                pictograms[zp - 1][1] = (Integer.toString(xp));
                                pictograms[zp - 1][2] = (Integer.toString(yp));
                                pictograms[zp - 1][3] = (Integer.toString(zp - 1));
                            } else {
                                for (int i = 0; zp - 1 < i; i++) {
                                    if (pictograms[i][0].equals(nameOfPiktogram)) {
                                        pictograms[i][1] = (Integer.toString(xp));
                                        pictograms[i][2] = (Integer.toString(yp));
                                    }
                                }

                            }
                        log.info("nemo dropnuteho piktogramu "+nameOfPiktogram);
                            log.info(pictograms[zp - 1][0]);
                        log.info(pictograms[zp - 1][1]);
                        log.info(pictograms[zp - 1][2]);
                        log.info(pictograms[zp - 1][3]);



                        //}


                    //log.info("drop position at x "+ (xp) + " y " + yp + " z " + (zp-1)+ " zpx " + zpx);
                    }else{
                        Notification.show(getString("createPuzzle-max-layout-pictogram-reached"));
                    }//pocet casti


            }else{
                    Notification.show(getString("createPuzzle-drop-picture-out-of-bound"));
                }//hranice



                //pictograms.add(namePict);


                //log.info("drop at x "+ (x) + " y " + y);
                //log.info("mouse x "+ (xm) + " y " + ym);
                //log.info("mouse relative x "+ (x) + " y " + y);
                //log.info();
                //addcount(createPictogram);

            }//drag

        });


        Panel createPictogramx = new Panel();
        createPictogramx.setContent(createPictograms);


      /*  DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
        dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

        dragSourcex.addDragStartListener(e -> {
                    dragSourcex.setDragData(picture);
                    //dragSource.setDragData("bla");
                }
                //dragSource.setDataTransferData("",searchPictogram);
        );*/





        Button createButton = new MButton(getString("createPuzzle-create-button-button"))   //vzbrat
                .withListener(clickEvent -> {

                    Iterable<PictogramPuzzle> picst = pictogramPuzzleService.getPics();
                    Iterator<PictogramPuzzle> iteratorPictogramPuzzle = picst.iterator();
                    boolean allowed=true;


                    if(nameField.getValue().isEmpty())
                        Notification.show(getString("createPuzzle-name-field-empty-text-field"));
                    else if(createPictograms.getComponentCount()==0)
                        Notification.show(getString("createPuzzle-empty-create-field"));
                    else {
                        while (iteratorPictogramPuzzle.hasNext()) {
                            PictogramPuzzle iterPic = iteratorPictogramPuzzle.next();
                            if (iterPic.getPictPuzzle().equals(nameField.getValue()))
                                allowed = false;
                        }
                        if(!allowed)
                            Notification.show(getString("createPuzzle-name-field-name-exist"));
                        else
                            Notification.show(getString("createPuzzle-create-successful"));
                        //Collection<AbsoluteLayout> sentenceCollection = new ArrayList<>();
                        //sentenceCollection.add(createPictograms);
                            addNewPictogram(nameField.getValue(), pictograms);
                            //getUI().getNavigator().navigateTo(CreatePuzzleView.VIEW_NAME);---------------------------------------------------
                    }

                    getUI().getNavigator().navigateTo(SelectPuzzleGameView.VIEW_NAME);//---------------------------------------------------
                });
    //create
        MHorizontalLayout path = new MHorizontalLayout()
                .add(namePictogram)
                .add(nameField);

        MVerticalLayout createPictogramLayout = new MVerticalLayout()
                .add(path)
                .add(createPictogramx)
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
                );
                //.add(skuska);
            content.setWidth("100%");

        //
            //imag.receiveUpload("picture", image);
                //pictogram.setComponentAlignment(topcenter, Alignment.TOP_CENTER);



                        /*new StreamResource((StreamResource.StreamSource) () ->
                                new ByteArrayOutputStream(Image),);*/

        //BufferedImage sav = new BufferedImage();
        //ByteArrayOutputStream imagebuffer = null;

        /*createPictograms.getData() = new ByteArrayOutputStream();
        ImageIO.write(image, "png", imagebuffer);
        return new ByteArrayInputStream(
                imagebuffer.toByteArray());*/
        //createPictograms.writeDesign();





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

        Panel panel = new Panel();
        panel.setSizeFull();
        //int w = UI.getCurrent().getPage().getBrowserWindowWidth();
        //int h = UI.getCurrent().getPage().getBrowserWindowHeight();
        MVerticalLayout sk = new MVerticalLayout();
        sk.setSizeFull();
        sk.setMargin(false);
        sk.setSpacing(false);
        //panel.setWidth(100,Unit.PERCENTAGE);
        //panel.setHeight(100,Unit.PERCENTAGE);
        //panel.setHeight("600px");
        //panel.setWidth("100%");
        panel.setContent(content);
        //content.setComponentAlignment();
        //panel.
        sk.addComponent(panel);
        sk.setComponentAlignment(panel, Alignment.TOP_CENTER);
        mainLayout.removeAllComponents();
        mainLayout.add(panel, Alignment.TOP_CENTER);
    }


    private void searchGrid(GridLayout searchPictogram, String search){
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
                    if (iterPic.getPictPart().equals(search) || search.equals("all")) {


                        Image picture = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                new ByteArrayInputStream(iterPic.getBytes()), ""));



                        picture.setWidth("80px");
                        picture.setHeight("80px");

                        DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
                        dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

                        dragSourcex.addDragStartListener(e -> {





                                    dragSourcex.setDragData(picture);
                                    //dragSource.setDragData("bla");
                                    //picture.setCaption(null);
                                    nameOfPiktogram=iterPic.getPictPart();
                                    widthOfPiktogram=iterPic.getWidth();
                                    heightOfPiktogram=iterPic.getHeight();
                                    //nu++;
                                    //log.info("nu " + nu);




                                }
                                //dragSource.setDataTransferData("",searchPictogram);
                        );


                        searchPictogram.addComponent(picture, i, j);
                        //searchPictogram.setCaption(iterPic.getPictPart());


                        
                        log.info("added Image " + iterPic.getPictPart());
                        //log.info("added Image " + searchPictogram.getCaption());


                        searchPictogram.addComponent(new Label(iterPic.getPictPart()), i, (j + 1));

                        log.info("added Text " + iterPic.getPictPart());

                    }//else {i--;}

                }
            }
            j=j+2;
        }
    }

    private void addNewPictogramPart(byte[] bytes, String pictPartName,int width, int height){

        PictogramPart pictogramPartAdd = new PictogramPart();
        pictogramPartAdd.setBytes(bytes);
        pictogramPartAdd.setPictPart(pictPartName);
        pictogramPartAdd.setWidth(width);
        pictogramPartAdd.setHeight(height);
        pictogramPartAdd.setCreateTime(Date.from(Instant.now()));
        pictogramPartService.savePic(pictogramPartAdd);
    }

    private void addNewPictogram(String pictPuzzleName, String[][] position){
        log.info(position[0][0]);
        log.info(position[0][1]);
        log.info(position[0][2]);
        log.info(position[0][3]);

        PictogramPuzzle pictogramPuzzleAdd = new PictogramPuzzle();
        //pictogramPuzzleAdd.setBytes(bytes);
        pictogramPuzzleAdd.setPictPuzzle(pictPuzzleName);
        pictogramPuzzleAdd.setComponents(position);
        pictogramPuzzleAdd.setCreateTime(Date.from(Instant.now()));
        pictogramPuzzleService.savePic(pictogramPuzzleAdd);
    }

    /*private int addnum(){
        //int xa=createPictogram.getCursorX();
        //int ya=createPictogram.getCursorY();ya++;
        //createPictogram.setCursorX(xa);
        //createPictogram.setCursorY(ya);
        nu = nu+20;
        return nu;
    }*/

    /*private void addcount(GridLayout createPictogram){
        //int xa=createPictogram.getCursorX();
        int ya=createPictogram.getCursorY();ya++;
        //createPictogram.setCursorX(xa);
        createPictogram.setCursorY(ya);
    }*/

    private class ImageUploader implements Upload.Receiver {

        private ByteArrayOutputStream stream;

        @Override
        public OutputStream receiveUpload(String filename, String mimeType) {
            stream = new ByteArrayOutputStream();
            return stream;
        }

    }


}

