package software.netcore.treed.ui.view.puzzle;

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
import software.netcore.treed.business.puzzle.PictogramPartService;
import software.netcore.treed.business.puzzle.PictogramPuzzleService;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.data.schema.puzzle.PictogramPuzzle;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
@SpringView(name =PlayPuzzleView.VIEW_NAME)
public class PlayPuzzleView extends AbstractPuzzleView implements View {

    public static final String VIEW_NAME = "/puzzle/play";

    private String nameOfPiktogram="";
    private int widthOfPiktogram=1;
    private String pokec="skuska2";//----------------------------------------------------------------------------------------------------
    private int heightOfPiktogram=1;
    private boolean isCorrect = true;
    private static String selectedPictogram="skuska3";
    private String[][] componentOfPictogram = new String[10][4];
    private final PictogramPartService pictogramPartService;
    private final PictogramPuzzleService pictogramPuzzleService;

    public PlayPuzzleView(PictogramPartService pictogramPartService, PictogramPuzzleService pictogramPuzzleService) {
        this.pictogramPartService = pictogramPartService;
        this.pictogramPuzzleService = pictogramPuzzleService;
    }

    /**
     * Build page.
     */
    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Iterable<PictogramPuzzle> picsPuzzle = pictogramPuzzleService.getPics();
        Iterator<PictogramPuzzle> iteratorPictogramPuzzle = picsPuzzle.iterator();
        while (iteratorPictogramPuzzle.hasNext()) {
            PictogramPuzzle iterPicPuzzle = iteratorPictogramPuzzle.next();
            if (iterPicPuzzle.getPictPuzzle().equals(selectedPictogram)){
                componentOfPictogram = iterPicPuzzle.getComponents();
            }
        }

        Iterable<PictogramPart> pics = pictogramPartService.getPics();
        Collection<PictogramPart> pictogramPartCollection = new ArrayList<>();
        for (PictogramPart pictogramPart : pics) {
            pictogramPartCollection.add(pictogramPart);
        }
        int sizeOfSearchPictogram = (Math.round(pictogramPartCollection.size()/5)+1)*2;

        Label searchPart = new Label(getString("playPuzzle-search-part-label"));

        GridLayout searchPictogram = new GridLayout(5,sizeOfSearchPictogram);
        searchPictogram.setSpacing(true);
        searchPictogram.setMargin(false);

        searchGridPart(searchPictogram,"all");

        TextField searchField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        ((MTextField) searchField).withValueChangeListener(event -> {
            if (searchField.getValue().isEmpty())
                searchGridPart(searchPictogram, "all");
            else
                searchGridPart(searchPictogram, searchField.getValue());
        });

        Panel pictogram = new Panel();
        pictogram.setWidth("450px");
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

        Label namePictogram = new Label(selectedPictogram);

        AbsoluteLayout createPictograms = new AbsoluteLayout();

        createPictograms.setHeight("800px");
        createPictograms.setWidth("800px");

        String[][] pictogramsPuzzle = new String[10][4];

        DropTargetExtension<AbsoluteLayout> dropTargets = new DropTargetExtension<>(createPictograms);    //umozni prijat
        dropTargets.setDropEffect(DropEffect.MOVE);
        dropTargets.addDropListener(event -> {
            isCorrect=true;
            Optional<AbstractComponent> dragSource = event.getDragSourceComponent();
            if (dragSource.isPresent() && dragSource.get() instanceof Image) {
                if (searchField.getValue().isEmpty())
                    searchGridPart(searchPictogram, "all");
                else
                    searchGridPart(searchPictogram, searchField.getValue());

                int x = event.getMouseEventDetails().getRelativeX();
                int y = event.getMouseEventDetails().getRelativeY();

                Component picture = dragSource.get();
                picture.setWidth((80*widthOfPiktogram)+"px");
                picture.setHeight((80*heightOfPiktogram)+"px");

                int xp =Math.round(x/80);
                int yp =Math.round(y/80);

                if (createPictograms.getWidth()>=xp*80+picture.getWidth() && createPictograms.getHeight()>= yp*80+picture.getHeight()){
                    int zpx=createPictograms.getComponentCount();
                    if (zpx < 10){
                        createPictograms.addComponent(picture, "left: "+80*xp+"px; top: "+80*yp+"px;");
                        int zp=createPictograms.getComponentCount();
                        if (zpx!=zp){
                            pictogramsPuzzle[zp-1][0]=(nameOfPiktogram);
                            pictogramsPuzzle[zp-1][1]=(Integer.toString(xp));
                            pictogramsPuzzle[zp-1][2]=(Integer.toString(yp));
                            pictogramsPuzzle[zp-1][3]=(Integer.toString(zp-1));
                        }else{
                            for (int i=0; zp-1>=i;i++){
                                if (pictogramsPuzzle[i][0].equals(nameOfPiktogram)){
                                    pictogramsPuzzle[i][1]=(Integer.toString(xp));
                                    pictogramsPuzzle[i][2]=(Integer.toString(yp));
                                }
                            }
                        }

                        int j=0;
                        Iterator<PictogramPuzzle> iterPictogramPuzzle = picsPuzzle.iterator();
                        while (iterPictogramPuzzle.hasNext()) {
                            PictogramPuzzle iterPicPuzzle = iterPictogramPuzzle.next();
                                  if (iterPicPuzzle.getPictPuzzle().equals(selectedPictogram)){
                                componentOfPictogram = iterPicPuzzle.getComponents();
                            }
                        }

                        while (componentOfPictogram[j][0]!=null){
                            for (int k=0;k<4;k++){
                                if (!componentOfPictogram[j][k].equals(pictogramsPuzzle[j][k])){
                                    isCorrect=false;
                                }
                            }
                            j++;
                        }
                    }else{
                        Notification.show(getString("createPuzzle-max-layout-pictogram-reached"));
                    }
                }else{
                    Notification.show(getString("createPuzzle-drop-picture-out-of-bound"));
                }
            }
            if (isCorrect && createPictograms.getComponentCount()!=0){
                Notification.show("Well done");//----------------------------------------------------------------------------------
            }
        });

        Panel createPictogramx = new Panel();
        createPictogramx.setContent(createPictograms);

        MHorizontalLayout path = new MHorizontalLayout()
                .add(namePictogram);

        MVerticalLayout createPictogramLayout = new MVerticalLayout()
                .add(path)
                .add(createPictogramx);

        createPictogramLayout.setComponentAlignment(path, Alignment.TOP_CENTER);
        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                .add(new MHorizontalLayout()
                        .add(new MVerticalLayout()
                                .add(searchLayout)
                        )
                        .add(createPictogramLayout)
                );
        content.setMargin(true);

        content.setWidth("100%");
        Panel panel = new Panel();
        panel.setSizeFull();
        panel.setContent(content);
        contentLayout.removeAllComponents();
        contentLayout.add(panel, Alignment.TOP_LEFT);
    }

    private void searchGridPart(GridLayout searchPictogram, String search){
        Iterable<PictogramPuzzle> picsPuzzle = pictogramPuzzleService.getPics();
        Iterator<PictogramPuzzle> iterPictogramPuzzle = picsPuzzle.iterator();
        while (iterPictogramPuzzle.hasNext()) {
            PictogramPuzzle iterPicPuzzle = iterPictogramPuzzle.next();
            if (iterPicPuzzle.getPictPuzzle().equals(selectedPictogram)){
                componentOfPictogram = iterPicPuzzle.getComponents();
            }
        }

        Iterable<PictogramPart> pics = pictogramPartService.getPics();
        Iterator<PictogramPart> iteratorPictogramPart = pics.iterator();
        int j=0;
        int i=0;
        searchPictogram.removeAllComponents();

        while (iteratorPictogramPart.hasNext()) {
            while (iteratorPictogramPart.hasNext() && 5>i) {
                if (iteratorPictogramPart.hasNext()) {
                    PictogramPart iterPic = iteratorPictogramPart.next();
                    if (iterPic.getPictPart().contains(search) || search.equals("all")) {
                        for (int k=0; componentOfPictogram[k][0]!=null;k++){
                            if (componentOfPictogram[k][0].equals(iterPic.getPictPart())){
                                Image picture = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                new ByteArrayInputStream(iterPic.getBytes()), ""));
                                picture.setWidth("80px");
                                picture.setHeight("80px");

                                DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
                                dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

                                dragSourcex.addDragStartListener(e -> {
                                        dragSourcex.setDragData(picture);
                                        widthOfPiktogram=iterPic.getWidth();
                                        heightOfPiktogram=iterPic.getHeight();
                                        nameOfPiktogram=iterPic.getPictPart();
                                    }
                                );
                                searchPictogram.addComponent(picture, i, j);
                                searchPictogram.addComponent(new Label(iterPic.getPictPart()), i, (j + 1));
                               i++;
                            }
                        }
                    }
                }
            }
            i=0;
            j=j+2;
        }
    }

    public static void setSelectedPictogram(String selPictogram){
        selectedPictogram=selPictogram;
    }

}
