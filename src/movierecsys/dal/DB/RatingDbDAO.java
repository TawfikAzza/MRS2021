package movierecsys.dal.DB;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.interfaces.IRatingDataAccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RatingDbDAO implements IRatingDataAccess {

    ConnectionManager cm;

    public RatingDbDAO() throws IOException {
        cm = new ConnectionManager();
    }

    @Override
    public void createRating(Rating rating) {

        try (Connection con = cm.getConnection()) {

            String sqlCommandInsert = "INSERT INTO Rating VALUES(?,?,?)";
            PreparedStatement pstmtInsert = con.prepareStatement(sqlCommandInsert);
            pstmtInsert.setInt(1,rating.getMovie().getId());
            pstmtInsert.setInt(2,rating.getUser().getId());
            pstmtInsert.setInt(3,rating.getRating());
            pstmtInsert.execute();



        } catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateRating(Rating rating) {
        try(Connection con = cm.getConnection()) {
            String sqlCommandUpdate = "UPDATE Rating SET score=? WHERE idMovie=? AND idUser=?";
            PreparedStatement pstmtUpdate = con.prepareStatement(sqlCommandUpdate);
            pstmtUpdate.setInt(1,rating.getRating());
            pstmtUpdate.setInt(2,rating.getMovie().getId());
            pstmtUpdate.setInt(3,rating.getUser().getId());
            pstmtUpdate.executeUpdate();

        }catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteRating(Rating rating) {
        try(Connection con = cm.getConnection()) {
            String sqlCommandDelete = "DELETE FROM Rating WHERE idMovie=? AND idUser=? AND score=?";
            PreparedStatement pstmtUpdate = con.prepareStatement(sqlCommandDelete);
            pstmtUpdate.setInt(1,rating.getMovie().getId());
            pstmtUpdate.setInt(2,rating.getUser().getId());
            pstmtUpdate.setInt(3,rating.getRating());
            pstmtUpdate.execute();

        }catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Rating> getAllRatings() {
        List<Rating> allRatings = new ArrayList();
        try (Connection con = cm.getConnection()) {
            String sqlcommandSelect = "SELECT Rating.score AS score, Movie.id AS idMovie, Movie.[year] AS year, Movie.title AS title, Users.id as idUser, Users.name as userName " +
                                        "FROM Rating,Movie,Users " +
                                        "WHERE Rating.idMovie=Movie.id AND Rating.idUser=Users.id";
            PreparedStatement pstmtSelect = con.prepareStatement(sqlcommandSelect);
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                Movie movie = new Movie(rs.getInt("idMovie"),rs.getInt("year"),rs.getString("title"));
                User user = new User(rs.getInt("idUser"),rs.getString("userName"));
                int score = rs.getInt("score");
                allRatings.add(new Rating(
                        movie,
                        user,
                        score)
                );
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allRatings;
    }

    @Override
    public List<Rating> getRatings(User user) {
        List<Rating> allRatingUser = new ArrayList();
        try (Connection con = cm.getConnection()) {
            String sqlcommandSelect = "SELECT Rating.score AS score, Movie.id AS idMovie, Movie.[year] AS year, Movie.title AS title" +
                    "FROM Rating,Movie,Users " +
                    "WHERE Rating.idMovie=Movie.id AND Rating.idUser=Users.id AND Users.id=?";
            PreparedStatement pstmtSelect = con.prepareStatement(sqlcommandSelect);
            pstmtSelect.setInt(1,user.getId());
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                Movie movie = new Movie(rs.getInt("idMovie"),rs.getInt("year"),rs.getString("title"));

                int score = rs.getInt("score");
                allRatingUser.add(new Rating(
                        movie,
                        user,
                        score)
                );
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allRatingUser;
    }

    @Override
    public List<Rating> getMovieRatings(Movie movie) throws IOException {
        List<Rating> allRatingMovie = new ArrayList();
        try (Connection con = cm.getConnection()) {
            String sqlcommandSelect = "SELECT Rating.score AS score, Users.id as idUser, Users.name as userName " +
                    "FROM Rating,Movie,Users " +
                    "WHERE Rating.idMovie=Movie.id AND Rating.idUser=Users.id AND Movie.id=?";
            PreparedStatement pstmtSelect = con.prepareStatement(sqlcommandSelect);
            pstmtSelect.setInt(1,movie.getId());
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                User user = new User(rs.getInt("idUser"),rs.getString("userName"));
                int score = rs.getInt("score");
                allRatingMovie.add(new Rating(
                        movie,
                        user,
                        score)
                );
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allRatingMovie;
    }
}
