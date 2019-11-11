package software.netcore.treed.ui.view.sim;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.data.HasValue;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.sim.ClauseService;
import software.netcore.treed.business.sim.PiktogramService;
import software.netcore.treed.data.schema.sim.Clause;
import software.netcore.treed.data.schema.sim.Piktogram;
import com.vaadin.navigator.View;
import com.vaadin.ui.themes.ValoTheme;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@StyleSheet("VAADIN/themes/treed/styles.scss")
@SpringView(name = software.netcore.treed.ui.view.sim.GameView.VIEW_NAME)
public class GameView extends AbstractSimView implements View {

    public static final String VIEW_NAME = "/game";
    private String parameter;
    private final ClauseService clauseService;
    private final PiktogramService piktogramService;

    public GameView(ClauseService clauseService, final PiktogramService piktogramService) {
        this.clauseService = clauseService;
       this.piktogramService = piktogramService;
    }

    @Override
    protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent event) {
        if (event.getParameters() != null) {
            String[] params = event.getParameters().split("/");
            for (String param : params) {
                parameter = param;
            }
        }
        build(parameter, contentLayout);
    }

    public void build(String parameter, MVerticalLayout contentLayout) {
         VerticalLayout content = new MVerticalLayout();
         contentLayout.removeAllComponents();
         contentLayout.addComponent(content);

        Label clauseNameLabel = new Label("<h1>" + parameter + "</h1>", ContentMode.HTML);
        content.addComponent(clauseNameLabel);
        content.setComponentAlignment(clauseNameLabel, Alignment.MIDDLE_CENTER);

        Button backButton = new MButton(getString("gameView-back-button"));
        backButton.addClickListener((Button.ClickListener) event ->
                getUI().getNavigator().navigateTo(SimHomeScreenView.VIEW_NAME));
        content.addComponent(backButton);
        content.setComponentAlignment(backButton, Alignment.MIDDLE_CENTER);

        final int[] c = {0};

        Iterable<Clause> clauses = clauseService.getClauses();
        for (Clause clause : clauses) {
            if (clause.getName().equals(parameter)) {
                int columns = clause.getColumnCount();
                int rows = clause.getRowCount();
                GridLayout grid = new GridLayout(columns, rows * 3);
                grid.removeAllComponents();

               Iterable<Piktogram> piktograms = clause.getPiktograms();
               final Collection<Piktogram> piktogramCollection = new ArrayList<>();
               for (Piktogram piktogram : piktograms)
                  piktogramCollection.add(piktogram);
               Iterator<Piktogram> iteratorPic = piktograms.iterator();

               int i = 0, j = 0;

               while(iteratorPic.hasNext() && j < rows * 3){
                  Piktogram iterPic = iteratorPic.next();
                  grid.addComponent(new Image("", new StreamResource((StreamResource.StreamSource) () ->
                        new ByteArrayInputStream(iterPic.getBytesImage()), "")), i, j);
                  j++;
                  Audio audio = new Audio("", new StreamResource((StreamResource.StreamSource) () ->
                        new ByteArrayInputStream(iterPic.getBytesAudio()), ""));
                  audio.play();
                  System.out.println(iterPic.getBytesAudio().toString());
                  grid.addComponent(audio, i, j);
                  j++;

                  int finalJ = j;
                  int finalI = i;

                  Iterable<Piktogram> pictograms = piktogramService.getPics();
                  final List<String> piktogramList = new ArrayList<>();
                  for (Piktogram piktogram : pictograms)
                     piktogramList.add(piktogram.getTerm());

                  ComboBox<String> field = new ComboBox<>("");
                  field.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                     if (iterPic.getTerm().equals(valueChangeEvent.getValue())) {
                        grid.removeComponent(valueChangeEvent.getComponent());
                        grid.addComponent(new Label(iterPic.getTerm()));
                        c[0]++;
                        grid.getComponent(finalI, finalJ).setStyleName(ValoTheme.LABEL_SUCCESS);
                        grid.setComponentAlignment(grid.getComponent(finalI, finalJ), Alignment.MIDDLE_CENTER);
                        if (c[0] == clause.getPiktograms().size())
                           Notification.show(getString("gameView-notification-win"));
                     } else valueChangeEvent.getComponent().addStyleName("wrongTerm");
                  });
                  field.setItems(piktogramList);
                  field.addStyleName("comboBox");
                  field.setEmptySelectionAllowed(false);
                  field.setItemCaptionGenerator(String::toString);
                  field.setNewItemProvider(inputString -> {
                     String newPic;
                     newPic = inputString;
                     field.setItems(piktogramList);
                     return Optional.of(newPic);
                  }) ;
                  grid.addComponent(field, i, j);

                  if(i+1 < columns) {
                     j -= 2;
                     i++;
                     //coment
                  }
                  else if(i < columns) {
                     i = 0;
                     j++;
                  }
               }
               content.addComponent(grid);
               content.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
               log.info("Clause " + parameter + " loaded");
            }
        }
    }
}