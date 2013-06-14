package ControllersTests;

import controllers.Application;
import org.apache.commons.io.IOUtils;
import play.mvc.Result;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: telematica
 * Date: 13/06/13
 * Time: 09:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationTest extends Application {
    public static Result getPreviousClip(){
        FileInputStream fs = null;
        try {
            response().setContentType("video/mp4");
            fs = new FileInputStream("C:/Users/telematica/Desktop/video.mp4");
            byte[] bytes = IOUtils.toByteArray((InputStream) fs);
            return ok(new FileInputStream("C:/Users/telematica/Desktop/video.mp4"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return ok(homePage.render(""));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return ok(homePage.render(""));
        }
    }
}
