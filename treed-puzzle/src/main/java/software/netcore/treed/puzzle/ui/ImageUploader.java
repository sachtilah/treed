package software.netcore.treed.puzzle.ui;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

//import com.vaadin.book.examples.BookExampleBundle;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;


// Implement both receiver that saves upload in a file and
// listener for successful upload
public class ImageUploader implements Receiver, Upload.SucceededListener {
    public File file;
    final Image image = new Image("Uploaded Image");

    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        // Create and return a file output stream

        // Create upload stream
        FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
            file = new File("/tmp/uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        // Show the uploaded file in the image viewer
        image.setSource(new FileResource(file));
    }


}
