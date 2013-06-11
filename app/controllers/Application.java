package controllers;

import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.Movie;
import exception.CancelActionException;
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
    public static class PasswordChanger{
        public String oldPassword;
        public String newPassword;
        public String newPasswordConfirm;
    }

    public static class ShowMovies{

    }

    public static class MovieSearcher{
        public String key;
    }

    public static class MovieChooser{
        public long movieId;
    }

    public static Result index() {
        return ok(index.render(form(Login.class)));
    }

    public static Result registerForm(){
        return ok(registerIndex.render(form(Register.class)));
    }
    public static Result changePassword(){
        return ok(changePasswordIndex.render(form(PasswordChanger.class)));
    }

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

    public static Result registering(){
        String message;
        Form<Register> form= form(Register.class).bindFromRequest();
        Register information = form.get();

        //intentar persistir informacion. Debe ir en User Controller
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

    public static Result showMovies(){
        String message;
        List<Movie> movies = null;
        try {
            if(!userController.showMovies().isEmpty()){
                movies = userController.showMovies();
                return ok(showMovies.render(movies));
            }
            message=NO_MOVIES;
            return ok(homePage.render(message));
        } catch (InvalidTokenException e) {
            return ok(index.render(form(Login.class)));
        }

    }

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

    public static Result chooseMovie(Long movieId){
        String message;
        MoviePlayerController moviePlayerController = new MoviePlayerController(userController.getToken(), movieId);
        try {
                moviePlayerController.startMovie(movieId,userController.getToken());
                return ok(movieView.render());
        } catch (CancelActionException e) {
                        return ok(homePage.render(e.getMessage()));
        }
     }
} 
