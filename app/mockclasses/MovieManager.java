package mockclasses;


import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.exceptions.MovieNotFoundException;
import edu.umflix.model.*;
import edu.umflix.model.Clip;
import model.exceptions.NoAdsException;
import model.exceptions.UserNotAllowedException;
import model.exceptions.ValuesInActivityException;

import java.io.FileNotFoundException;
import java.util.List;

/**
 *
 *  Mocks the MovieManager interface.
 *
 */
public class MovieManager implements model.MovieManager {
    @Override
    public List<Clip> getMovie(String s, Long aLong) throws InvalidTokenException, MovieNotFoundException, UserNotAllowedException {
        return null;
    }

    @Override
    public ClipData getClipData(String s, Long aLong) throws InvalidTokenException, FileNotFoundException {
        return null;
    }

    @Override
    public void sendActivity(String s, Activity activity) throws InvalidTokenException, ValuesInActivityException, UserNotAllowedException {

    }

    @Override
    public ClipData getAd(String s, Long aLong) throws InvalidTokenException, NoAdsException {
        return null;
    }
}
