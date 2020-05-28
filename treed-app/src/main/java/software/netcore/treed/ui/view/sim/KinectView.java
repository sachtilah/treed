package software.netcore.treed.ui.view.sim;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Date;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import lombok.SneakyThrows;
import software.netcore.treed.business.sim.ClauseService;

@SpringView(name = software.netcore.treed.ui.view.sim.KinectView.VIEW_NAME)
public class KinectView extends AbstractSimView implements View {

   static class Kinect extends J4KSDK {

      int counter = 0;
      long time = 0;

      @SneakyThrows
      @Override
      public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations, byte[] joint_status) {

         for (int i = 0; i < this.getMaxNumberOfSkeletons(); i++) {
            Skeleton.getSkeleton(i, skeleton_tracked, positions, orientations, joint_status, this);
         }

         //System.out.println("A new skeleton frame was received: " + skeleton_tracked + ", " + positions + ", " + orientations + ", " + joint_status);
      }

      @Override
      public void onColorFrameEvent(byte[] color_frame) {
         int counter = 0;
         //System.out.print("A new color frame was received: ");
      }

      @Override
      public void onDepthFrameEvent(short[] depth_frame, byte[] body_index, float[] xyz, float[] uv) {
         //System.out.println("A new depth frame was received: " + depth_frame + ", " + body_index + ", " + xyz + ", " + uv);

         if (counter == 0) {
            time = new Date().getTime();
         }
         counter += 1;
      }

      public static void doIt() {

         if (!System.getProperty("os.arch").toLowerCase().contains("64")) {
            System.out.println("WARNING: You are running a 32bit version of Java.");
            System.out.println("This may reduce significantly the performance of this application.");
            System.out.println("It is strongly adviced to exit this program and install a 64bit version of Java.\n");
         }

         System.out.println("This program will run for 10 seconds.");
         Kinect kinect = new Kinect();
         kinect.start(J4KSDK.COLOR | J4KSDK.DEPTH | J4KSDK.SKELETON);

         //Sleep for 10 seconds.
         try {
            Thread.sleep(5000);
         } catch (InterruptedException e) {
         }

         kinect.stop();
         System.out.println("FPS: " + kinect.counter * 1000.0 / (new Date().getTime() - kinect.time));
      }
   }

   public static final String VIEW_NAME = "/kinect";
   private final ClauseService clauseService;

   public KinectView(ClauseService clauseService) {
      this.clauseService = clauseService;
   }

   protected void build(MVerticalLayout contentLayout, ViewChangeListener.ViewChangeEvent viewChangeEvent) {
      contentLayout.removeAllComponents();
      contentLayout.setMargin(true);
      contentLayout.setSpacing(true);

      VerticalLayout verticalLayout = new VerticalLayout();
      verticalLayout.removeAllComponents();
      verticalLayout.setMargin(true);
      verticalLayout.setSpacing(true);

      contentLayout.add(new MVerticalLayout()
                  .withSizeUndefined()
                  .add(new MLabel("No clause created yet."))
            , Alignment.TOP_CENTER);

      Kinect.doIt();

   }
}