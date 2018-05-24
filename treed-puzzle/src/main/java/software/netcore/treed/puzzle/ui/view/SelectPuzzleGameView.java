package software.netcore.treed.puzzle.ui.view;

//import com.vaadin.event.MouseEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
//import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
//import com.vaadin.ui.dnd.DragSourceExtension;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.AbstractRestrictedEnterView;
import software.netcore.treed.api.TreedCustomComponent;
//import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.data.schema.puzzle.PictogramPuzzle;
//import software.netcore.treed.puzzle.business.PictogramPartService;
import software.netcore.treed.puzzle.business.PictogramPartService;
import software.netcore.treed.puzzle.business.PictogramPuzzleService;
import software.netcore.treed.ui.view.AbstractMenuView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@SpringView(name =SelectPuzzleGameView.VIEW_NAME)
public class SelectPuzzleGameView extends AbstractMenuView implements View {

    public static final String VIEW_NAME = "/puzzle/select";
   private String captionx;
    //private String nameOfPiktogram="";
    private String[][] componentOfPictogram = new String[10][4];
    private int sizeOfPictograms;
    Collection<String> piktogramNames = new ArrayList<>();
    //String[] piktogramNames =new String[sizeOfPictograms];
    //-------------------------------------------------------------------
    private final PictogramPartService pictogramPartService;
    private final PictogramPuzzleService pictogramPuzzleService;

    public SelectPuzzleGameView(PictogramPuzzleService pictogramPuzzleService, PictogramPartService pictogramPartService) {
        this.pictogramPuzzleService = pictogramPuzzleService;
        this.pictogramPartService = pictogramPartService;
    }

    /**
     * Build page.
     */
    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent event) {
        //captionx="skuska";
        //log.info("run build");
        Iterable<PictogramPuzzle> pics = pictogramPuzzleService.getPics();
        Collection<PictogramPuzzle> pictogramPuzzleCollection = new ArrayList<>();
        for (PictogramPuzzle pictogramPuzzle : pics) {
            pictogramPuzzleCollection.add(pictogramPuzzle);
        }
        sizeOfPictograms = pictogramPuzzleCollection.size();
        int sizeOfSearchPictogram = (Math.round(pictogramPuzzleCollection.size()/5)+1)*2;

        Label selectPuzzle = new Label(getString("selectPuzzleGame-select-puzzle-label"));
        Button selectButton = new MButton(getString("selectPuzzleGame-select-button-button"));

        TextField searchField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        //GridLayout searchPictogram = new GridLayout(5,10);
        //GridLayout searchPictogram = new GridLayout(5,sizeOfSearchPictogram);
        AbsoluteLayout searchPictogram = new AbsoluteLayout();
        //searchPictogram.setSpacing(false);
        //searchPictogram.setMargin(false);
        searchPictogram.setWidth(100,Unit.PERCENTAGE);
        searchPictogram.setHeight((((Math.round(pictogramPuzzleCollection.size()/5)+1)*200)+25),Unit.PIXELS);
        //searchPictogram.setSizeFull();//-----------------------------------------------------------------------------------------


        searchGrid(searchPictogram,"all");
        //searchGridx(searchPictogram,"all");

        //searchPictogram.addComponent(selectPuzzle,0,0);
        //searchPictogram.addComponent(searchField,0,1);

        searchPictogram.addLayoutClickListener(ClickEvent ->{
                    int x = ClickEvent.getMouseEventDetails().getRelativeX();
                    int y = ClickEvent.getMouseEventDetails().getRelativeY();
                    //log.info("drop at x "+ (x) + " y " + y);
                    int xp =Math.round((x-10)/170);
                    int yp =Math.round((y-25)/200);//nechapem preco mi davamedyeru hore
                    //captionx=piktogt;
                    int selected =yp*5+xp;
                    //searchPictogram.getComponentCount()/2;
                    log.info("selected " + selected);
                    //log.info(Integer.toString(searchPictogram.getComponentCount()));
                    if (searchPictogram.getComponentCount()/2>=(selected+1)){
                    if (piktogramNames.toArray()[selected]!=null) {
                        captionx = String.valueOf(piktogramNames.toArray()[selected]);
                        PlayPuzzleView.setSelectedPictogram(captionx);
                        //log.info("asimeno " + captionx);
                    }
                    }



                    //int a= Math.round(searchPictogram.getComponent(0,0).getHeight());
                    //int b = Math.round(searchPictogram.getComponent(0,1).getHeight());
                    /*if (searchPictogram.getComponent(0,0)!=null && searchPictogram.getComponent(0,1)!=null) {
                        int xp =Math.round(x/searchPictogram.getComponent(0,0).getWidth());
                        int yp =Math.round(y/(searchPictogram.getComponent(0,0).getHeight()+searchPictogram.getComponent(0,1).getHeight()));

                        //int xp = Math.round(x / 100);
                        //int yp = Math.round(y / (100));
                        //xp=0;
                        //yp=0;
                        //log.info(Integer.toString(xp),Integer.toString(yp));


                        if (searchPictogram.getComponent(xp,yp*2)!= null){
                            captionx = searchPictogram.getComponent(xp, (yp*2)).getCaption();
                            //searchPictogram.get
                            if (ClickEvent.getChildComponent().getCaption()!=null){
                            String nam = ClickEvent.getChildComponent().getCaption();
                               ClickEvent.getChildComponent();
                            log.info("name of child component " + nam);
                            }

                            log.info("drop at x " + (xp) + " y " + yp);
                        }
                    }*/
        }
        );

        selectButton.addClickListener(clickEvent ->{
            if(captionx!=null)
                getUI().getNavigator().navigateTo(PlayPuzzleView.VIEW_NAME);
            else
                Notification.show(getString("selectPuzzleGame-no-pictogram-select-button"));
        });



        ((MTextField) searchField).withValueChangeListener(valueChangeEvent -> {
            /*String sk = "35px";
            Notification.show(sk);*/

            if (searchField.getValue().isEmpty())
                searchGrid(searchPictogram, "all");
            else
                searchGrid(searchPictogram, searchField.getValue());


        });
/*
// A single-select radio button group
        RadioButtonGroup<String> single =
                new RadioButtonGroup<>("Single Selection");
        //single.setItems("Single", "Sola", "Yksi");
        single.setItems(selectPuzzle);*/





        Panel pictogram = new Panel();
        pictogram.setWidth("870px");
        pictogram.setHeight("500px");
        //log.info("panel w "+ (pictogram.getWidth()) + " h " + (pictogram.getHeight()));

        pictogram.setContent(searchPictogram);
        //pictogram.setContent(new Label("puzzle-pictograms"));

        MHorizontalLayout search= new MHorizontalLayout()
                .add(selectPuzzle)
                .add(searchField);
        //search
        MVerticalLayout searchLayout = new MVerticalLayout()
                .withMargin(true)
                .add(search)
                .add(pictogram)
                .add(selectButton);
        //search.setComponentAlignment(pictogram, Alignment.TOP_CENTER);
        searchLayout.setComponentAlignment(search, Alignment.TOP_CENTER);
        searchLayout.setComponentAlignment(pictogram, Alignment.TOP_CENTER);
        searchLayout.setComponentAlignment(selectButton, Alignment.TOP_CENTER);



        Panel panel = new Panel();
        //panel.setHeight("600px");
        panel.setSizeFull();
        panel.setContent(searchLayout);
        contentLayout.removeAllComponents();
        contentLayout.add(panel, Alignment.TOP_CENTER);
    }

    private void searchGrid(AbsoluteLayout searchPictogram, String search){
        //pics = pictogramPartService.getPics();
        Iterable<PictogramPuzzle> pics = pictogramPuzzleService.getPics();

        Collection<PictogramPuzzle> pictogramPuzzleCollection = new ArrayList<>();
        for (PictogramPuzzle pictogramPuzzle : pics) {
            pictogramPuzzleCollection.add(pictogramPuzzle);
        }
        sizeOfPictograms = pictogramPuzzleCollection.size();

        Iterator<PictogramPuzzle> iteratorPictogramPuzzle = pics.iterator();
        int j=0;
        int i=0;

        /*for (int k = 0; k < piktogramNames.size(); k++)
            piktogramNames.clear();[k] = null;*/

        piktogramNames.clear();
        searchPictogram.removeAllComponents();

        while (iteratorPictogramPuzzle.hasNext()) {
            //for (int i = 0; i < 5; i++) {
            while (iteratorPictogramPuzzle.hasNext() && 5>i) {

                //if (iteratorPictogramPuzzle.hasNext()) {
                    PictogramPuzzle iterPic = iteratorPictogramPuzzle.next();

                    //if (iterPic.getPictPuzzle().equals(captionx)){//na hru
                        componentOfPictogram = iterPic.getComponents();
                    //}


                    if (iterPic.getPictPuzzle().contains(search) || search.equals("all")) {


                        Iterable<PictogramPart> picsPart = pictogramPartService.getPics();

                        GridLayout picture = new GridLayout(10,10);
                        picture.setMargin(false);
                        picture.setSpacing(false);
                        picture.setWidth("160px");
                        picture.setHeight("160px");

                        int l=0;
                        while (componentOfPictogram[l][0]!=null){
                                Iterator<PictogramPart> iteratorPictogramPart = picsPart.iterator();
                                while (iteratorPictogramPart.hasNext()) {
                                    PictogramPart iterPart = iteratorPictogramPart.next();


                                if (componentOfPictogram[l][0].equals(iterPart.getPictPart())) {
                                    /*log.info(iterPart.getPictPart()+componentOfPictogram[l][0]);
                                    log.info(componentOfPictogram[l][1]);
                                    log.info(componentOfPictogram[l][2]);
                                    log.info(componentOfPictogram[l][3]);*/

                                    //isCorrect=false;
                                    Image pikt = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                            new ByteArrayInputStream(iterPart.getBytes()), ""));

                                    pikt.setWidth(16*iterPart.getWidth()+"px");
                                    pikt.setHeight(16*iterPart.getHeight()+"px");
                                    //log.info("added Image w,h" + iterPart.getWidth()+iterPart.getHeight());
                                    picture.addComponent(pikt,Integer.valueOf(componentOfPictogram[l][1]),Integer.valueOf(componentOfPictogram[l][2]));
                                    //log.info("added part " + iterPart.getPictPart());
                                }
                                }
                            l++;
                        }

                       //picture.setWidth("160px");
                       //picture.setHeight("160px");

                        //searchPictogram.addComponent(picture, i, j);

                        searchPictogram.addComponent(picture, "left: "+((170*i)+10)+"px; top: "+(200*j)+"px;");

                        //searchPictogram.getComponent(i, j);

                        //log.info("added Image " + iterPic.getPictPuzzle());

                        piktogramNames.add(iterPic.getPictPuzzle());

                        //searchPictogram.addComponent(new Label(iterPic.getPictPuzzle()), i, (j + 1));
                        Label name = new Label(iterPic.getPictPuzzle());
                        name.setWidth("150px");
                        name.setHeight("20px");
                        searchPictogram.addComponent(name, "left: "+((170*i)+15)+"px; top: "+((200*j)+170)+"px;");

                        //log.info("added Text " + iterPic.getPictPuzzle());
                        i++;
                    }

            }
            i=0;
            j=j+1;
        }
    }


}
