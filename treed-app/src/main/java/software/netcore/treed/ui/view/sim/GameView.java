package software.netcore.treed.ui.view.sim;

import com.vaadin.data.HasValue;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import software.netcore.treed.business.sim.ClauseService;
import software.netcore.treed.data.schema.sim.Clause;
import software.netcore.treed.data.schema.sim.Piktogram;
import com.vaadin.navigator.View;
import com.vaadin.ui.themes.ValoTheme;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@SpringView(name = software.netcore.treed.ui.view.sim.GameView.VIEW_NAME)
public class GameView extends AbstractSimView implements View {

    public static final String VIEW_NAME = "/game";
    private String parameter;
    private final ClauseService clauseService;

    public GameView(ClauseService clauseService) {
        this.clauseService = clauseService;
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
               for (Piktogram piktogram : piktograms) {
                  System.out.println(piktogram.getTerm());
                  piktogramCollection.add(piktogram);
               }
               Iterator<Piktogram> iteratorPic = piktograms.iterator();

               int i = 0, j = 0;

               while(iteratorPic.hasNext() && j < rows * 3){
                  Piktogram iterPic = iteratorPic.next();
                  grid.addComponent(new Image("", new StreamResource((StreamResource.StreamSource) () ->
                        new ByteArrayInputStream(iterPic.getBytesImage()), "")), i, j);
                  j++;
                  Audio audio = new Audio("", new StreamResource((StreamResource.StreamSource) () ->
                        new ByteArrayInputStream(iterPic.getBytesAudio()), ""));
                  grid.addComponent(audio, i, j);
                  j++;

                  int finalJ = j;
                  int finalI = i;
                  grid.addComponent(new MTextField("").withValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                     if (iterPic.getTerm().equals(valueChangeEvent.getValue())) {
                        grid.removeComponent(valueChangeEvent.getComponent());
                        grid.addComponent(new Label(iterPic.getTerm()));
                        c[0]++;
                        grid.getComponent(finalI, finalJ).setStyleName(ValoTheme.LABEL_SUCCESS);
                        grid.setComponentAlignment(grid.getComponent(finalI, finalJ), Alignment.MIDDLE_CENTER);
                        if (c[0] == clause.getPiktograms().size())
                           Notification.show(getString("gameView-notification-win"));
                     } else valueChangeEvent.getComponent().addStyleName("wrongTerm");
                  }), i, j);

                  if(i+1 < columns) {
                     j -= 2;
                     i++;
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