package software.netcore.treed.puzzle.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.AbstractRestrictedEnterView;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.data.schema.puzzle.PictogramPuzzle;
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
    private String[][] componentOfPictogram = new String[10][4];
    private int sizeOfPictograms;
    Collection<String> piktogramNames = new ArrayList<>();
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

        AbsoluteLayout searchPictogram = new AbsoluteLayout();
        searchPictogram.setWidth(100,Unit.PERCENTAGE);
        searchPictogram.setHeight((((Math.round(pictogramPuzzleCollection.size()/5)+1)*200)+25),Unit.PIXELS);

        searchGrid(searchPictogram,"all");

        searchPictogram.addLayoutClickListener(ClickEvent ->{
            int x = ClickEvent.getMouseEventDetails().getRelativeX();
            int y = ClickEvent.getMouseEventDetails().getRelativeY();
            int xp =Math.round((x-10)/170);
            int yp =Math.round((y-25)/200);//nechapem preco mi davamedyeru hore
            int selected =yp*5+xp;
                if (searchPictogram.getComponentCount()/2>=(selected+1)){
                    if (piktogramNames.toArray()[selected]!=null) {
                        captionx = String.valueOf(piktogramNames.toArray()[selected]);
                        PlayPuzzleView.setSelectedPictogram(captionx);
                    }
                }
            }
        );

        selectButton.addClickListener(clickEvent ->{
            if(captionx!=null)
                getUI().getNavigator().navigateTo(PlayPuzzleView.VIEW_NAME);
            else
                Notification.show(getString("selectPuzzleGame-no-pictogram-select-button"));
        });

        ((MTextField) searchField).withValueChangeListener(valueChangeEvent -> {
            if (searchField.getValue().isEmpty())
                searchGrid(searchPictogram, "all");
            else
                searchGrid(searchPictogram, searchField.getValue());
        });

        Panel pictogram = new Panel();
        pictogram.setWidth("870px");
        pictogram.setHeight("500px");

        pictogram.setContent(searchPictogram);

        MHorizontalLayout search= new MHorizontalLayout()
                .add(selectPuzzle)
                .add(searchField);
    //search
        MVerticalLayout searchLayout = new MVerticalLayout()
                .withMargin(true)
                .add(search)
                .add(pictogram)
                .add(selectButton);
        searchLayout.setComponentAlignment(search, Alignment.TOP_CENTER);
        searchLayout.setComponentAlignment(pictogram, Alignment.TOP_CENTER);
        searchLayout.setComponentAlignment(selectButton, Alignment.TOP_CENTER);

        Panel panel = new Panel();
        panel.setSizeFull();
        panel.setContent(searchLayout);
        contentLayout.removeAllComponents();
        contentLayout.add(panel, Alignment.TOP_CENTER);
    }

    private void searchGrid(AbsoluteLayout searchPictogram, String search){
        Iterable<PictogramPuzzle> pics = pictogramPuzzleService.getPics();
        Collection<PictogramPuzzle> pictogramPuzzleCollection = new ArrayList<>();
        for (PictogramPuzzle pictogramPuzzle : pics) {
            pictogramPuzzleCollection.add(pictogramPuzzle);
        }
        sizeOfPictograms = pictogramPuzzleCollection.size();
        Iterator<PictogramPuzzle> iteratorPictogramPuzzle = pics.iterator();
        int j=0;
        int i=0;
        piktogramNames.clear();
        searchPictogram.removeAllComponents();

        while (iteratorPictogramPuzzle.hasNext()) {
            while (iteratorPictogramPuzzle.hasNext() && 5>i) {
                PictogramPuzzle iterPic = iteratorPictogramPuzzle.next();
                componentOfPictogram = iterPic.getComponents();
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
                                Image pikt = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                        new ByteArrayInputStream(iterPart.getBytes()), ""));
                                pikt.setWidth(16*iterPart.getWidth()+"px");
                                pikt.setHeight(16*iterPart.getHeight()+"px");
                                picture.addComponent(pikt,Integer.valueOf(componentOfPictogram[l][1]),Integer.valueOf(componentOfPictogram[l][2]));
                            }
                        }
                        l++;
                    }
                    searchPictogram.addComponent(picture, "left: "+((170*i)+10)+"px; top: "+(200*j)+"px;");
                    piktogramNames.add(iterPic.getPictPuzzle());
                    Label name = new Label(iterPic.getPictPuzzle());
                    name.setWidth("150px");
                    name.setHeight("20px");
                    searchPictogram.addComponent(name, "left: "+((170*i)+15)+"px; top: "+((200*j)+170)+"px;");
                    i++;
                }
            }
            i=0;
            j=j+1;
        }
    }

}
