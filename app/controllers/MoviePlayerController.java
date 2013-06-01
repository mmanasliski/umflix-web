package controllers;

import dao.DaoFactory;
import exception.CancelActionException;
import mockclasses.*;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * This controller manages the view of a Movie.
 *
 */
public class MoviePlayerController {
        // Configuration file name
        private static final String PROPERTIES = "dao_factory";
        private static  ResourceBundle rb = ResourceBundle.getBundle(PROPERTIES);

        // Key for the name of the class that implement MovieManager
        private static final String MOVIE_MANAGER_IMPL_K = "MOVIE_MANAGER_IMPL";

        //Token of the movie of the user that is watching the movie.
        private String token;
        private Long movieId;
        private List<Clip> movie;

        public MoviePlayerController(String token, Long movieId){
            this.token=token;
            this.movieId=movieId;
        }

    /*
     * Starts playing the movie.
     * @throws CancelActionException when the token is no longer valid.
     */
    private void startMovie(Long movieId, String token) throws CancelActionException{
        MovieManager movieDao = (MovieManager)(DaoFactory.getDao(rb.getString(MOVIE_MANAGER_IMPL_K)));
             // Uses MovieManager's getMovie to set the variable movie
             // Asks MovieManager for first ClipData and shows it
             // When it finishes ask for ad and shows, then clip, then add till movie finishes
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
