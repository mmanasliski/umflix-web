package controllers;

import edu.um.arq.umflix.catalogservice.CatalogService;
import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.model.Movie;
import edu.umflix.model.Role;
import edu.umflix.model.User;
import edu.umflix.usermanager.UserManager;
import edu.umflix.usermanager.exceptions.*;
import exception.CancelActionException;

import javax.ejb.EJB;
import java.util.List;

import static controllers.BeanFactory.getBean;

/**
 *
 * This controller responds to user's requests and manages their session.
 *
 */

public class UserController {
    // Configuration file name
    private static final String INVALID_EMAIL ="The email you submited is not valid.";
    private static final String INVALID_PASSWD ="Please choose a valid password";
    private static final String INVALID_ROLE ="Please retry the registration process.";
    private static final String EMAIL_TAKEN ="Email already taken, choose another one";

    private AuthenticationHandler authHandler = (AuthenticationHandler)getBean("AuthenticationService");
    private UserManager userManager = (UserManager) getBean("UserManager");
    private CatalogService catalogService = (CatalogService) getBean("CatalogService");

    /**
     * Indicates if the WebApp has a session opened
     */
    private boolean sessionOpened;

    /**
     *User's session token
     */
       private String token;

    /**
     * getter of the token
     * @return token
     */
    public String getToken() {
        return token;
    }


    /**
     *  Log the user to the system
     * @param userEmail The email address by which the user is registered.
     * @param password The password of the user.
     * @throws CancelActionException if login wasn't accepted.
     */
    public void login(String userEmail, String password) throws CancelActionException{

        // Creates an User with Role User, invokes UserManager to do the login.
        User user = new User(userEmail, null, password, null);//supuse mail y name iguales
        try{
            token = userManager.login(user);
        } catch (InvalidUserException e) {
            throw new CancelActionException("user or password invalid");
        }
    }

    /**
     * Register an user in the system
     * @param userEmail The email address by which the user wants to be registered.
     * @param password The password of the user.
     * @throws CancelActionException if registration wasn't accepted.
     */
    public void register(String userName, String userEmail, String password) throws CancelActionException{
        Role roleUser = new Role(Role.RoleType.USER.getRole());
        User user=new User(userEmail,userName,password,roleUser);
        try {
            userManager.register(user);
        } catch (InvalidEmailException e) {
            throw new CancelActionException(INVALID_EMAIL);
        } catch (InvalidPasswordException e) {
            throw new CancelActionException(INVALID_PASSWD);
        } catch (InvalidRoleException e) {
            throw new CancelActionException(INVALID_ROLE);
        } catch (EmailAlreadyTakenException e) {
            throw new CancelActionException(EMAIL_TAKEN);
        }
    }

    /**
     * Updates user's password
     * @param password The new password the user wants to be set.
     * @param newPassword The current password of the user.
     * @throws CancelActionException if the update wasn't accepted with those parameters.
     */
    public boolean updatePassword(String password, String newPassword){
        boolean updated=false;
        if(password!=null && newPassword!=null){
            if(authHandler.validateToken(token)){
                try{
                    User user = authHandler.getUserOfToken(token); //tries to get the user from token
                    //checks that user password is the same that the one inserted
                    if(user.getPassword().equals(password)){
                        try {
                            userManager.update(token,user,newPassword);
                            updated=true;
                        } catch (InvalidUserException e) {
                        } catch (PermissionDeniedException e) {
                        } catch (InvalidPasswordException e) {
                        } catch (InvalidTokenException e) {
                        }
                    }
                }catch(InvalidTokenException e){
                }
            }else{
                //log error de validacion
            }
        }

        return updated;
        // Gets the User from the session's token
        // Invokes UserManager to do the registration.
    }


    /**
     * Create a list of the UMFlix's movies
     * @return The list with all the selected movies
     * @throws InvalidTokenException when the token is not valid for the user
     */
    public List<Movie> showMovies() throws InvalidTokenException {
        // Uses the web service to access to CatalogService and searches with NULL parameters
        // Mock implementation

        List<Movie> movieList;
        try{
            movieList = catalogService.search(null, null);
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException();
        }
        return movieList;
    }

    /**
     * Search the movie with a keyword restriction
     * @param key The key is used to compare movie's attributes with it.
     * @return Returns the movie that matches they key
     * @throws CancelActionException if there weren't movies matching the key.
     */
    public List<Movie> searchMovie(String key) throws CancelActionException, InvalidTokenException {

        // Uses the web service to access to CatalogService and searches with the key parameter
        // Mock implementation
        Movie movie;
        try{
            List<Movie> movieList = catalogService.search(key, this.token);
            if(movieList!=null && !movieList.isEmpty()){
               return movieList;
            }else throw new CancelActionException("Movie not found");
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException();
        }
    }

    /**
     * Start the play of a moovie
     * @param movieId the identification of the movie to play
     * @throws CancelActionException when moviePlayerControllers throws it
     */
    public void chooseMovie(Long movieId) throws CancelActionException {
        // Creates a new MoviePlayerController with movieId and token parameters and calls startMovie()
        MoviePlayerController moviePlayerController = new MoviePlayerController();
        moviePlayerController.startMovie(movieId, this.token);
    }

    /**
     *
     * @param token of the user to get
     * @return the user to which the token belongs
     * @throws InvalidTokenException when the token is invalid
     */
    public User getUserOfToken(String token) throws InvalidTokenException{
         return authHandler.getUserOfToken(token);
    }

}
