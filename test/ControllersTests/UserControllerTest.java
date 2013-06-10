package ControllersTests;

import java.util.ResourceBundle;
import controllers.UserController;
import edu.umflix.authenticationhandler.AuthenticationHandler;
import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.authenticationhandler.impl.AuthenticationHandlerImpl;
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

public class UserControllerTest {
    /*
+     *
+     * This class mocks UserController so it uses mocked classes for the UserManager's methods implementation
+     *
+     */
    class UserControllerMock extends UserController {
        private AuthenticationHandler authHandler = Mockito.mock(AuthenticationHandlerImpl.class);
        private UserManager userManager = Mockito.mock(UserManager.class);     //Cambiar por la implementacion de UserManager

    }

    @Test
    public void testLogin(){
        UserControllerMock testedMock = new UserControllerMock();
        String userEmail="";
        String password="";
        User user = new User(userEmail, null, password, null);
        try {
            testedMock.login(userEmail,password);
        } catch (CancelActionException e) {
            fail("Error al login");
        }
        try {
            Mockito.verify(testedMock.userManager).login(user);
        } catch (InvalidUserException e) {
            fail("User invalido");
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
            fail("Error en registro");
        }
        try {
            Mockito.verify(testedMock.userManager).register(user);
        } catch (InvalidEmailException e) {
            fail("Invalid email");
        } catch (InvalidPasswordException e) {
            fail("Invalid password");
        } catch (InvalidRoleException e) {
            fail("Invalid role");
        } catch (EmailAlreadyTakenException e) {
            fail("Email taken");
        }
    }
}
