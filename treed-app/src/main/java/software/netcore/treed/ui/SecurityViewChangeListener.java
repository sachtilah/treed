package software.netcore.treed.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import lombok.RequiredArgsConstructor;
import software.netcore.treed.ui.view.LoginAttemptView;

import java.util.Objects;

/**
 * @since v.1.8.0
 */
public class SecurityViewChangeListener implements ViewChangeListener {


    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
//        View oldView = event.getOldView();
//        View newView = event.getNewView();
//
//        if (Objects.isNull(oldView) && newView instanceof LoginAttemptView){
//            // 1st enter into application to the login view
//            return true;
//        }else if(){
//
//        }


        return true;
    }

}
