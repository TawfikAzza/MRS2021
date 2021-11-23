package movierecsys.dal.interfaces;

import movierecsys.be.Movie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IMovieDataAccess {

    List<Movie> getAllMovies() throws FileNotFoundException, IOException;

    Movie createMovie(int releaseYear, String title);

    void deleteMovie(Movie movie) throws IOException;

    void updateMovie(Movie movie);

    Movie getMovie(int id);
}
