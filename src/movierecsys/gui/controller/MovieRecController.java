/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import movierecsys.be.Movie;
import movierecsys.be.User;
import movierecsys.gui.model.MovieModel;

/**
 *
 * @author pgn
 */
public class MovieRecController implements Initializable
{

    /**
    * Variables used for the User's data on the form
     *
    * */

    @FXML
    private TextField txtIdUser;

    @FXML
    private TextField txtUserName;

    @FXML
    private ListView<User> lstUser;

    /**
     * Variable used fir the Movie's data in the form
     *
     * */

    @FXML
    private TextField txtIdMovie;

    @FXML
    private TextField txtNameMovie;

    @FXML
    private TextField txtYearMovie;

    /**
     * The TextField containing the URL of the targeted website.
     */
    @FXML
    private TextField txtMovieSearch;

    /**
     * The TextField containing the query word.
     */
    @FXML
    private ListView<Movie> lstMovies;


    private final MovieModel movieModel;

    public MovieRecController() throws IOException {
        movieModel = new MovieModel();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try {
            lstMovies.setItems(movieModel.getAllMovies());
            lstUser.setItems(movieModel.getAllUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * User related functionalities
     *
     * */
    public void searchUserById(ActionEvent actionEvent) {
        try {
            User user = movieModel.searchUserById(Integer.parseInt(txtIdUser.getText()));
            txtUserName.setText(user.getName());
        } catch (NullPointerException e) {
            showAndLogError(e);
            e.printStackTrace();
        }
    }

    /**
     * Movie related functionlaities
     *
     * */

    public void searchMovie(KeyEvent keyEvent) {
        try {
            lstMovies.setItems(movieModel.searchMovie(txtMovieSearch.getText()));
            System.out.println("txt: "+txtMovieSearch.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createMovie(ActionEvent actionEvent) {
        movieModel.createMovie(Integer.parseInt(txtYearMovie.getText()),txtNameMovie.getText());
    }

    public void modifyMovie(ActionEvent actionEvent) {
        movieModel.modifyMovie(new Movie(Integer.parseInt(txtIdMovie.getText()),Integer.parseInt(txtYearMovie.getText()),txtNameMovie.getText()));
    }




    /**
     * Section gathering al the utility methods
     * Like methods which update the fields contained in the form
     * these method are self explanatory and dont need any comments
     * */
    public void updateMovieFieldsByKey(KeyEvent keyEvent) {
        updateMovieFields();
    }


    public void updateMovieFieldsByMouse(MouseEvent mouseEvent) {
        updateMovieFields();
    }

    public void updateUserFieldsByMouse(MouseEvent mouseEvent) {
        updateUserFields();
    }

    public void updateUserFieldsByKey(KeyEvent keyEvent) {
        updateUserFields();
    }

    private void updateMovieFields() {
        Movie movieSelected = lstMovies.getSelectionModel().getSelectedItem();
        txtIdMovie.setText(String.valueOf(movieSelected.getId()));
        txtYearMovie.setText(String.valueOf(movieSelected.getYear()));
        txtNameMovie.setText(movieSelected.getTitle());
    }
    private void updateUserFields(){
        User userSelected = lstUser.getSelectionModel().getSelectedItem();
        txtIdUser.setText(String.valueOf(userSelected.getId()));
        txtUserName.setText(userSelected.getName());
    }

    private static void showAndLogError(NullPointerException ex) {
        Logger.getLogger(MovieRecController.class.getName()).log(Level.SEVERE, null, ex);

        Alert alert = new Alert(Alert.AlertType.ERROR,
                ex.getMessage()
                        + String.format("%n")
                        + "See error log for technical details."
        );
        alert.showAndWait();
    }

}
//Rubbish code
    /*
    public void displayMovie(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/movierecsys/gui/view/MovieDisplayView.fxml"));
        Parent root = loader.load();
        MovieDisplayViewController controller = loader.getController();
        System.out.println(controller);
        ListView lst = (ListView) mouseEvent.getSource();
        Movie movieSelected = (Movie)lst.getSelectionModel().getSelectedItem();
        System.out.println(movieSelected.getTitle());
        controller.setMovie(movieSelected);
        controller.displayMovie();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
*/