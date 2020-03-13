/*
 * -----------------------------------------------------------------------\
 * Lumeer
 *
 * Copyright (C) 2016 - 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package software.netcore.treed.ui.view.sim;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Date;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.gui.DWApp;

import software.netcore.treed.business.sim.ClauseService;

/**
 * @author Martin Večeřa <marvenec@gmail.com>
 */

@SpringView(name = software.netcore.treed.ui.view.sim.KinectView.VIEW_NAME)
public class KinectView extends AbstractSimView implements View {

   static class KinectView2 extends J4KSDK {

      int counter = 0;
      long time = 0;

      @Override
      public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] positions, float[] orientations, byte[] joint_status) {
         System.out.println("A new skeleton frame was received.");
      }

      @Override
      public void onColorFrameEvent(byte[] color_frame) {
         System.out.println("A new color frame was received.");
      }

      @Override
      public void onDepthFrameEvent(short[] depth_frame, byte[] body_index, float[] xyz, float[] uv) {
         System.out.println("A new depth frame was received.");

         if (counter == 0) {
            time = new Date().getTime();
         }
         counter += 1;
      }

      public static void doIt() {

         if (System.getProperty("os.arch").toLowerCase().indexOf("64") < 0) {
            System.out.println("WARNING: You are running a 32bit version of Java.");
            System.out.println("This may reduce significantly the performance of this application.");
            System.out.println("It is strongly adviced to exit this program and install a 64bit version of Java.\n");
         }

         System.out.println("This program will run for 10 seconds.");
         KinectView2 kinect = new KinectView2();
         kinect.start(J4KSDK.COLOR | J4KSDK.DEPTH | J4KSDK.SKELETON);

         //Sleep for 10 seconds.
         try {
            Thread.sleep(10000);
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

      KinectView2.doIt();

   }
}
