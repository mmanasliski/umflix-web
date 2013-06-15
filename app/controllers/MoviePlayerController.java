package controllers;

import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.MovieNotFoundException;
import edu.umflix.model.Activity;
import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import edu.umflix.model.User;
import edu.umflix.persistence.ActivityDao;
import exception.CancelActionException;
import exception.ClipDoesntExistException;
import model.MovieManager;
import model.exceptions.NoAdsException;
import model.exceptions.UserNotAllowedException;
import org.apache.log4j.Logger;
import javax.ejb.EJB;
import java.io.*;
import java.util.Date;
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
        public MovieManager movieDao;

        @EJB(beanName = "ActivityDao")
        private ActivityDao activityDao;

        //Token of the movie of the user that is watching the movie.
        private String token;
        private Long movieId;
        private List<Clip> movie;
        private int currentClipIndex=-1; // Starts with -1 because there isn't a movie
        private boolean adTime=false;

    /**
     *  Starts playing the movie.
     * @param movieID the identification of the movie that the user want to play
     * @param userToken the token of the user that is trying to play the movie
     * @throws CancelActionException when the token is no longer valid.
     */
    public void startMovie(Long movieID, String userToken) throws CancelActionException{
        this.token=userToken;
        this.movieId=movieID;
        adTime=false;

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

    /**
     * Provides the next clip of a movie
     * @return the bytes that conform the clip
     * @throws CancelActionException if the getCurrentClip() method thrwos it
     * @throws ClipDoesntExistException if doesnt exist a next clip
     */
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

    /**
     * Provides the next clip of a movie
     * @return the bytes that conform the clip
     * @throws CancelActionException if the getCurrentClip() method thrwos it
     * @throws ClipDoesntExistException if doesn't exist a next clip
     */
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

    /**
     * Provides the current clip of a movie
     * @return  the bytes that conform the clip
     * @throws CancelActionException if the token is not valid
     */
        public byte[] getCurrentClip() throws CancelActionException {
        ClipData clipData;

        try{
            clipData = movieDao.getClipData(token,movie.get(currentClipIndex).getId());
            sendActivity();
        } catch (InvalidTokenException e){
            throw new CancelActionException("Your session is no longer valid, please login again.");
        }

        return getBytes(clipData.getBytes());
    }

    /**
     * Provides the necessary Advertisement
     * @return the bytes of the ad, mp4 format
     * @throws CancelActionException if the token is not valid
     * @throws NoAdsException when there are no ads to get
     */
    private byte[] getAd() throws CancelActionException, NoAdsException {
        ClipData clipData;
        try{
            clipData = movieDao.getAd(token, movieId);
            sendActivity();
        } catch (InvalidTokenException e){
            throw new CancelActionException("Your session is no longer valid, please login again.");
        }

        return getBytes(clipData.getBytes());
    }

    /**
     *  Sends the current Activity to ActivityDao.
     */
    public void sendActivity() throws InvalidTokenException{
        // Sends the information about this Activity
        Activity activity;
        UserController userController = new UserController();
        User user = userController.getUserOfToken(token);
        Date date = new Date();
        activity = new Activity(movieId,currentClipIndex, date.getTime(),user);
        activityDao.addActivity(activity);
    }


    /**
     * Transform an array from Byte's objects to bytes
     * @param bytes An array of Bytes
     * @return An array of bytes
     */
    private byte[] getBytes(Byte[] bytes){
        byte[] normalBytes = new byte[bytes.length];
        for (int i=0;i<bytes.length;i++){
              normalBytes[i]=bytes[i].byteValue();
        }
        return normalBytes;
    }

}
