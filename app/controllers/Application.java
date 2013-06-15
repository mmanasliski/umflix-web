package controllers;

import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.Movie;
import exception.CancelActionException;
import exception.ClipDoesntExistException;
import org.apache.commons.io.IOUtils;
import play.api.mvc.Response;
import play.data.*;
import play.mvc.*;
import play.*;
import play.data.validation.Constraints.*;
import views.html.*;
import static play.data.Form.*;

import play.data.Form;
import play.mvc.Result;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import play.data.validation.Constraints.*;

import java.io.*;
import java.util.*;

import views.html.*;


import java.util.List;

public class Application extends Controller {

    private static final String EMAIL_ERROR ="Email already used, please try another";
    private static final String PASSWORD_MISSMATCH ="The passwords you inserted did not match";
    private static final String SUCCEDED =" created. Please head back to login page and sign in !";
    private static final String SUCCEDED_TO_CHANGE_PASSWORD ="You have updated your password succesfully";
    private static final String MOVIE_NOT_FOUND = "Movie not found";
    private static final String MOVIE_NOT_AVAILABLE  = "Movie is not available now, try again later";
    private static final String NO_MOVIES = "No movies to show";
    private static final String COULD_NOT_CHANGE_PASSWORD ="Please retry changing your password, something went wrong";
    static UserController userController= new UserController();
    static MoviePlayerController moviePlayerController=new MoviePlayerController();

    /**
     * Describes the login form.
     */
    public static class Login {
        public String name;
        public String password;
    }

    /**
     * Describes the Register form.
     */
    public static class Register {
        public String email;
        public String name;
        public String password;
        public String secondPassword;
    }

    /**
     * Describes the form to change the password
     */
    public static class PasswordChanger{
        public String oldPassword;
        public String newPassword;
        public String newPasswordConfirm;
    }

    /**
     * Describes an auxiliar form that contains the movie's key
     */
    public static class MovieSearcher{
        public String key;
    }

    /**
     * Loads the index
     * @return Load the index page with the login form
     */
    public static Result index() {
        return ok(index.render(form(Login.class)));
    }

    /**
     * Loads the page to allow the user to do the register
     * @return Return the request to load the registerIndex page with the register form
     */
        public static Result registerForm(){
        return ok(registerIndex.render(form(Register.class)));
    }

    /**
     *Loads the page to allow the user to do change his password
     * @return Return the request to load the changePasswordIndex page with the register form
     */
    public static Result changePassword(){
        return ok(changePasswordIndex.render(form(PasswordChanger.class)));
    }

    /**
     * Acept or deny the log of the user
     * @return Return the homePage if the log was successful or a message if it wasn't
     */
    public static Result login(){
        String message;
        Form<Login> form = form(Login.class).bindFromRequest();
        Login data = form.get();
        try{
            message = "WELCOME";
            userController.login(data.name, data.password);
            return ok(homePage.render(message));
        }catch(CancelActionException e){
            return unauthorized(e.getMessage());
        }
    }

    /**
     * Register a new user to the system
     * @return reload the same page if there was an error or a message if the register was successful
     */
    public static Result registering(){
        String message;
        Form<Register> form= form(Register.class).bindFromRequest();
        Register information = form.get();
        if(information.password.equals(information.secondPassword)){
            try {
                userController.register(information.name,information.email,information.password);
                message="Account "+information.email+" created. Please go back and login to start watching!";
                return ok(register.render(message));
            } catch (CancelActionException e) {
                e.printStackTrace();
                return ok(registerIndex.render(form(Register.class)));
            }
        }else{
            message=PASSWORD_MISSMATCH;
            return ok(registerIndex.render(form(Register.class)));
        }
    }

    /**
     * Change the user's password
     * @return Reload the page with a message telling if the request was SUCCEDED or not
     */
    public static Result changeMyPassword(){
        String message;
        Form<PasswordChanger> form = form(PasswordChanger.class).bindFromRequest();
        PasswordChanger information = form.get();
        if(information.newPassword.equals(information.newPasswordConfirm)){
            if(userController.updatePassword(information.oldPassword,information.newPassword)){
                message=SUCCEDED_TO_CHANGE_PASSWORD;
                return ok(changePassword.render(message));
            } else{
                message=COULD_NOT_CHANGE_PASSWORD;
                return ok(changePassword.render(message));
            }
        }else{
            return ok(changePasswordIndex.render(form(PasswordChanger.class)));
        }

    }

    /**
     * Show all the availables movies
     * @return Loads a page with all the results or loads the homePage if there was an error with the user's token
     */
    public static Result showMovies(){
        String message;
        List<Movie> movies = null;
        try {
            if(userController.showMovies()!=null && !userController.showMovies().isEmpty()){
                movies = userController.showMovies();
                return ok(showMovies.render(movies));
            }

            //PRUEBA
            movies = new ArrayList<Movie>();
            Movie movie = new Movie();
            movie.setId(1L);
            movie.setDirector("Director");
            movie.setGenre("Genre");
            movie.setTitle("Movie title");
            movies.add(0,movie);
            return ok(showMovies.render(movies));
            //PRUEBA

            //message=NO_MOVIES;
            //return ok(homePage.render(message));
        } catch (InvalidTokenException e) {
            return ok(index.render(form(Login.class)));
        }
    }

    /**
     * Search all the movies that contains the keyword requested
     * @return Loads a page showing all the results or loads the homepage showing an appropriate message if an error occurred
     */
    public static Result searchMovie(){
        String message;
        List<Movie> movies = null;
        Form<MovieSearcher> form = form(MovieSearcher.class).bindFromRequest();
        MovieSearcher information = form.get();
        try {
            if(!userController.searchMovie(information.key).isEmpty()){
                movies= userController.searchMovie(information.key);
                return ok(showMovies.render(movies));
            }
            message=MOVIE_NOT_FOUND;
            return ok(homePage.render(message));
        } catch (CancelActionException e) {
            message=MOVIE_NOT_FOUND;
            return ok(homePage.render(message));
        } catch (InvalidTokenException e) {
            return ok(index.render(form(Login.class)));
        }
    }

    /**
     * Start playing the movie chose
     * @param movieId The movie's identifier
     * @param movieName The name of the movie chose
     * @return Loads a new page with the player or the homePage if there was an error
     */
    public static Result chooseMovie(Long movieId, String movieName){
        try {
              moviePlayerController.startMovie(movieId, userController.getToken());
              return ok(movieView.render(movieName,true));
        } catch (CancelActionException e) {
            return ok(homePage.render(e.getMessage()));
        }
    }

    /**
     * Refresh the view of the movie if occurs the request of the next clip
     * @param title The title of the actual movie
     * @return The view of the movie with the clip requested
     */
    public static Result refreshViewPrevClip(String title){
        return ok(prevView.render(title,false));
    }

    /**
     * ma
     * @return The next clip that will be loaded in the player, or Load the homePage if there was an error
     */
    public static Result getPreviousClip(){
         byte[] bytes;
        try {
            bytes = moviePlayerController.prevClip();
            response().setContentType("video/mp4");
            return ok(bytes);
        }catch (CancelActionException e) {
            return ok(homePage.render(e.getMessage()));  //To change body of catch statement use File | Settings | File Templates.
        }catch (ClipDoesntExistException e){
            try {
                bytes = moviePlayerController.getCurrentClip();
            } catch (CancelActionException es) {
                return ok(homePage.render(e.getMessage()));
            }
            response().setContentType("video/mp4");
            return ok(bytes);
        }
    }

    /**
     * Refresh the view of the movie if occurs the request of the next clip
     * @param title The title of the actual movie
     * @return The view of the movie with the clip requested
     */
    public static Result refreshViewNextClip(String title){
        return ok(nextView.render("Stupid dog.",true));
    }

    public static Result getNextClip(){
        byte[] bytes;
        try {
            bytes = moviePlayerController.nextClip();
            response().setContentType("video/mp4");
            return ok(bytes);
        }catch (CancelActionException e) {
            return ok(homePage.render(e.getMessage()));  //To change body of catch statement use File | Settings | File Templates.
        }catch (ClipDoesntExistException e){
            try {
                bytes = moviePlayerController.getCurrentClip();
            } catch (CancelActionException es) {
                return ok(homePage.render(e.getMessage()));
            }
            response().setContentType("video/mp4");
            return ok(bytes);
        }
    }
} 
