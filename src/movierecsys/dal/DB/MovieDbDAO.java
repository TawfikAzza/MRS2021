package movierecsys.dal.DB;

import movierecsys.be.Movie;
import movierecsys.be.User;
import movierecsys.dal.interfaces.IMovieDataAccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieDbDAO implements IMovieDataAccess {
    ConnectionManager cm;
    public MovieDbDAO() throws IOException {
        cm = new ConnectionManager();
    }

    @Override
    public List<Movie> getAllMovies() throws FileNotFoundException, IOException {
        List<Movie> allMovies = new ArrayList();
        try (Connection con = cm.getConnection()) {
            String sqlcommandSelect = "SELECT * FROM Movie";
            PreparedStatement pstmtSelect = con.prepareStatement(sqlcommandSelect);
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                allMovies.add(new Movie(
                        rs.getInt("id"),
                        rs.getInt("year"),
                        rs.getString("title"))
                );
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allMovies;
    }

    @Override
    public Movie createMovie(int releaseYear, String title) {
        Movie createdMovie = null;
        try (Connection con = cm.getConnection()) {

            String sqlCommandInsert = "INSERT INTO Movie VALUES(?,?)";
            PreparedStatement pstmtInsert = con.prepareStatement(sqlCommandInsert, Statement.RETURN_GENERATED_KEYS);
            pstmtInsert.setInt(1,releaseYear);
            pstmtInsert.setString(2,title);
            pstmtInsert.execute();
            ResultSet rs = pstmtInsert.getGeneratedKeys();

            while(rs.next()) {
                createdMovie = new Movie(rs.getInt(1),releaseYear,title);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createdMovie;
    }

    @Override
    public void deleteMovie(Movie movie) throws IOException {
        try(Connection con = cm.getConnection()) {
            String sqlCommandDelete = "DELETE FROM Movie WHERE id=?";
            PreparedStatement pstmtUpdate = con.prepareStatement(sqlCommandDelete);
            pstmtUpdate.setInt(1,movie.getId());
            pstmtUpdate.execute();

        }catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateMovie(Movie movie) {
        try(Connection con = cm.getConnection()) {
            String sqlCommandUpdate = "UPDATE Movie SET title=?,year=? WHERE id=?";
            PreparedStatement pstmtUpdate = con.prepareStatement(sqlCommandUpdate);
            pstmtUpdate.setString(1,movie.getTitle());
            pstmtUpdate.setInt(2,movie.getYear());
            pstmtUpdate.setInt(3,movie.getId());
            pstmtUpdate.executeUpdate();

        }catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Movie getMovie(int id) {
        Movie movie=null;
        try (Connection con = cm.getConnection()) {
            String sqlcommandSelect = "SELECT * FROM Movie WHERE id=?";
            PreparedStatement pstmtSelect = con.prepareStatement(sqlcommandSelect);
            pstmtSelect.setInt(1,id);
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                movie = new Movie(
                        rs.getInt("id"),
                        rs.getInt("year"),
                        rs.getString("title")
                );
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movie;
    }
}
