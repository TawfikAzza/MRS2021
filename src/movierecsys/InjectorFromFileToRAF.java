package movierecsys;

import movierecsys.be.Movie;
import movierecsys.be.Rating;
import movierecsys.be.User;
import movierecsys.dal.DB.ConnectionManager;
import movierecsys.dal.DB.UserDbDAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InjectorFromFileToRAF {
    private static final int ID_SIZE = Integer.BYTES; //4
    private static final int YEAR_SIZE = Integer.BYTES; //4
    private static final int NAME_SIZE = 200;
    private static final int IMAGE_SIZE = 50;
    private static final int RECORD_SIZE = ID_SIZE + YEAR_SIZE + NAME_SIZE;
    private static final int EMPTY_ID = -1;

    private static final int ID_USER_SIZE = Integer.BYTES; //4
    private static final int NAME_USER_SIZE = 50;
    //Injection part of the Movies
    private static String fileStringPath= "data/movie_titles.txt";
    private static Path filePath =Path.of(fileStringPath);

    private static String binaryFileStringPath= "data/movie_titles.binary";
    private static Path  binaryFilePath =Path.of(binaryFileStringPath);
    //End injection part of the movie

    //Injection part of the Ratings
    private static String fileStringRatingPath= "data/ratings.txt";
    private static Path fileRatingPath =Path.of(fileStringRatingPath);

    private static String binaryFileRatingStringPath= "data/ratings.binary";
    private static Path  binaryFileRatingPath =Path.of(binaryFileRatingStringPath);
    //End injection part of the Ratings
    private static String fileStringUserPath= "data/users.txt";
    private static Path fileUserPath =Path.of(fileStringUserPath);

    private static String binaryFileUserStringPath= "data/users.binary";
    private static Path  binaryFileUserPath =Path.of(binaryFileUserStringPath);
    //Injection Part of the Users


    public static void main(String[] args) throws IOException {

      //  List<Movie> allMovies = new ArrayList<>();

        /*allMovies = getAllMovies();
        addAllMoviesDB(allMovies);*/
        /*
        System.out.println("Size="+allMovies.size());
        addAllMovies(allMovies);*/

       /* List<InjectionModel> allRatings = new ArrayList<>();
        System.out.println("Retrieving Datas from files....");
        allRatings= getAllRatings();
        System.out.println("Retieving Finished, starting database Injection...");
        addAllRatingsDB(allRatings);*/
        /*System.out.println(("Size allRatings: "+allRatings.size()));
        addAllRating(allRatings);*/
        //List<User> userTest = getAllUsers();
        //addAllUsersDB(userTest);
        /*addAllUsers(userTest);*/

    }

    public static List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try {
            List<String> allLines = Files.readAllLines(fileUserPath);
            for (String str: allLines) {
                String[] strResult = str.split(",");
                //System.out.println(strResult[0]+strResult[1]+strResult[2]);
                int idUser= Integer.parseInt(strResult[0]);
                String nameUser = strResult[1];

                User u = new User(idUser,nameUser);
                allUsers.add(u);
            }
            return allUsers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ConnectionManager connectDB() throws IOException {
        ConnectionManager cm = new ConnectionManager();
        return cm;
    }
    public static void addAllUsersDB(List<User> listUser) throws IOException {
        ConnectionManager cm = connectDB();
        try(Connection con = cm.getConnection()) {
            String sqlCommandInsert = "INSERT INTO Users VALUES(?,?)";
            PreparedStatement pstmtInsert = con.prepareStatement(sqlCommandInsert);
            for (User u: listUser) {
                pstmtInsert.setInt(1,u.getId());
                pstmtInsert.setString(2,u.getName());
                pstmtInsert.execute();
                System.out.printf("UserId: %d, Name: %s injected.%n",u.getId(),u.getName());
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void addAllMoviesDB(List<Movie> listMovie) throws IOException {
        ConnectionManager cm = connectDB();
        try(Connection con = cm.getConnection()) {
            String sqlCommandInsert = "INSERT INTO Movie VALUES(?,?,?)";
            PreparedStatement pstmtInsert = con.prepareStatement(sqlCommandInsert);
            for (Movie m: listMovie) {
                pstmtInsert.setInt(1,m.getId());
                pstmtInsert.setInt(2,m.getYear());
                pstmtInsert.setString(3,m.getTitle());
                pstmtInsert.execute();
                System.out.printf("MovieID: %d, Year: %d, Title: %s Injected Successfully. %n",m.getId(),m.getYear(),m.getTitle());
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void addAllRatingsDB(List<InjectionModel> listInjection) throws IOException {
        ConnectionManager cm = connectDB();
        try(Connection con = cm.getConnection()) {
            String sqlCommandInsert = "INSERT INTO Rating VALUES(?,?,?)";
            PreparedStatement pstmtInsert = con.prepareStatement(sqlCommandInsert);
            for (InjectionModel inj: listInjection) {
                pstmtInsert.setInt(1,inj.getIdMovie());
                pstmtInsert.setInt(2,inj.getIdUser());
                pstmtInsert.setInt(3,inj.getRating());
                pstmtInsert.execute();
            }
            System.out.println("Injection finished total objects injected="+listInjection.size());
        } catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void addAllUsers(List<User> listUser) {
        File file = new File(binaryFileUserStringPath);
        int maxUserID = 0;
        for (User user: listUser) {
            if(user.getId()>maxUserID) {
                maxUserID=user.getId();
            }
        }
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")){
            raf.writeInt(maxUserID);//The first data injected is the max ID of the user in the file for later operation use.
            //if at one point in time, we are asked to add a new user.
            for (User user: listUser) {
                raf.writeInt(user.getId());
                raf.writeBytes(String.format("%-" + NAME_USER_SIZE + "s",user.getName()).substring(0,NAME_USER_SIZE));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<Movie> getAllMovies() {
        List<Movie> allMovies = new ArrayList<>();
        try {
            List<String> allLines = Files.readAllLines(filePath);
            for (String str: allLines) {
                String[] strResult = str.split(",");
                //System.out.println(strResult[0]+strResult[1]+strResult[2]);
                int id= Integer.parseInt(strResult[0]);
                int year = !Objects.equals(strResult[1], "NULL") ?Integer.parseInt(strResult[1]):1900;
                String title = strResult[2];
                Movie m = new Movie(id,year,title);
                allMovies.add(m);
            }
            return allMovies;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void addAllMovies(List<Movie> allMovies) {
        File file = new File(binaryFileStringPath);
        int lastID = 0;
        for (Movie movie: allMovies) {
            if(movie.getId()>lastID) {
                lastID = movie.getId();
            }
        }


        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")) {
            raf.writeInt(lastID);
            for (Movie m: allMovies) {

                raf.writeInt(m.getId());
                raf.writeInt(m.getYear());
                String name = m.getTitle();
                String image="no_image.png";
                raf.writeBytes(String.format("%-" + NAME_SIZE + "s",name).substring(0,NAME_SIZE));

                raf.writeBytes(String.format("%-" + IMAGE_SIZE + "s",image).substring(0,IMAGE_SIZE));
                System.out.printf("id: %d, Year:%d, name: %s added. %n",m.getId(),m.getYear(),m.getTitle());

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<InjectionModel> getAllRatings() {
        List<InjectionModel> allRatings= new ArrayList<>();
        try {
            List<String> allLines = Files.readAllLines(fileRatingPath);
            for (String str: allLines) {
                String[] strResult = str.split(",");
                //System.out.println(strResult[0]+strResult[1]+strResult[2]);
                int idMovie= Integer.parseInt(strResult[0]);
                int idUser = Integer.parseInt(strResult[1]);
                int rating= Integer.parseInt(strResult[2]);
                //Rating r = new Rating(id,year,title);
                allRatings.add(new InjectionModel(idMovie,idUser,rating));
            }
            return allRatings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addAllRating(List<InjectionModel> allRatings) {
        File file = new File(binaryFileRatingStringPath);
        int count=0;
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")) {

            for (InjectionModel inj: allRatings) {
                raf.writeInt(inj.getIdMovie());
                raf.writeInt(inj.getIdUser());
                raf.writeInt(inj.getRating());
                count++;
                System.out.printf("id movie: %d, idUser: %d, Rating: %d injected successfully %n",inj.getIdMovie(),inj.getIdUser(),inj.getRating());

            }
            System.out.println("Total Rows injected: "+count);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
