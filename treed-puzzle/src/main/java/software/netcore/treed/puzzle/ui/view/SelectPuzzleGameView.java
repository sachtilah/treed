package software.netcore.treed.puzzle.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.DragSourceExtension;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.data.schema.puzzle.PictogramPuzzle;
import software.netcore.treed.puzzle.business.PictogramPartService;
import software.netcore.treed.puzzle.business.PictogramPuzzleService;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@SpringView(name =SelectPuzzleGameView.VIEW_NAME)
public class SelectPuzzleGameView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/puzzle/select";
    private MVerticalLayout mainLayout;
    private String nameOfPiktogram="";
    private final PictogramPuzzleService pictogramPuzzleService;

    public SelectPuzzleGameView(PictogramPuzzleService pictogramPuzzleService) {
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
        Iterable<PictogramPuzzle> pics = pictogramPuzzleService.getPics();
        Collection<PictogramPuzzle> pictogramPuzzleCollection = new ArrayList<>();
        for (PictogramPuzzle pictogramPuzzle : pics) {
            pictogramPuzzleCollection.add(pictogramPuzzle);
        }
        int sizeOfSearchPictogram = Math.round(pictogramPuzzleCollection.size()/5)*2;

        Label searchPart = new Label();
        GridLayout searchPictogram = new GridLayout(10,10);
        //GridLayout searchPictogram = new GridLayout(10,sizeOfSearchPictogram);
        searchPictogram.setSpacing(true);
        searchPictogram.setMargin(false);


        searchGrid(searchPictogram,"all", pics);


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


        });


        Panel pictogram = new Panel();
        pictogram.setWidth("500px");
        pictogram.setHeight("300px");

        pictogram.setContent(searchPictogram);
        //pictogram.setContent(new Label("puzzle-pictograms"));
        Button selectButton = new MButton().withListener(clickEvent -> { //nahrat
            getUI().getNavigator().navigateTo(PlayPuzzleView.VIEW_NAME);
        });
        MHorizontalLayout search= new MHorizontalLayout()
                .add(searchPart)
                .add(searchField);
        //search
        MVerticalLayout searchLayout = new MVerticalLayout()
                .withMargin(false)
                .add(search)
                .add(pictogram)
                .add(selectButton);
        //search.setComponentAlignment(pictogram, Alignment.TOP_CENTER);
        searchLayout.setComponentAlignment(search, Alignment.TOP_CENTER);
        searchLayout.setComponentAlignment(pictogram, Alignment.TOP_CENTER);
        searchLayout.setComponentAlignment(selectButton, Alignment.TOP_CENTER);



        Panel panel = new Panel();
        panel.setHeight("600px");

        panel.setContent(searchLayout);
        mainLayout.removeAllComponents();
        mainLayout.add(panel, Alignment.TOP_CENTER);
    }

    private void searchGrid(GridLayout searchPictogram, String search, Iterable<PictogramPuzzle> picso){
        //pics = pictogramPartService.getPics();
        Iterable<PictogramPuzzle> pics = pictogramPuzzleService.getPics();



        Iterator<PictogramPuzzle> iteratorPictogramPuzzle = pics.iterator();
        int j=0;
        searchPictogram.removeAllComponents();

        while (iteratorPictogramPuzzle.hasNext()) {
            for (int i = 0; i < 5; i++) {

                if (iteratorPictogramPuzzle.hasNext()) {
                    PictogramPuzzle iterPic = iteratorPictogramPuzzle.next();
                    if (iterPic.getPictPuzzle().equals(search) || search == "all") {


                        Image picture = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                new ByteArrayInputStream(iterPic.getBytes()), ""));
                        picture.setWidth("80px");
                        picture.setHeight("80px");

                        DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
                        dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

                        dragSourcex.addDragStartListener(e -> {
                            String name= iterPic.getPictPuzzle();
                        });
                        dragSourcex.addDragStartListener(e -> {
                                    dragSourcex.setDragData(picture);
                                    //dragSource.setDragData("bla");
                                    nameOfPiktogram=iterPic.getPictPuzzle();
                                }
                                //dragSource.setDataTransferData("",searchPictogram);
                        );


                        searchPictogram.addComponent(picture, i, j);



                        log.info("added Image " + iterPic.getPictPuzzle());


                        searchPictogram.addComponent(new Label(iterPic.getPictPuzzle()), i, (j + 1));

                        log.info("added Text " + iterPic.getPictPuzzle());

                    }//else {i--;}

                }
            }
            j=j+2;
        }
    }
}
