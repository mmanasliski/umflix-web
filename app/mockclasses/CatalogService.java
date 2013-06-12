package mockclasses;

import edu.um.arq.umflix.catalogservice.exception.DaoException;
import edu.umflix.authenticationhandler.exceptions.InvalidTokenException;
import edu.umflix.model.Movie;

import java.util.Collections;
import java.util.List;

/**
 *
 *
 */
public class CatalogService implements edu.um.arq.umflix.catalogservice.CatalogService{
    @Override
    public List<Movie> search(String s, String s2) throws DaoException, InvalidTokenException {
        return null;
    }
}
