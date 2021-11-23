package movierecsys.dal.interfaces;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IRatingDataAccess {

    void createRating(Rating rating);

    void updateRating(Rating rating);

    void deleteRating(Rating rating);

    List<Rating> getAllRatings();

    List<Rating> getRatings(User user);

    List<Rating> getMovieRatings(Movie movie) throws FileNotFoundException, IOException;

}
