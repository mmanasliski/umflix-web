package controllers;

import dao.DaoFactory;
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

import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * This controller responds to user's requests and manages their session.
 *
 */

public class UserController {
    // Configuration file name
    private static final String PROPERTIES = "conf.dao_factory";
    private static final String INVALID_EMAIL ="The email you submited is not valid.";
    private static final String INVALID_PASSWD ="Please choose a valid password";
    private static final String INVALID_ROLE ="Please retry the registration process.";
    private static final String EMAIL_TAKEN ="Email already taken, choose another one";
    private static ResourceBundle rb = ResourceBundle.getBundle(PROPERTIES);

    // Key for the name of the classes that implement UserManager and AuthenticationHandler
    private static final String AUTH_HANDLER_IMPL_K = "AUTH_HANDLER_IMPL";
    private static final String USER_MANAGER_IMPL_K = "USER_MANAGER_IMPL";
    private static final String CATALOG_SERVICE_IMPL_K = "CATALOG_SERVICE_IMPL";

    static AuthenticationHandler authHandler = (AuthenticationHandler)(DaoFactory.getDao(rb.getString(AUTH_HANDLER_IMPL_K)));
    //static UserManager userManager = (UserManager)(DaoFactory.getDao(rb.getString(USER_MANAGER_IMPL_K)));
    //static CatalogService catalogService = (CatalogService)(DaoFactory.getDao(rb.getString(CATALOG_SERVICE_IMPL_K)));
    static UserManager userManager=new mockclasses.UserManager();
    static CatalogService catalogService=new mockclasses.CatalogService();

    private static final String MOVIE_MANAGER_IMPL_K = "MOVIE_MANAGER_IMPL";


    // Indicates if the WebApp has a session opened
    private boolean sessionOpened;

   // User's session token
    private String token;

    public String getToken() {
        return token;
    }


    /*
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

    /*
     * @param userEmail The email address by which the user wants to be registered.
     * @param password The password of the user.
     * @throws CancelActionException if registration wasn't accepted.
     */
    public void register(String userName, String userEmail, String password) throws CancelActionException{
        Role roleUser = new Role((long)0);
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

    /*
     * @param password The new password the user wants to be set.
     * @param newPassword The current password of the user.
     * @throws CancelActionException if the update wasn't accepted with those parameters.
     */
    public boolean updatePassword(String password, String newPassword){
        boolean updated=false;
        if(authHandler.validateToken(token)){
            try{
                 User user = authHandler.getUserOfToken(token);   //tries to get the user from token
                try {
                    userManager.update(token,user,newPassword);
                    updated=true;
                } catch (InvalidUserException e) {
                } catch (PermissionDeniedException e) {
                } catch (InvalidPasswordException e) {
                } catch (InvalidTokenException e) {
                }
            }catch(InvalidTokenException e){
             }
        }else{
            //log error de validacion
        }
        return updated;
        // Gets the User from the session's token
        // Invokes UserManager to do the registration.
    }

    /*
    * @ UMFlix's movies
    */
    public List<Movie> showMovies() throws InvalidTokenException {
        // Uses the web service to access to CatalogService and searches with NULL parameters
        // Mock implementation

        List<Movie> movieList;
        //CatalogServiceImpl catalogService = new CatalogServiceImpl();
        try{
            movieList = catalogService.search(null, null);
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException();
        }
        return movieList;
    }

    /*
     * @param key The key is used to compare movie's attributes with it.
     * @return Returns the movie that matches they key
     * @throws CancelActionException if there weren't movies matching the key.
     */
    public List<Movie> searchMovie(String key) throws CancelActionException, InvalidTokenException {

        // Uses the web service to access to CatalogService and searches with the key parameter
        // Mock implementation
        Movie movie;
        //CatalogServiceImpl catalogService = new CatalogServiceImpl();
        try{
            List<Movie> movieList = catalogService.search(key, this.token);
            if(movieList!=null && !movieList.isEmpty()){
               return movieList;
            }else throw new CancelActionException("Movie not found");
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException();
        }
    }

    /*
     * Creates a new view of the movie.
     */
    public void chooseMovie(Long movieId) throws CancelActionException {
        // Creates a new MoviePlayerController with movieId and token parameters and calls startMovie()
        MoviePlayerController moviePlayerController = new MoviePlayerController();
        moviePlayerController.startMovie(movieId, this.token);
    }

}
