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
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.puzzle.PictogramPartService;
import software.netcore.treed.business.puzzle.PictogramPuzzleService;
import software.netcore.treed.data.schema.puzzle.PictogramPart;
import software.netcore.treed.data.schema.puzzle.PictogramPuzzle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.*;

@Slf4j
@SpringView(name =CreatePuzzleView.VIEW_NAME)
public class CreatePuzzleView extends AbstractPuzzleView implements View{
    private int zpx=0;
    private String nameOfPiktogram="";
    private int widthOfPiktogram=1;
    private int heightOfPiktogram=1;
    private int identificator = 0;
    private boolean removePictogram=false;
    public static final String VIEW_NAME = "/puzzle/create";
    private final PictogramPuzzleService pictogramPuzzleService;
    private final PictogramPartService pictogramPartService;
    private ArrayList<String[]> pictogramComponents=new ArrayList<String[]>();

    public CreatePuzzleView(PictogramPartService pictogramPartService, PictogramPuzzleService pictogramPuzzleService) {
        this.pictogramPartService = pictogramPartService;
        this.pictogramPuzzleService = pictogramPuzzleService;
    }

    /**
     * Build page.
     */
    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        /*
         * Objects
         */
        contentLayout.removeAllComponents();

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

        NativeSelect<String> selectWidthPart =
                new NativeSelect<>(getString("createPuzzle-select-width-part-native-select"));
        selectWidthPart.setEmptySelectionAllowed(false);
        selectWidthPart.setItems("1","2","3","4","5","6","7","8","9","10");

        NativeSelect<String> selectHeightPart =
                new NativeSelect<>(getString("createPuzzle-select-height-part-native-select"));
        selectHeightPart.setEmptySelectionAllowed(false);
        selectHeightPart.setItems("1","2","3","4","5","6","7","8","9","10");

        final Upload uploadx = new Upload();
        uploadx.setReceiver(receiver);

        uploadx.addStartedListener(event -> {
            pathField.setValue(event.getFilename());
        });

        uploadx.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            succeededEvent.getFilename();
          });

        uploadx.setButtonCaption(getString("createPuzzle-caption-upload-button"));

        Button uploadButton = new MButton(getString("createPuzzle-upload-button-button")).withListener(clickEvent -> {
            if (selectWidthPart.getValue()==null)
            {selectWidthPart.setSelectedItem("1");}
            if (selectHeightPart.getValue()==null)
            {selectHeightPart.setSelectedItem("1");}

            Iterable<PictogramPart> pics = pictogramPartService.getPics();
            Iterator<PictogramPart> iteratorPictogramPart = pics.iterator();
            boolean allowed=true;


            if(namePartField.getValue().isEmpty())
                Notification.show(getString("createPuzzle-name-part-field-empty-text-field"));
            /*else if(uploadx == null)
                Notification.show(getString("createPuzzle-name-part-upload-empty-text-field"));*/
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
            uploadLayout.addComponent(selectWidthPart,0,2);
            uploadLayout.addComponent(selectHeightPart,1,2);

        Iterable<PictogramPart> pics = pictogramPartService.getPics();
        Collection<PictogramPart> pictogramPartCollection = new ArrayList<>();
        for (PictogramPart pictogramPart : pics) {
            pictogramPartCollection.add(pictogramPart);
        }

        int sizeOfSearchPictogram = (Math.round(pictogramPartCollection.size()/5)+1)*2;

        Label searchPart = new Label(getString("createPuzzle-search-part-label"));

        GridLayout searchPictogram = new GridLayout(5,sizeOfSearchPictogram);
                searchPictogram.setSpacing(true);
                searchPictogram.setMargin(false);

        searchGrid(searchPictogram,"all");

        TextField searchField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        ((MTextField) searchField).withValueChangeListener(event -> {

            if (searchField.getValue().isEmpty())
            searchGrid(searchPictogram, "all");
            else
                searchGrid(searchPictogram, searchField.getValue());
            });

        Panel pictogram = new Panel();
            pictogram.setWidth("465px");
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

        Label namePictogram = new Label(getString("createPuzzle-name-pictogram-label"));

        TextField nameField = new MTextField()
                .withFullSize()
                .withHeight("35px");

        CheckBox removePictogramPart = new CheckBox(getString("createPuzzle-remove-pictogram-part-checkbox"));

        removePictogramPart.setValue(false);

        removePictogramPart.addValueChangeListener(event ->
                removePictogram=removePictogramPart.getValue());

        AbsoluteLayout createPictograms = new AbsoluteLayout();
        createPictograms.removeAllComponents();

        createPictograms.setHeight("800px");
        createPictograms.setWidth("800px");

        //List<String[]> pictograms = new ArrayList<>();

        //String[] position = new String[4];
        //Collection<String[]> pictogramsPartPosition = new ArrayList<>();

        String[][] pictograms = new String[10][4];

        DropTargetExtension<AbsoluteLayout> dropTargets = new DropTargetExtension<>(createPictograms);
        dropTargets.setDropEffect(DropEffect.MOVE);

        dropTargets.addDropListener(event -> {
            Optional<AbstractComponent> dragSource = event.getDragSourceComponent();

                if (dragSource.isPresent() && dragSource.get() instanceof Image) {
                    if (searchField.getValue().isEmpty())
                        searchGrid(searchPictogram, "all");
                    else
                        searchGrid(searchPictogram, searchField.getValue());

                int x = event.getMouseEventDetails().getRelativeX();
                int y = event.getMouseEventDetails().getRelativeY();

                Component picture = dragSource.get();

                picture.setWidth((80*widthOfPiktogram)+"px");
                picture.setHeight((80*heightOfPiktogram)+"px");
                int xp =Math.round(x/80);
                int yp =Math.round(y/80);

                if (createPictograms.getWidth()>=xp*80+picture.getWidth() && createPictograms.getHeight()>= yp*80+picture.getHeight()){
                    zpx=createPictograms.getComponentCount();
                    if (zpx < 10){
                        createPictograms.addComponent(picture, "left: "+80*xp+"px; top: "+80*yp+"px;");
                        int zp=createPictograms.getComponentCount();
                        String[] pictogramData=new String[4];
                        if (zpx != zp) {
                            pictogramData[0]=nameOfPiktogram;
                            pictogramData[1]=(Integer.toString(xp));
                            pictogramData[2]=(Integer.toString(yp));
                            pictogramData[3]=(Integer.toString(identificator));
                            identificator++;
                            pictogramComponents.add(pictogramData);
                        }else{
                            for (Iterator<String[]> iterator = pictogramComponents.iterator(); iterator.hasNext();) {
                                String[] string = iterator.next();
                                if (string[3].equals(Integer.toString(identificator))) {
                                    String[] temp = new String[4];
                                    temp[0]=nameOfPiktogram;
                                    temp[1]=(Integer.toString(xp));
                                    temp[2]=(Integer.toString(yp));
                                    temp[3]=Integer.toString(identificator);
                                    iterator.remove();
                                    pictogramComponents.add(temp);
                                }
                            }
                        }
                        if (zpx != zp) {
                            pictograms[zp - 1][0] = (nameOfPiktogram);
                            pictograms[zp - 1][1] = (Integer.toString(xp));
                            pictograms[zp - 1][2] = (Integer.toString(yp));
                            pictograms[zp - 1][3] = (Integer.toString(zp - 1));
                        } else if (removePictogram){
                            for (int i = 0; zp - 1 >= i; i++) {
                                if (pictograms[i][0].equals(nameOfPiktogram)) {//overit podmineku----------------------------
                                     pictograms[i][0] = pictograms[i+1][0];
                                     pictograms[i][1] = pictograms[i+1][1];
                                     pictograms[i][2] = pictograms[i+1][2];
                                     pictograms[i][3] = pictograms[i+1][3];
                                     }
                                 }
                                 pictograms[zp-1][0]=null;
                                 pictograms[zp-1][1]=null;
                                 pictograms[zp-1][2]=null;
                                 pictograms[zp-1][3]=null;
                            AbsoluteLayout parent = (AbsoluteLayout) picture.getParent();
                            parent.removeComponent(picture);
                            }else{
                                for (int i = 0; zp - 1 >= i; i++) {
                                    if (pictograms[i][0].equals(nameOfPiktogram)) {
                                        pictograms[i][1] = (Integer.toString(xp));
                                        pictograms[i][2] = (Integer.toString(yp));

                                    }
                                }
                            }
                    }else{
                        Notification.show(getString("createPuzzle-max-layout-pictogram-reached"));
                    }
                }else{
                    Notification.show(getString("createPuzzle-drop-picture-out-of-bound"));
                }
            }
        });
        Panel createPictogramx = new Panel();
        createPictogramx.setContent(createPictograms);

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
                            addNewPictogram(nameField.getValue(), pictograms);
                            //getUI().getNavigator().navigateTo(CreatePuzzleView.VIEW_NAME);---------------------------------------------------
                    }
                    getUI().getNavigator().navigateTo(SelectPuzzleGameView.VIEW_NAME);//---------------------------------------------------
                });
    //create
        MHorizontalLayout path = new MHorizontalLayout()
                .add(namePictogram)
                .add(nameField)
                .add(removePictogramPart);
        path.setComponentAlignment(removePictogramPart, Alignment.TOP_RIGHT);

        MVerticalLayout createPictogramLayout = new MVerticalLayout()
                .add(path)
                .add(createPictogramx)
                .add(createButton);
            createPictogramLayout.setComponentAlignment(path, Alignment.TOP_CENTER);
            createPictogramLayout.setComponentAlignment(createButton, Alignment.TOP_CENTER);

    //main layout
        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                .add(new MHorizontalLayout()
                        .add(new MVerticalLayout()
                                .add(uploadLayout)
                                .add(searchLayout)
                        )
                        .add(createPictogramLayout)
                );
            content.setWidth("100%");

        uploadx.addSucceededListener((Upload.SucceededListener) succeededEvent -> {
            Image image = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                    new ByteArrayInputStream(receiver.stream.toByteArray()), ""));
            image.setWidth("250px");
            image.setHeight("250px");
        });

        Panel panel = new Panel();
        panel.setSizeFull();
        MVerticalLayout sk = new MVerticalLayout();
        sk.setSizeFull();
        sk.setMargin(false);
        sk.setSpacing(false);
        panel.setContent(content);
        sk.addComponent(panel);
        sk.setComponentAlignment(panel, Alignment.TOP_CENTER);
        contentLayout.removeAllComponents();
        contentLayout.add(panel, Alignment.TOP_CENTER);
    }

    private void searchGrid(GridLayout searchPictogram, String search){
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
                        Image picture = new Image("", new StreamResource((StreamResource.StreamSource) () ->
                                new ByteArrayInputStream(iterPic.getBytes()), ""));

                        picture.setWidth("80px");
                        picture.setHeight("80px");

                        DragSourceExtension<Image> dragSourcex = new DragSourceExtension<>(picture);    //umozny drag
                        dragSourcex.setEffectAllowed(EffectAllowed.MOVE);                                            //sposob presunu

                        dragSourcex.addDragStartListener(e -> {
                                dragSourcex.setDragData(picture);
                                nameOfPiktogram=iterPic.getPictPart();
                                widthOfPiktogram=iterPic.getWidth();
                                heightOfPiktogram=iterPic.getHeight();
                                }
                        );
                        searchPictogram.addComponent(picture, i, j);
                        searchPictogram.addComponent(new Label(iterPic.getPictPart()), i, (j + 1));
                        i++;
                    }
                }
            }
            i=0;
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
        PictogramPuzzle pictogramPuzzleAdd = new PictogramPuzzle();
        pictogramPuzzleAdd.setPictPuzzle(pictPuzzleName);
        pictogramPuzzleAdd.setComponents(position);
        pictogramPuzzleAdd.setCreateTime(Date.from(Instant.now()));
        pictogramPuzzleService.savePic(pictogramPuzzleAdd);
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

