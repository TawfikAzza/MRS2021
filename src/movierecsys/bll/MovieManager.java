package movierecsys.bll;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.bll.util.MovieRecommender;
import movierecsys.bll.util.MovieSearcher;
import movierecsys.dal.DALController;
import movierecsys.dal.IDALFacade;
import java.io.IOException;
import java.util.List;


public class MovieManager implements OwsLogicFacade{

    private final MovieSearcher movieSearcher;
    private final MovieRecommender movieRecommender;
   // private IMovieDataAccess movieDAO;
    private final IDALFacade idalFacade;
    public MovieManager() throws IOException {
        movieSearcher=new MovieSearcher();
        movieRecommender = new MovieRecommender();
       // movieDAO= new MovieDAO();
        idalFacade = new DALController();

    }
    /*
    *
    * Don't know what is expected of us there I must admit...
    *
    * */

    @Override
    public List<Rating> getRecommendedMovies(User user) {
        return null;
    }

    @Override
    public List<Movie> getAllTimeTopRatedMovies() throws IOException {
        User user1 = new User(-1,"Tawfik");
        return movieRecommender.highAverageRecommendations(idalFacade.getAllMovies(),idalFacade.getAllRatings(),idalFacade.getRatings(user1));
       // return null;
        //return null;
    }

    @Override
    public List<Movie> getMovieReccomendations(User user) {
        return null;
    }

    @Override
    public List<Movie> searchMovies(String query) throws IOException {
        if(query.length()>0)
            return movieSearcher.search(idalFacade.getAllMovies(),query);
        else
            return idalFacade.getAllMovies();

    }

    @Override
    public Movie createMovie(int year, String title) {

        return idalFacade.createMovie(year,title);
    }

    @Override
    public void updateMovie(Movie movie) {
        idalFacade.updateMovie(movie);
    }

    @Override
    public void deleteMovie(Movie movie) throws IOException {
        idalFacade.deleteMovie(movie);
    }

    @Override
    public void rateMovie(Movie movie, User user, int rating) {
        idalFacade.createRating(new Rating(movie,user,rating));
    }

    @Override
    public User createNewUser(String name) throws IOException {

        return idalFacade.createUser(name);
    }

    @Override
    public User getUserById(int id) {
        return idalFacade.getUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return idalFacade.getAllUsers();
    }
}
