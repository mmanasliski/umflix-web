package controllers;

import dao.DaoFactory;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.MovieNotFoundException;
import edu.umflix.model.Clip;
import edu.umflix.model.ClipData;
import exception.CancelActionException;
import model.MovieManager;
import model.exceptions.UserNotAllowedException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * This controller manages the view of a Movie.
 *
 */
public class MoviePlayerController {
        // Configuration file name
        private static final String PROPERTIES = "conf.dao_factory";
        private static  ResourceBundle rb = ResourceBundle.getBundle(PROPERTIES);

        // Key for the name of the class that implement MovieManager
        private static final String MOVIE_MANAGER_IMPL_K = "MOVIE_MANAGER_IMPL";

        //Token of the movie of the user that is watching the movie.
        private String token;
        private Long movieId;
        private List<Clip> movie;
        private int currentClipIndex;
        private MovieManager movieDao;

    /*
     * Starts playing the movie.
     * @throws CancelActionException when the token is no longer valid.
     *
     */
    protected OutputStream startMovie(Long movieID, String userToken) throws CancelActionException{
        ClipData clipData;
        ByteArrayOutputStream oStream =new ByteArrayOutputStream();
        movieDao = (MovieManager)(DaoFactory.getDao(rb.getString(MOVIE_MANAGER_IMPL_K)));
        this.token=userToken;
        this.movieId=movieID;
        currentClipIndex=0;
        try {
            try {
                movie = movieDao.getMovie(token, movieId); // Sets the movie the user is watching.
            } catch (MovieNotFoundException e){
                throw new CancelActionException("The movie couldn't be found, please choose another movie.");
            } catch (UserNotAllowedException e){
                throw new CancelActionException("You are not allowed to see this movie.");
            }
            clipData = movieDao.getClipData(token,movie.get(currentClipIndex).getId());
        } catch (InvalidTokenException e){
            throw new CancelActionException("Your session is no longer valid, please login again.");
        }
        byte[] bytes = getBytes(clipData.getBytes());
        oStream.write(bytes,0,clipData.getBytes().length);
        return oStream;
    }

    private byte[] getBytes(Byte[] bytes){
        byte[] normalBytes = new byte[bytes.length];
        for (int i=0;i<bytes.length;i++){
              normalBytes[i]=bytes[i].byteValue();
        }
        return normalBytes;
    }

    /*
     * Continues the playing of the movie.
     */
    private void play(){

    }

    /*
     * Interrupts the playing of the movie.
     */
    private void pause(){

    }

    /*
     *  Stops the playing of the movie and returns to the first clip, paused.
     */
    private void stop(){

    }

    /*
     * @param n The number of clips to go backwards
     */
    private void goBackwards(int n){
        MovieManager movieDao = (MovieManager)(DaoFactory.getDao(rb.getString(MOVIE_MANAGER_IMPL_K)));
         // Stops the current playing, goes backwards n number of clips.
        // Starts playing in that clip.
    }


}
