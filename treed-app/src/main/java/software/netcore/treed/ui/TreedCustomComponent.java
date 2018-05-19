package software.netcore.treed.ui;


import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomComponent;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * Base class for every custom component used within application. Provides basic
 * internationalization capability.
 *
 * @since v.1.0.0
 */
public abstract class TreedCustomComponent extends CustomComponent {

    /**
     * Returns i18n string based on key and configured {@link Locale}.
     *
     * @return message
     */
    protected String getString(String i18nKey) {
        VaadinSession session = VaadinSession.getCurrent();
        MessageSource messageSource = session.getAttribute(MessageSource.class);
        Locale locale = session.getLocale();

        return messageSource.getMessage(i18nKey, null, locale);
    }

}