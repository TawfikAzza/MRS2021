/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.interfaces.IRatingDataAccess;

/**
 *
 * @author pgn
 */
public class RatingDAO implements IRatingDataAccess
{
    /*
    * Rating DAO Class:
    * In order to understand the logic behind the different method composing this class,
    * Please go visit the MovieDAO class, where I tried to explain the way I search, create or update the binary
    * file movie_titles.binary
    *
    * */
    private static final int IDMOVIE_SIZE = Integer.BYTES; //4
    private static final int IDUSER_SIZE = Integer.BYTES; //4
    private static final int RATING_SIZE = Integer.BYTES; //4
    private static final int RECORD_SIZE = IDMOVIE_SIZE + IDUSER_SIZE + RATING_SIZE;
    private static final int EMPTY_ID_RATING=-1;
    //File as well as Path declaration

    private static String RATING_SOURCE= "data/ratings_test.binary";
    private static Path  binaryFileRatingPath =Path.of(RATING_SOURCE);
    private static File file = new File(RATING_SOURCE);

    /**
     * Persists the given rating.
     * @param rating the rating to persist.
     */
    public void createRating(Rating rating)
    {
        /*if(!checkRating(rating)) {
            System.out.println("In RatingDAO error, the rating already exists.");
            return;
        }*/
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")){
            while(raf.getFilePointer()<raf.length()) {
                int idMovie = raf.readInt();
                int idUser = raf.readInt();
                if(idMovie == EMPTY_ID_RATING && idUser == EMPTY_ID_RATING) {
                    raf.seek(-IDUSER_SIZE-IDMOVIE_SIZE);
                    raf.writeInt(rating.getMovie().getId());
                    raf.writeInt(rating.getUser().getId());
                    raf.writeInt(rating.getRating());
                    return;
                } else {
                    raf.skipBytes(RECORD_SIZE-IDMOVIE_SIZE-IDUSER_SIZE);
                }
            }
            raf.seek(raf.length());
            raf.writeInt(rating.getMovie().getId());
            raf.writeInt(rating.getUser().getId());
            raf.writeInt(rating.getRating());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("In RatingDAO, problem with method createRating(Rating rating).");
            e.printStackTrace();
        }
    }
    
    /**
     * Updates the rating to reflect the given object.
     * @param rating The updated rating to persist.
     */
    public void updateRating(Rating rating)
    {
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")){
            while(raf.getFilePointer()<raf.length()) {
                int idMovie = raf.readInt();
                int idUser = raf.readInt();
                if(idMovie==rating.getMovie().getId() && idUser== rating.getUser().getId()) {
                    raf.seek(raf.getFilePointer()-IDMOVIE_SIZE-IDUSER_SIZE);
                    raf.writeInt(rating.getMovie().getId());
                    raf.writeInt(rating.getUser().getId());
                    raf.writeInt(rating.getRating());
                    return;
                } else {
                    raf.skipBytes(RECORD_SIZE-IDMOVIE_SIZE-IDUSER_SIZE);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("In RatingDAO, problem with method updateRating(Rating rating).");
            e.printStackTrace();
        }
    }
    
    /**
     * Removes the given rating.
     * @param rating 
     */
    public void deleteRating(Rating rating)
    {   //For the rating deletion, instead of deletting the rating record and afterward recreating the entire file(32,9Mb!)
        //I replace the values of the entry by -1 for the user ID and the movie ID,
        //next time I create a rating, I'll look into them
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")) {
            while(raf.getFilePointer()<raf.length()) {
                int idMovie = raf.readInt();
                int idUser = raf.readInt();
                if(idMovie == rating.getMovie().getId() && idUser == rating.getUser().getId()) {
                    raf.seek(-IDUSER_SIZE-IDMOVIE_SIZE);
                    raf.writeInt(EMPTY_ID_RATING);
                    raf.writeInt(EMPTY_ID_RATING);
                }
                raf.skipBytes(RECORD_SIZE-IDMOVIE_SIZE-IDUSER_SIZE);
            }
        } catch (IOException e){
            System.out.println("Error in Class RatingDAO with method deleteRating(Rating rating.");
            e.printStackTrace();
        }
    }
    
    /**
     * Gets all ratings from all users.
     * @return List of all ratings.
     */
    public List<Rating> getAllRatings()
    {
        HashMap<Integer,Movie> mapMovie= getMapMovie();
        HashMap<Integer,User> mapUser = getMapUser();
        //I had to to this in order to avoid excessive access to the differents files
        //containing the Movies as well as the Users, if not for each rating I would have had one access to the movie file
        //and one access to the User files, and there is 3.2M entries in the Rating file.....


        List<Rating> allRatings = new ArrayList<>();
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")){
           while(raf.getFilePointer()<raf.length()) {
               int idMovie = raf.readInt();
               int idUser = raf.readInt();
               int valueRating = raf.readInt();
               //System.out.printf("In RatingDAO idMovie: %d, idUser: %d, ValueRating: %d %n",idMovie,idUser,valueRating);
               Rating rating = new Rating(mapMovie.get(idMovie),mapUser.get(idUser),valueRating);
               allRatings.add(rating);
            }
            return allRatings;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("In RatingDAO, problem with method getAllRatings().");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all ratings from a specific user.
     * @param user The user 
     * @return The list of ratings.
     */
    public List<Rating> getRatings(User user)
    {
        HashMap<Integer,Movie> mapMovie= getMapMovie();

        //I had to to this in order to avoid excessive access to the differents files
        //containing the Movies as well as the Users, if not for each rating I would have had one access to the movie file
        //and one access to the User files, and there is 3.2M entries in the Rating file.....

        List<Rating> userRatings = new ArrayList<>();
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")) {
            while(raf.getFilePointer()<raf.length()){
                int idMovie = raf.readInt();
                int idUser = raf.readInt();

                //System.out.println("In rating dao");
                if(idUser==user.getId()) {
                    int valueRating = raf.readInt();
                    //If the right User ID is found in the rating entry, I create a rating to be added in the list
                    //which will be returned.
                    Rating rating = new Rating(mapMovie.get(idMovie),user,valueRating);
                    userRatings.add(rating);
                } else {
                    //if not the right rating, I advance to the next entry.
                    raf.skipBytes(RECORD_SIZE-IDMOVIE_SIZE-IDUSER_SIZE);
                }
            }
            return userRatings;
        }catch(IOException e) {
            System.out.println("Error in class RatinDAO with method getRatings(User user).");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Rating> getMovieRatings(Movie movie) throws FileNotFoundException,IOException {
        List<Rating> ratingMovie = new ArrayList<>();
        HashMap<Integer,User> mapUser = getMapUser();
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")) {
            while(raf.getFilePointer()<raf.length()) {
                int idMovie = raf.readInt();
                if(idMovie== movie.getId()) {
                    int idUser= raf.readInt();
                    int valueRating = raf.readInt();
                    Rating rating = new Rating(movie,mapUser.get(idUser),valueRating);
                    ratingMovie.add(rating);
                } else {
                    raf.skipBytes(RECORD_SIZE-IDMOVIE_SIZE);
                }
            }
            return ratingMovie;
        }
    }

    private HashMap<Integer,Movie> getMapMovie() {
        MovieDAO movieDAO = new MovieDAO();
        List<Movie> allMovies = movieDAO.getAllMovies();
        HashMap<Integer,Movie> mapMovie= new HashMap<>();

        for (Movie m: allMovies) {
            mapMovie.put(m.getId(),m);
        }
        return mapMovie;
    }
    private HashMap<Integer,User> getMapUser(){
        UserDAO userDAO = new UserDAO();
        List<User> allUsers = userDAO.getAllUsers();
        HashMap<Integer,User> mapUser = new HashMap<>();

        for (User u: allUsers) {
            mapUser.put(u.getId(),u);
        }
        return mapUser;
    }
    private boolean checkRating(Rating rating) {//For the management of the position of the pointer of the file,
        //please refer to the MovieDAO class, where I explained the different step I take in the management of this
        //little sh....
        //This method is there to check if a rating already exists in the perstistant datas.
        //for that I check if the ID of the movie associated with the ID of the user already exists.
        //if it exists, the user already rated this movie
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")){
            while(raf.getFilePointer()<raf.length()) {
                int idMovie = raf.readInt();
                int idUser = raf.readInt();
                if(idMovie== rating.getMovie().getId() && idUser == rating.getUser().getId()) {
                    return false;
                } else {
                    raf.seek(RECORD_SIZE-IDMOVIE_SIZE-IDUSER_SIZE);
                }
            }
        } catch (IOException e) {
            System.out.println("Error in RatingDAO in the checkRating(Rating rating) method.");
            e.printStackTrace();
        }

        return true;
    }
    
}
