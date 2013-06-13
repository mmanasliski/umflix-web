package ControllersTests;

import java.util.ResourceBundle;

import controllers.MoviePlayerController;
import controllers.UserController;
import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.authenticationhandler.impl.AuthenticationHandlerImpl;
import edu.umflix.model.Role;
import edu.umflix.model.User;
import edu.umflix.usermanager.UserManager;
import edu.umflix.usermanager.exceptions.EmailAlreadyTakenException;
import edu.umflix.usermanager.exceptions.InvalidEmailException;
import edu.umflix.usermanager.exceptions.InvalidPasswordException;
import edu.umflix.usermanager.exceptions.InvalidRoleException;
import exception.CancelActionException;
import org.junit.Test;
import org.mockito.Mockito;
import edu.umflix.authenticationhandler.impl.AuthenticationHandlerImpl;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    /*
+     *
+     * This class mocks UserController so it uses mocked classes for the UserManager's methods implementation
+     *
+     */


    class UserControllerMock extends UserController {
        private AuthenticationHandler authHandler = Mockito.mock(AuthenticationHandlerImpl.class);
        private UserManager userManager = Mockito.mock(UserManager.class);
    }

    @Test
    public void testLogin(){
        Role roleUser = new Role((long)0);
        UserControllerMock testedMock = new UserControllerMock();
        String userEmail="hola@gmail.com";
        String password="password";
        User user = new User(userEmail, null, password, roleUser);
        try {
            testedMock.login(userEmail,password);
        } catch (CancelActionException e) {
            fail();
        }
        try {
            Mockito.verify(testedMock.userManager).login(user);
        } catch (InvalidUserException e) {
            fail();
        }

    }

    @Test
    public void testRegister(){
        UserControllerMock testedMock = new UserControllerMock();
        String userEmail="";
        String name="";
        String password="";
        User user = new User(userEmail, name, password, null);
        try {
            testedMock.register(userEmail,name,password);
        } catch (CancelActionException e) {
            fail();
        }
        try {
            Mockito.verify(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            fail();
        } catch (InvalidPasswordException e) {
            fail();
        } catch (InvalidRoleException e) {
            fail();
        } catch (EmailAlreadyTakenException e) {
            fail();
        }
    }
}
