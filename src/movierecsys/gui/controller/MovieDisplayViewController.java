package movierecsys.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import movierecsys.be.Movie;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieDisplayViewController {
    @FXML
    private ImageView imgViewMovie;
    @FXML
    private Label lblMovieTitle;


    private Movie movie;

    public void setMovie(Movie movie) {
        this.movie=movie;

    }
    public void displayMovie() {

        lblMovieTitle.setText(movie.getTitle());
        imgViewMovie.setImage(movie.getImageMovie());
    }
}
