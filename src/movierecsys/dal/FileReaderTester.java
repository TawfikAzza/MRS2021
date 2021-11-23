/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.bll.MovieManager;
import movierecsys.dal.DB.UserDbDAO;
import movierecsys.dal.files.MovieDAO;
import movierecsys.dal.files.RatingDAO;
import movierecsys.dal.files.UserDAO;

/**
 *
 * @author pgn
 */
public class FileReaderTester
{

    /**
     * Example method. This is the code I used to create the users.txt files.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
       // printMovies();
        //createMovie();
        //updateMovie();
        //deleteMovie();
        //getMovie();
       // createRating();
       // getAllUsers();
        //getUser();
       // updateUser();
        //getAllRatings();
        //getUserRating();
       // MovieManager movieManager = new MovieManager();
       // List<Movie> listMovie= movieManager.getAllTimeTopRatedMovies();
        //getAllUserDb();
        //getUserDb(1);
        //updateUserDb();
        createUserDb("Trine Mortensen");
    }
    public static void getAllUserDb() throws IOException {
        UserDbDAO userDbDAO = new UserDbDAO();
        List<User> userList = userDbDAO.getAllUsers();
        for (User u:userList) {
            System.out.println("userId: "+u.getId()+" UserName: "+u.getName());
        }
    }
    public static void getUserDb(int id) throws IOException {
        UserDbDAO userDbDAO = new UserDbDAO();
        User user = userDbDAO.getUser(id);
        System.out.println("userId: "+user.getId()+" UserName: "+user.getName());
    }
    public static void updateUserDb() throws IOException {
        UserDbDAO userDbDAO = new UserDbDAO();
        User user = new User(1,"Tawfik Azza");
        userDbDAO.updateUser(user);
    }
    public static void createUserDb(String name) throws IOException {
        UserDbDAO userDbDAO = new UserDbDAO();
        User user = userDbDAO.createUser(name);
        System.out.println("userId: "+user.getId()+" UserName: "+user.getName());

    }
    public static void createRating() {
        RatingDAO ratingDAO = new RatingDAO();
        Movie movie = new Movie(5,1999,"The Matrix");
        User user1 = new User(1900514,"Tawfik");
        Rating rating1 = new Rating(movie,user1,3);
        User user2 = new User(1900514,"Janet");
        Rating rating2 = new Rating(movie,user2,3);
        User user3 = new User(1900514,"Actarus");
        Rating rating3 = new Rating(movie,user3,3);
        ratingDAO.createRating(rating1);
        ratingDAO.createRating(rating2);
        ratingDAO.createRating(rating3);
        System.out.println("Rating created.");
    }
    public static void getAllRatings() {
        RatingDAO ratingDAO = new RatingDAO();
        List<Rating> allRatings = ratingDAO.getAllRatings();
        HashMap<Integer,String> mapMovie = new HashMap<>();
        for (Rating r: allRatings) {
            if(!mapMovie.containsKey(r.getMovie().getId())) {
                mapMovie.put(r.getMovie().getId(),r.getMovie().getTitle());
            }
        }
        System.out.println("Size Movie Rating:"+mapMovie.size());
       // System.out.println(allRatings);
    }
    public static void getUserRating() {
        RatingDAO ratingDAO = new RatingDAO();
        List<Rating> ratings = new ArrayList<>();
        User user = new User(1900514,"Susanna Fujisaki");
        ratings = ratingDAO.getRatings(user);
        ratings.stream().forEach(x-> System.out.println(x));
    }
    public static void getAllUsers() {
        UserDAO userDAO = new UserDAO();
        List<User> allUsers = new ArrayList<>();
        allUsers = userDAO.getAllUsers();
        for (User u: allUsers) {
            System.out.println(u);
        }
        System.out.println("Total Users: "+allUsers.size());
    }
    public static void getUser() {
        UserDAO userDAO = new UserDAO();
        User user =  userDAO.getUser(2645431);
        System.out.println(user);
    }
    public static void updateUser() throws IOException {
        UserDAO userDAO = new UserDAO();
        User user = new User(2645431,"Susanna Fujisaki");
        //User user = new User(2645431,"Susanna Banana");
        userDAO.updateUser(user);

    }
   public static void printMovies() throws IOException {
       MovieDAO movieDao = new MovieDAO();
       List<Movie> allMovs = movieDao.getAllMovies();
        for (Movie allMov : allMovs)
        {
            System.out.printf("ID: %d, Year: %d, Title: %s%n",allMov.getId(),allMov.getYear(),allMov.getTitle());
        }
        System.out.println("Movie count: " + allMovs.size());
        System.out.println();
        System.out.println("------------------------------- Method createMovie(Movie movie) Test ------------------------------------------");
        System.out.println();
   }
   public static void createMovie() {
       MovieDAO movieDao = new MovieDAO();
       System.out.println("Creating a movie if it already exists in the persistence storage," +
               "the movie is not added, if it doesn't exists, the movie is added.");
       //movieDao.createMovie(2021,"The grand Tour LochDown");
       movieDao.createMovie(1984,"Dune The original");
       //allMovs = movieDao.getAllMovies();
       System.out.println("Operation executed.");
       // System.out.println("Movie count: " + allMovs.size());
       System.out.println();
       System.out.println("------------------------------- End Method createMovie(movie movie) Test --------------------------------------");
       System.out.println();
   }
   public static void deleteMovie() throws IOException {
       MovieDAO movieDao = new MovieDAO();
       System.out.println();
       System.out.println("------------------------------- Method deleteMovie(Movie movie) Test ------------------------------------------");
       System.out.println();
       Movie movie = new Movie(17772,2021,"Dune serie");
       movieDao.deleteMovie(movie);
       //allMovs = movieDao.getAllMovies();
       System.out.println("Operation executed.");
       //System.out.println("Movie count: " + allMovs.size());
       System.out.println();
       System.out.println("------------------------------- End Method deleteMovie(movie movie) Test --------------------------------------");
       System.out.println();
   }

   public static void updateMovie() throws IOException {
       MovieDAO movieDao = new MovieDAO();

       Movie movie = new Movie(17772,2021,"Dune 2021 the Gender awake one");

       //System.out.println(movieDao.getAllMovies());
       movieDao.updateMovie(movie);
       System.out.println("Operation executed.");
       //System.out.println("Movie count: " + movieDao.getAllMovies().size());
       System.out.println();
       System.out.println("------------------------------- End Method updateMovie(movie movie) Test --------------------------------------");
       System.out.println();
       /* System.out.println();
        System.out.println("-------------------------------   Method getMovie(int ID) Test  --------------------------------------");
        System.out.println();
        Movie mov = movieDao.getMovie(1);
        System.out.println("Movie found: "+mov);*/
   }

   public static void getMovie() {
       MovieDAO movieDao = new MovieDAO();
        Movie movie = movieDao.getMovie(17759);
       System.out.printf("Movie fetched: %s",movie);
   }
}
