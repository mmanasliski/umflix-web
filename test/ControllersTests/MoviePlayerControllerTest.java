package ControllersTests;

import controllers.MoviePlayerController;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.MovieNotFoundException;
import exception.CancelActionException;
import model.MovieManager;
import model.exceptions.UserNotAllowedException;
import org.junit.Test;
import org.mockito.*;
import services.MovieManagerImpl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MoviePlayerControllerTest {

    class MoviePlayerControllerMock extends MoviePlayerController {

        public MoviePlayerControllerMock(){
            this.movieDao=Mockito.mock(MovieManagerImpl.class);
        }


    }

    @Test
    public void testStartMovie(){
     MoviePlayerControllerMock moviePlayerControllerMock = new MoviePlayerControllerMock();
        Long id=1L;
        String token="pepe";
        try {
            moviePlayerControllerMock.startMovie(id,token);
        } catch (CancelActionException e) {
        }
        try{
            verify(moviePlayerControllerMock.movieDao, times(1)).getMovie(token,id);
        } catch (InvalidTokenException e) {
        } catch (UserNotAllowedException e) {
        } catch (MovieNotFoundException e) {
        }

    }

    public void testStartMovieNotFound(){
        MoviePlayerControllerMock moviePlayerControllerMock = new MoviePlayerControllerMock();
        Long id=1L;
        String token="pepe";

        try {
            doThrow(new MovieNotFoundException()).when((moviePlayerControllerMock.movieDao).getMovie(token, id));
        } catch (InvalidTokenException e) {
            fail();
        } catch (MovieNotFoundException e) {
               try{
                moviePlayerControllerMock.startMovie(id, token);
               }catch(Exception x){
                   assertTrue(x instanceof CancelActionException);
               }
        } catch (UserNotAllowedException e) {
            fail();
        }

    }

    public void testStartMovieUserNotAllowed(){
        MoviePlayerControllerMock moviePlayerControllerMock = new MoviePlayerControllerMock();
        Long id=1L;
        String token="pepe";

        try {
            doThrow(new UserNotAllowedException("message")).when((moviePlayerControllerMock.movieDao).getMovie(token, id));
        } catch (InvalidTokenException e) {
            fail();
        } catch (MovieNotFoundException e) {
             fail();
        } catch (UserNotAllowedException e) {
            try{
                moviePlayerControllerMock.startMovie(id, token);
            }catch(Exception x){
                assertTrue(x instanceof CancelActionException);
            }
        }

    }

    public void testStartMovieInvalidToken(){
        MoviePlayerControllerMock moviePlayerControllerMock = new MoviePlayerControllerMock();
        Long id=1L;
        String token="pepe";

        try {
            doThrow(new InvalidTokenException()).when((moviePlayerControllerMock.movieDao).getMovie(token, id));
        } catch (InvalidTokenException e) {
            try{
                moviePlayerControllerMock.startMovie(id, token);
            }catch(Exception x){
                assertTrue(x instanceof CancelActionException);
            }
        } catch (MovieNotFoundException e) {
            fail();
        } catch (UserNotAllowedException e) {
             fail();
        }
    }




    }