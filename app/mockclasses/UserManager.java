package mockclasses;

import edu.umflix.authenticationhandler.exceptions.InvalidUserException;
import edu.umflix.model.User;
import edu.umflix.usermanager.impl.UserManagerImpl;

/**
 *
 *  Mocks the UserManager interface.
 *
 */
public class UserManager extends UserManagerImpl {

    @Override
    public String login(User user) throws InvalidUserException {
        return "token";
    }


}
