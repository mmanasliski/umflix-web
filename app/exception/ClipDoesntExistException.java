package exception;

/**
 *
 * This exception is thrown when a request wasn't successful and it's needed to notify the user of the reason
 * The message set in the exception will be the message that explains the user what was the problem
 *
 */
public class ClipDoesntExistException extends Exception {

    static final long serialVersionUID = 1;

    public ClipDoesntExistException(String message){
        super(message);
    }

}