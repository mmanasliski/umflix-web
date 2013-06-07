package controllers;

import dao.DaoFactory;
import exception.CancelActionException;
import mockclasses.AuthenticationHandler;
import mockclasses.Movie;
import mockclasses.MovieManager;

import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * This controller responds to user's requests and manages their session.
 *
 */

public class UserController {
    // Configuration file name
    private static final String PROPERTIES = "dao_factory";
    private static ResourceBundle rb = ResourceBundle.getBundle(PROPERTIES);

    // Key for the name of the classes that implement UserManager and AuthenticationHandler
    private static final String USER_MANAGER_IMPL_K = "USER_MANAGER_IMPL";
    private static final String AUTH_HANDLER_IMPL_K = "AUTH_HANDLER_IMPL";

    // Indicates if the WebApp has a session opened
    private boolean sessionOpened;

    // User's session token
    private String token;

    //UserManager's bean
    @EJB(beanName = "UserManager")
    UserManager userManager;

    /*
     * @param userEmail The email address by which the user is registered.
     * @param password The password of the user.
     * @throws CancelActionException if login wasn't accepted.
     */
    public void login(String userEmail, String password) throws CancelActionException{
        //UserManager userManager = (UserManager)(DaoFactory.getDao(rb.getString(USER_MANAGER_IMPL_K))); userManager no es un DAO ???
        // Creates an User with Role User, invokes UserManager to do the login.
        Role roleUser = new Role(Role.roleType.USER);//ver bien!!!
        User user = new UserController(userEmail, userEmail, password, roleUser);//supuse mail y name iguales
        try{
            String token = userManager.login(user); //el token no lo deberia manejar el usermanager?????
        }catch(InvalidUserException iue){
            throw new CancelActionException("user or password invalid");
        }
    }

    /*
     * @param userEmail The email address by which the user wants to be registered.
     * @param password The password of the user.
     * @throws CancelActionException if registration wasn't accepted.
     */
    public void register(String userName, String userEmail, String password) throws CancelActionException{
        UserManager userManager = (UserManager)(DaoFactory.getDao(rb.getString(USER_MANAGER_IMPL_K)));
         // Creates an User with Role User, charges the data and invokes UserManager to do the registration.
    }

    /*
     * @param password The new password the user wants to be set.
     * @param newPassword The current password of the user.
     * @throws CancelActionException if the update wasn't accepted with those parameters.
     */
    public boolean updatePassword(String password, String newPassword){
        AuthenticationHandler authHandler = (AuthenticationHandler)(DaoFactory.getDao(rb.getString(AUTH_HANDLER_IMPL_K)));
        UserManager userManager = (UserManager)(DaoFactory.getDao(rb.getString(USER_MANAGER_IMPL_K)));

        // Mock implementation
        return false;
        // Gets the User from the session's token
        // Invokes UserManager to do the registration.

    }

    /*
    * @ UMFlix's movies
    */
    public List<Movie> showMovies(){
        // Uses the web service to access to CatalogService and searches with NULL parameters
        // Mock implementation
        return null;
    }

    /*
     * @param key The key is used to compare movie's attributes with it.
     * @return Returns the movie that matches they key
     * @throws CancelActionException if there weren't movies matching the key.
     */
    public Movie searchMovie(String key) throws CancelActionException{

        // Uses the web service to access to CatalogService and searches with the key parameter
        // Mock implementation
        return null;

    }

    /*
     * Creates a new view of the movie.
     */
    public void chooseMovie(){
         // Creates a new MoviePlayerController with movieId and token parameters and calls startMovie()
    }

}
