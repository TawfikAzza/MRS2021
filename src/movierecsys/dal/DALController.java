package movierecsys.dal;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.DB.UserDbDAO;
import movierecsys.dal.files.MovieDAO;
import movierecsys.dal.files.RatingDAO;
import movierecsys.dal.files.UserDAO;
import movierecsys.dal.interfaces.IMovieDataAccess;
import movierecsys.dal.interfaces.IRatingDataAccess;
import movierecsys.dal.interfaces.IUserDataAccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class DALController implements IDALFacade{
    private IMovieDataAccess movieDAO;
    private IRatingDataAccess ratingDAO;
    private IUserDataAccess userDAO;
    private IUserDataAccess userDbDAO;
    public DALController() throws IOException {
        movieDAO = new MovieDAO();
        ratingDAO = new RatingDAO();
        userDAO = new UserDAO();
        userDbDAO = new UserDbDAO();
    }

    @Override
    public void createRating(Rating rating) {
        ratingDAO.createRating(rating);
    }

    @Override
    public void updateRating(Rating rating) {
        ratingDAO.updateRating(rating);
    }

    @Override
    public void deleteRating(Rating rating) {
        ratingDAO.deleteRating(rating);
    }

    @Override
    public List<Rating> getAllRatings() {
        return ratingDAO.getAllRatings();
    }

    @Override
    public List<Rating> getRatings(User user) {
        return ratingDAO.getRatings(user);
    }

    @Override
    public List<Rating> getMovieRatings(Movie movie) throws FileNotFoundException, IOException {
        return ratingDAO.getMovieRatings(movie);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return userDAO.getUser(id);
    }

    @Override
    public void updateUser(User user) throws IOException {
        userDAO.updateUser(user);

    }

    @Override
    public User createUser(String name) throws FileNotFoundException, IOException {
        return userDAO.createUser(name);
    }

    @Override
    public List<Movie> getAllMovies() throws FileNotFoundException, IOException {
        return movieDAO.getAllMovies();
    }

    @Override
    public Movie createMovie(int releaseYear, String title) {
        return movieDAO.createMovie(releaseYear, title);
    }

    @Override
    public void deleteMovie(Movie movie) throws IOException {
        movieDAO.deleteMovie(movie);
    }

    @Override
    public void updateMovie(Movie movie) {
        movieDAO.updateMovie(movie);
    }

    @Override
    public Movie getMovie(int id) {
        return movieDAO.getMovie(id);
    }

}
