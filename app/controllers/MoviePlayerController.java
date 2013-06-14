package controllers;

import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.MovieNotFoundException;
import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import exception.CancelActionException;
import exception.ClipDoesntExistException;
import model.MovieManager;
import model.exceptions.NoAdsException;
import model.exceptions.UserNotAllowedException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import services.MovieManagerImpl;
import javax.ejb.EJB;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This controller manages the view of a Movie.
 *
 */
public class MoviePlayerController {
        // Configuration file name
//        private static final String PROPERTIES = "conf.dao_factory";
//        private static  ResourceBundle rb = ResourceBundle.getBundle(PROPERTIES);

        // Key for the name of the class that implement MovieManager
//        private static final String MOVIE_MANAGER_IMPL_K = "MOVIE_MANAGER_IMPL";

        protected static Logger logger = Logger.getLogger("MoviePlayerController.class");

        @EJB(beanName = "MovieManager")
        private MovieManager movieDao;

        //Token of the movie of the user that is watching the movie.
        private String token;
        private Long movieId;
        private List<Clip> movie;
        private int currentClipIndex=-1; // Starts with -1 because there isn't a movie
        private boolean adTime=false;

    /*
     * Starts playing the movie.
     * @throws CancelActionException when the token is no longer valid.
     *
     */
    protected void startMovie(Long movieID, String userToken) throws CancelActionException{
        //CAMBIAR A EJB
       // movieDao = new MovieManagerImpl();
        this.token=userToken;
        this.movieId=movieID;
        adTime=false;

        //PRUEBA
        if(movieId==1L){
            movie = new ArrayList<Clip>();
            Clip clip = new Clip();
            clip.setId(2L);
            movie.add(clip);
        }
        else{  //No olvidar sacar la llave que cierra esto
        //PRUEBA

        // Sets the Clip list of the movie this MoviePlayerController manages.
        try {
            movie = movieDao.getMovie(token, movieId); // Sets the movie the user is watching.
        } catch (MovieNotFoundException e){
            throw new CancelActionException("The movie couldn't be found, please choose another movie.");
        } catch (UserNotAllowedException e){
            throw new CancelActionException("You are not allowed to see this movie.");
        } catch (InvalidTokenException e){
            throw new CancelActionException("Your session is no longer valid, please login again.");
        }

        }
    }

    public byte[] nextClip() throws CancelActionException, ClipDoesntExistException {
        if(adTime==false){
            if((currentClipIndex+1)==movie.size()){
                throw new ClipDoesntExistException("This movie has no more clips. Thanks for watching.");
            }
            currentClipIndex++;
            byte[] currentClip=getCurrentClip();
            adTime = true;  // The following time nextClip is called it has to return an ad.
            return currentClip;
        } else {
            try {
                byte[] ad = getAd();
                adTime = false;
                return ad;
            } catch (NoAdsException e){
                adTime = false;
                logger.warn("NoAdsException catched while watching movie "+ movieId);
                return nextClip();
            }
        }
    }

    public byte[] prevClip() throws CancelActionException, ClipDoesntExistException{
        if(adTime==false){
            if(currentClipIndex==0){
                throw new ClipDoesntExistException("This is the first clip of the movie.");
            }
            currentClipIndex--;
            byte[] currentClip=getCurrentClip();
            adTime = true;  // The following time nextClip is called it has to return an ad.
            return currentClip;
        } else {
            try {
                byte[] ad = getAd();
                adTime = false;
                return ad;
            } catch (NoAdsException e){
                adTime = false;
                logger.warn("NoAdsException catched while watching movie "+ movieId);
                return prevClip();
            }
        }
    }

    public byte[] getCurrentClip() throws CancelActionException {
        ClipData clipData;


        //PRUEBA
        if(movie.get(currentClipIndex).getId()==2L){
            try{
            FileInputStream fs = new FileInputStream("C:/Users/telematica/Desktop/video.mp4");
            byte[] bytes = IOUtils.toByteArray(fs);
            return bytes;
            } catch (FileNotFoundException eh) {
                throw new RuntimeException(eh.getMessage());
            } catch (IOException exc) {
                throw new RuntimeException(exc.getMessage());
            }
        }
        //PRUEBA

        try{
            clipData = movieDao.getClipData(token,movie.get(currentClipIndex).getId());
        } catch (InvalidTokenException e){
            throw new CancelActionException("Your session is no longer valid, please login again.");
        } //catch (FileNotFoundException e) {
           // e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        //}
        return getBytes(clipData.getBytes());
    }

    private byte[] getAd() throws CancelActionException, NoAdsException {
        ClipData clipData;
        try{
            clipData = movieDao.getAd(token, movieId);
        } catch (InvalidTokenException e){
            throw new CancelActionException("Your session is no longer valid, please login again.");
        }
        return getBytes(clipData.getBytes());
    }

    private byte[] getBytes(Byte[] bytes){
        byte[] normalBytes = new byte[bytes.length];
        for (int i=0;i<bytes.length;i++){
              normalBytes[i]=bytes[i].byteValue();
        }
        return normalBytes;
    }

}
