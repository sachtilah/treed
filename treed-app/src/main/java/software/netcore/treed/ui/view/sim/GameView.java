package software.netcore.treed.ui.view.sim;

import com.vaadin.data.HasValue;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.extern.slf4j.Slf4j;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

import software.netcore.treed.business.sim.ClauseService;
import software.netcore.treed.business.sim.PiktogramService;
import software.netcore.treed.data.schema.sim.Clause;
import software.netcore.treed.data.schema.sim.Piktogram;
import com.vaadin.navigator.View;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Slf4j
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
      log.info(String.valueOf(com.vaadin.shared.Version.getFullVersion()));
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
            GridLayout mainGrid = new GridLayout(columns, rows);
            mainGrid.removeAllComponents();


            Iterable<Piktogram> piktograms = clause.getPiktograms();
            final Collection<Piktogram> piktogramCollection = new ArrayList<>();
            for (Piktogram piktogram : piktograms)
               piktogramCollection.add(piktogram);
            Iterator<Piktogram> iteratorPic = piktograms.iterator();

            int i = 0, j = 0;

            while(iteratorPic.hasNext() && j < rows){
               GridLayout grid = new GridLayout(1, 5);
               int gridRow = 0;
               grid.removeAllComponents();
               Piktogram iterPic = iteratorPic.next();
               grid.addComponent(new Image("", new StreamResource((StreamResource.StreamSource) () ->
                     new ByteArrayInputStream(iterPic.getBytesImage()), "")), 0, gridRow);
               gridRow++;
               try {
                  if(iterPic.getBytesAudio() != null) {
                     File outputFile = new File("C:/ProgramData/Treed/audio/" + iterPic.getTerm() + ".mp3");
                     FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
                     fileoutputstream.write(iterPic.getBytesAudio());
                     fileoutputstream.close();

                     Audio audio = new Audio();
                     final Resource resource = new ExternalResource("C:/ProgramData/Treed/audio/" + iterPic.getTerm() + ".mp3");
                     audio.setSource(resource);
                     audio.play();
                     grid.addComponent(audio, 0, gridRow);
                     gridRow++;
                  }
               } catch (IOException ex) {
                  ex.printStackTrace();
               }

               try {
                  if(iterPic.getBytesVideo() != null) {
                     File outputFile = new File("C:/ProgramData/Treed/audio/" + iterPic.getTerm() + ".mp4");
                     FileOutputStream fileoutputstream = new FileOutputStream(outputFile);
                     fileoutputstream.write(iterPic.getBytesVideo());
                     fileoutputstream.close();

                     Video video = new Video();
                     final Resource resource = new ExternalResource("C:/ProgramData/Treed/audio/" + iterPic.getTerm() + ".mp4");
                     video.setSource(resource);
                     video.play();
                     grid.addComponent(video, 0, gridRow);
                     gridRow++;
                  }
               } catch (IOException ex) {
                  ex.printStackTrace();
               }

               int finalGridRow = gridRow;

               Iterable<Piktogram> pictograms = piktogramService.getPics();
               final List<String> piktogramList = new ArrayList<>();
               for (Piktogram piktogram : pictograms)
                  piktogramList.add(piktogram.getTerm());

               ComboBox<String> field = new ComboBox<>("");

               field.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                  if (iterPic.getTerm().equals(valueChangeEvent.getValue())) {
                     grid.removeComponent(valueChangeEvent.getComponent());
                     grid.addComponent(new Label(iterPic.getTerm()),0, finalGridRow);
                     c[0]++;
                     grid.getComponent(0, finalGridRow).setStyleName(ValoTheme.LABEL_SUCCESS);
                     grid.setComponentAlignment(grid.getComponent(0, finalGridRow), Alignment.MIDDLE_CENTER);
                     if (c[0] == clause.getPiktograms().size()) {
                        Notification notification = new Notification(getString("gameView-notification-win"));
                        notification.show(getString("gameView-notification-win"));
                        notification.setDelayMsec(5000);
                     }
                  } else {
                     valueChangeEvent.getComponent().setStyleName("wrongTerm");
                  }
               });
               field.setItems(piktogramList);
               field.addStyleName("comboBox");
               field.setEmptySelectionAllowed(false);
               field.setItemCaptionGenerator(String::toString);
/*                  field.setNewItemProvider(inputString -> {
                     String newPic;
                     newPic = inputString;
                     field.setItems(piktogramList);
                     return Optional.of(newPic);
                  }) ;

*/
                  grid.addComponent(field, 0, gridRow);
                  mainGrid.addComponent(grid, i, j);
               if(i<columns-1)
                  i++;
               else if(j<rows-1){
                  i = 0;
                  j++;
               }
            }
            content.addComponent(mainGrid);
            content.setComponentAlignment(mainGrid, Alignment.MIDDLE_CENTER);
            log.info("Clause " + parameter + " loaded");
         }
      }
   }
}