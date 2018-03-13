package software.netcore.treed.ui.view.resetPassword;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.vaadin.viritin.button.MButton;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;

/**
 * @since v. 1.0.0
 */
@SpringView(name = NewPasswordView.VIEW_NAME)
public class NewPasswordView extends CustomComponent implements View {

    public static final String VIEW_NAME = "/reset/verifyview";
    public String verifyPass;
    public String parameter;

    public NewPasswordView() {

        // A layout structure used for composition
        VerticalLayout panelContent = new VerticalLayout();

        // Compose from multiple components
        Label label = new Label("Zadajte nové heslo:");
        panelContent.addComponent(label);

        PasswordField passwordField = new PasswordField("Heslo: ");
        panelContent.addComponent(passwordField);


        Button verifyButton = new MButton("Zmeniť heslo").withListener(clickEvent -> {
          verifyPass = passwordField.getValue();
          verifyPassword(parameter, verifyPass);
          //newPassword(mail, verifyPass);

            Notification.show("Heslo zmenené. Pokračujte na login view");
        });


        panelContent.addComponent(verifyButton);
        // The composition root MUST be set
        setCompositionRoot(panelContent);

        // Set the size as undefined at all levels
        panelContent.setSizeUndefined();
        // this is not needed for a Composite
        setSizeUndefined();

        panelContent.setComponentAlignment(verifyButton, Alignment.MIDDLE_CENTER);
        panelContent.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

    }

    public void verifyPassword(String resetPass, String verifyPass) {

        try {
            String mail;
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:C:${file.separator}ProgramData${file.separator}Treed${file.separator}hsql${file.separator}db", "treed", "treed");
            Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rec = st.executeQuery("SELECT usermail, otpPass FROM otp");
            while (rec.next()) {
                if (resetPass.equals(rec.getString("otpPass"))) {
                    mail = rec.getString("usermail");
                    System.out.println("UPDATED: " + mail + " " + verifyPass);
                    st.executeUpdate("UPDATE account SET password='" + verifyPass + "' WHERE usermail='" + mail + "'");
                    st.executeUpdate("DELETE FROM otp WHERE usermail='" + mail + "'");
                    break;
                }
                else {
                    System.out.println("Password did not match the database: " + resetPass);
                }
            }
            st.close();
        } catch (SQLException d) {
            System.out.println(d.toString());
        } catch (ClassNotFoundException f) {
            System.out.println(f.toString());
        }

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event){
        if(event.getParameters() != null){
            String[] params = event.getParameters().split("/");
            for(String param : params)
                parameter = param;
        }
    }
}
