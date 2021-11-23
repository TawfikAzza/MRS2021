package movierecsys.gui.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import movierecsys.be.Movie;
import movierecsys.be.User;
import movierecsys.bll.MovieManager;
import movierecsys.bll.OwsLogicFacade;

import java.io.IOException;
import java.util.List;

public class MovieModel {

    private ObservableList<Movie> allMovies;
    private ObservableList<User> allUsers;
    private OwsLogicFacade logicFacade;
    public MovieModel() throws IOException {
        this.logicFacade= new MovieManager();
        allMovies = FXCollections.observableArrayList();
        allUsers = FXCollections.observableArrayList();
//        allMovies.add(new Movie(1,2021,"Dune","dune2021.jpg"));
//        allMovies.add(new Movie(2,1984,"Dune the original","dune1984.jpg"));
    }
    public ObservableList<Movie> getAllMovies() throws IOException {
        List<Movie> allMovieList = logicFacade.getAllTimeTopRatedMovies();
        allMovies.addAll(allMovieList);
        return allMovies;
    }

    public ObservableList<User> getAllUsers() {
        List<User> allUserList = logicFacade.getAllUsers();
        allUsers.addAll(allUserList);
        return allUsers;
    }

    public User searchUserById(int idUser) {
        User user = logicFacade.getUserById(idUser);
        return user;
    }

    public ObservableList<Movie> searchMovie(String text) throws IOException {
        ObservableList<Movie> movieList = FXCollections.observableArrayList();
        movieList.addAll(logicFacade.searchMovies(text));
        System.out.println("Sizemovielist: "+movieList.size());
        return movieList;
    }

    public void createMovie(int year, String title) {
        logicFacade.createMovie(year,title);
    }

    public void modifyMovie(Movie selectedMovie) {
        logicFacade.updateMovie(selectedMovie);
    }
}
