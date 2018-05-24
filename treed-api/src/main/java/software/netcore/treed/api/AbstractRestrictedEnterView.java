package software.netcore.treed.api;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Base VIEW class for every Treed VIEW which requires user to be logged in when entering it.
 *
 * @since v.1.0.0
 */
public abstract class AbstractRestrictedEnterView extends TreedCustomComponent implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSizeFull();

        MVerticalLayout contentLayout = new MVerticalLayout()
                .withMargin(false)
                .withSpacing(false)
                .withFullSize();

        setCompositionRoot(new MVerticalLayout()
                .withMargin(false)
                .withSpacing(false)
                .withFullSize()
                .add(getHeader())
                .add(contentLayout, 1f));

        build(contentLayout, event);
    }

    protected abstract void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent event);

    protected abstract Component getHeader();

}
