package movierecsys.dal.DB;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.interfaces.IRatingDataAccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class RatingDbDAO implements IRatingDataAccess {
    @Override
    public void createRating(Rating rating) {
        
    }

    @Override
    public void updateRating(Rating rating) {

    }

    @Override
    public void deleteRating(Rating rating) {

    }

    @Override
    public List<Rating> getAllRatings() {
        return null;
    }

    @Override
    public List<Rating> getRatings(User user) {
        return null;
    }

    @Override
    public List<Rating> getMovieRatings(Movie movie) throws FileNotFoundException, IOException {
        return null;
    }
}
