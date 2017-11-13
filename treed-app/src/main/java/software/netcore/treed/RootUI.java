package software.netcore.treed;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import lombok.RequiredArgsConstructor;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

/**
 * @since v. 1.4.0
 */
@SpringUI
@Title("Treed")
@Push(transport = Transport.WEBSOCKET)
@PreserveOnRefresh
@Viewport("user-scalable=no,initial-scale=1.0")
@RequiredArgsConstructor
public class RootUI extends UI {

    private final AccountService accountService;

    @Override
    protected void init(VaadinRequest request) {
        getUI().setResizeLazy(true);


        setContent(new MPanel()
                .withContent(new MVerticalLayout()
                        .add(new Label("Click it"))
                        .add(new MButton("Show users")
                                .withListener(e -> {

                                    MVerticalLayout l = new MVerticalLayout();
                                    accountService.getAccounts()
                                            .forEach(acc -> l.add(new MLabel(acc.getUsername())));

                                    addWindow(new MWindow("Accounts")
                                            .withContent(new MPanel(l))
                                            .withWidth(150, Unit.PIXELS)
                                            .withCenter());

                                }))));
    }

}
