package movierecsys.dal;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IDALFacade {

    /*
    *
    * Rating section of the general interface
    *
    * */
    void createRating(Rating rating);

    void updateRating(Rating rating);

    void deleteRating(Rating rating);

    List<Rating> getAllRatings();

    List<Rating> getRatings(User user);

    List<Rating> getMovieRatings(Movie movie) throws FileNotFoundException, IOException;

    /*
    *
    * User section of the General interface.
    *
    *
    * */

    List<User> getAllUsers();

    User getUser(int id);

    void updateUser(User user) throws IOException;

    public User createUser(String name) throws FileNotFoundException, IOException;

    /*
    *
    * Movie section of the General interface.
    *
    * */

    List<Movie> getAllMovies() throws FileNotFoundException, IOException;

    Movie createMovie(int releaseYear, String title);

    void deleteMovie(Movie movie) throws IOException;

    void updateMovie(Movie movie);

    Movie getMovie(int id);

}
