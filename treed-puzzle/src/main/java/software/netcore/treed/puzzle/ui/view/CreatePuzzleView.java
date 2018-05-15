package software.netcore.treed.puzzle.ui.view;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.api.TreedCustomComponent;
import software.netcore.treed.puzzle.ui.ImageUploader;


@RequiredArgsConstructor
@SpringView(name =CreatePuzzleView.VIEW_NAME)
public class CreatePuzzleView extends TreedCustomComponent implements View {

    public static final String VIEW_NAME = "/puzzle/create";

    private MVerticalLayout mainLayout;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
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
        /**
         * Objects
         */
        Button selectButton = new MButton(getString("select-upload-puzzle-part"))
                .withListener(clickEvent -> {
            uploadPath();
        });

        TextField pathField = new MTextField(getString("path-puzzle-part-pictogram"))
                .withFullSize()
                .withHeight("35px");

        TextField namePartField = new MTextField(getString("name-puzzle-part-pictogram"))
                .withFullSize()
                .withHeight("35px");

        Button uploadButton = new MButton(getString("upload-puzzle-part"))
                .withListener(clickEvent -> {
                    upload(pathField.getValue(), namePartField.getValue());
                })
                .withListener(event -> getUI().getNavigator().navigateTo(CreatePuzzleView.VIEW_NAME));

        TextField searchField = new MTextField(getString("search-puzzle-part-pictogram"))
                .withFullSize()
                .withHeight("35px");

                //-----------------------------------testing---------------------------
        Image image = new Image(null,
                new ThemeResource("design/puzzle/CreatePuzzleView.png"));
        image.setSizeUndefined();
                //-----------------------------------testing--------------------------

        Panel pictogram = new Panel(getString("puzzle-pictograms"));
            pictogram.setWidth("500px");
            pictogram.setHeight("300px");
            pictogram.setContent(image);

        TextField nameField = new MTextField(getString("puzzle-pictogram-name"))
                .withFullSize()
                .withHeight("35px");

        //doplnit vytvaranie piktogramov grid createPictogram------------------testing
        GridLayout createPictogram = new GridLayout(10,10);
        createPictogram.addComponent(new Button("R/C 1"));
        for (int i = 0; i < 9; i++) {
            createPictogram.addComponent(new Button("Col " +
                    (createPictogram.getCursorX() + 1)));
        }

// Fill out the first column using coordinates.
        for (int i = 1; i < 10; i++) {
            createPictogram.addComponent(new Button("Row " + i), 0, i);
        }
        // Add some components of various shapes.
        /*createPictogram.addComponent(new Button("3x1 button"), 1, 1, 3, 1);
        createPictogram.addComponent(new Label("1x2 cell"), 1, 2, 1, 3);
        InlineDateField date =
                new InlineDateField("A 2x2 date field");
        date.setResolution(DateField.RESOLUTION_DAY);
        createPictogram.addComponent(date, 2, 2, 3, 3);*/
        //------------------------------------------------------------------------testing

        Button createButton = new MButton(getString("create-puzzle-pikt"))
                .withListener(clickEvent -> {
                    createPikt();
                })
                .withClickShortcut(ShortcutAction.KeyCode.ENTER);
            //getUI().getNavigator().navigateTo(CreatePuzzleView.VIEW_NAME);

        /**
         * Layouts
         */
        MVerticalLayout pathLayout = new MVerticalLayout()
                .add(new MHorizontalLayout()
                .withSpacing(true)
                .add(selectButton)
                .add(pathField)
        )
                .add(new MHorizontalLayout()
                        .withMargin(false)
                        .add(namePartField, uploadButton)
                );

        MVerticalLayout searchLayout = new MVerticalLayout()
                .add(new MHorizontalLayout()
                                .withMargin(false)
                        .add(searchField)
                        );
                searchLayout.addComponent(pictogram);

        MVerticalLayout createLayout = new MVerticalLayout();
                createLayout.addComponent(nameField);
                createLayout.addComponent(createPictogram);
                createLayout.addComponent(createButton);

        MVerticalLayout content = new MVerticalLayout()
                .withUndefinedSize()
                //.withSize("100%","100%")
                .add(new MHorizontalLayout()
                    .add(new MVerticalLayout()
                        .add(pathLayout)
                        .add(searchLayout)
                    )
                    .add(createLayout)
                );
            content.setWidth("100%");

        //ImageUploader imag = new ImageUploader();
            //imag.receiveUpload("picture", image);
                //pictogram.setComponentAlignment(topcenter, Alignment.TOP_CENTER);


        mainLayout.removeAllComponents();
        mainLayout.add(content, Alignment.TOP_LEFT);
    }


    /**
     * Upload puzzle part.
     *
     * @param path path to file
     * @param name name of uploaded file
     */
    private void upload(String path, String name){

    }

    /**
     * Choose path.
     *
     * //@param username username
     * //@param password password
     */
    private void uploadPath(){

    }

    /**
     * Create pictogram.
     *
     * //@param username username
     * //@param password password
     */
    private void createPikt(){

    }
}

