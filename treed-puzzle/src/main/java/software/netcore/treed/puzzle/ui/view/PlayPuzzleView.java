package software.netcore.treed.puzzle.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.puzzle.business.PictogramPartService;
import software.netcore.treed.puzzle.business.PictogramPuzzleService;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
@SpringView(name =PlayPuzzleView.VIEW_NAME)
public class PlayPuzzleView extends TreedCustomComponent implements View {
    private String nameOfPiktogram="";
    public static final String VIEW_NAME = "/puzzle/play";
    private MVerticalLayout mainLayout;
    private final PictogramPartService pictogramPartService;
    private final PictogramPuzzleService pictogramPuzzleService;
    public PlayPuzzleView(PictogramPartService pictogramPartService, PictogramPuzzleService pictogramPuzzleService) {
        this.pictogramPartService = pictogramPartService;
        this.pictogramPuzzleService = pictogramPuzzleService;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        //log.info("run enter");
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
        log.info("run build");


        Iterable<PictogramPart> pics = pictogramPartService.getPics();
        Collection<PictogramPart> pictogramPartCollection = new ArrayList<>();
        for (PictogramPart pictogramPart : pics) {
            pictogramPartCollection.add(pictogramPart);
        }
        int sizeOfSearchPictogram = Math.round(pictogramPartCollection.size()/5)*2;




        Label searchPart = new Label(getString("playPuzzle-search-part-label"));

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



        searchGridPart(searchPictogram,"all", pics);

        //Iterator<PictogramPart> iteratorPictogram = pics.iterator();

        //pictogramPartCollection.iterator();




        TextField searchField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        ((MTextField) searchField).withValueChangeListener(event -> {


            if (searchField.getValue().isEmpty())
                searchGridPart(searchPictogram, "all", pics);
            else
                searchGridPart(searchPictogram, searchField.getValue(), pics);



            Iterable<PictogramPart> picss = pictogramPartService.getPics();


        });


        Panel pictogram = new Panel();
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



        Label namePictogram = new Label();



        //GRID
        //List<GridLayout> createPictogramList = new ArrayList<>();






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
                    searchGridPart(searchPictogram, "all", pics);
                else
                    searchGridPart(searchPictogram, searchField.getValue(), pics);
                int xm = event.getMouseEventDetails().getClientX();
                int ym = event.getMouseEventDetails().getClientY();
                /*isfirstitme
                String name=nameOfPiktogram;*/
                int x = event.getMouseEventDetails().getRelativeX();
                int y = event.getMouseEventDetails().getRelativeY();
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
                log.info(" nazov piktogramu " + nameOfPiktogram);
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

                log.info("drop nasobenie at x "+ (xp) + " y " + yp);
                createPictograms.addComponent(picture, "left: "+80*xp+"px; top: "+80*yp+"px;");
                createPictograms.getPosition(picture);
                log.info("drop at x "+ (x) + " y " + y);
                log.info("mouse x "+ (xm) + " y " + ym);
                log.info("mouse relative x "+ (x) + " y " + y);
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
        MHorizontalLayout path = new MHorizontalLayout()
                .add(namePictogram);


        MVerticalLayout createPictogramLayout = new MVerticalLayout()
                .add(path)
                .add(createPictograms);
                //.add(createPictogram)

        createPictogramLayout.setComponentAlignment(path, Alignment.TOP_CENTER);
        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                //.withSize()
                //.withSize("800px","600px")
                .add(new MHorizontalLayout()
                        .add(new MVerticalLayout()

                                .add(searchLayout)
                        )
                        .add(createPictogramLayout)
                );

        content.setWidth("100%");
        Panel panel = new Panel();
        panel.setHeight("600px");
        panel.setContent(content);
        mainLayout.removeAllComponents();
        mainLayout.add(panel, Alignment.TOP_LEFT);

    }

    public void searchGridPart(GridLayout searchPictogram, String search, Iterable<PictogramPart> picso){
        //pics = pictogramPartService.getPics();
        Iterable<PictogramPart> pics = pictogramPartService.getPics();




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
                            String name= iterPic.getPictPart();
                        });
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

}
