/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.dal.files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


import javafx.scene.image.Image;
import movierecsys.be.Movie;
import movierecsys.dal.interfaces.IMovieDataAccess;


/**
 * @author pgn
 */
public class MovieDAO implements IMovieDataAccess {

    /*
    * This class has been implemented after having gone through the hurdle of creating the
    * binary version of the movie_titles.txt file,
    * In order to do so, I specified differents size for the data I was going to work with in this project.
    *
    * Looking at the format of the .txt file, I came up with the help of Jeppe's videos as well as a timely
    * insights from Peter with this format:
    *
    * the movie's ID field will be stored on 4 bytes, being an int, it should suffice
    * the Year's field being an int will also be stored in an int so 4 bytes in size
    * The name of the movie will be stored on a length of 200 bytes which should provide enough place for any movie titles
    * The image size(Still not impleented in the program as of yet) which will store the name of the movie image.
    * I reserved the place for it with a length of max 50 bytes (for now the String no_image.png is registered)
    *
    * */
    private static final int ID_SIZE = Integer.BYTES; //4
    private static final int YEAR_SIZE = Integer.BYTES; //4
    private static final int NAME_SIZE = 200;
    private static final int IMAGE_SIZE = 50;
    private static final int RECORD_SIZE = ID_SIZE + YEAR_SIZE + NAME_SIZE + IMAGE_SIZE;
    private static final int EMPTY_ID = -1;
    private static String MOVIE_SOURCE= "data/movie_titles.binary";
    private static Path binaryFilePath =Path.of(MOVIE_SOURCE);
    private File file = new File(MOVIE_SOURCE);

    /**
     * Gets a list of all movies in the persistence storage.
     *
     * @return List of movies.
     * @throws java.io.FileNotFoundException
     */
    public List<Movie> getAllMovies() {
        List<Movie> allMovies = new ArrayList<>();
        //Creating a pointer for the binary file movie_titles.binary
        //this pointer will help me parse the file and get all movies registered in it.
        //In the Movie file, the first Data is an int of 4 bytes length.
        //this int stores the maximum ID a movie has in the file.
        //This way I always know without having to parse all the file which ID I should use when adding a movie
        //SO for all operations, I'll always start by decaling the pointer of the random access file object
        //buy an offset of ID_SIZE in order to not take into account the first data contained in the file.
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")) {
            raf.seek(ID_SIZE);//File pointer offset setted at +4, as I don't need the information contained in it,
            //I do nothing of it
            //While the end of file is not reached.
            while(raf.getFilePointer()<raf.length()) {
                //the first entry is an int according to the specifications I implemented at the creation of the file.
                int id = raf.readInt();

                if(id !=EMPTY_ID) {//If the movie is not a deleted entry, we add it to the List of movies to be returned

                    //the second entry is the release year of the movie
                    int year = raf.readInt();

                    //the third is the movie's title
                    byte[] bytesName = new byte[NAME_SIZE];
                    raf.read(bytesName);
                    String title = new String(bytesName).trim();
                   // System.out.printf("id: %d, year: %d, title: %s %n",id,year,title);
                    //the fourth is the movie's image (no yet implemented, but I reserve the space if
                    //I decide to implement it.
                    byte[] bytesImage = new byte[IMAGE_SIZE];
                    raf.read(bytesImage);
                    String imageName = new String(bytesImage).trim();
                    Movie m = new Movie(id,year,title);
                    allMovies.add(m);
                } else {// if the movie is a deleted entry, we skip the record and continue on to the next entry

                    raf.skipBytes(RECORD_SIZE-ID_SIZE);
                }
            }
            return allMovies;
        } catch (IOException e) {
            System.out.println("In MovieDAO, error in the getAllMovies() method.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads a movie from a , s
     *
     * @param t
     * @return
     * @throws NumberFormatException
     */
    private Movie stringArrayToMovie(String t) {
        String[] arrMovie = t.split(",");

        int id = Integer.parseInt(arrMovie[0]);
        int year = Integer.parseInt(arrMovie[1]);
        String title = arrMovie[2];
        if (arrMovie.length > 3) {
            for (int i = 3; i < arrMovie.length; i++) {
                title += "," + arrMovie[i];
            }
        }
        Movie mov = new Movie(id, year, title);
        return mov;
    }



    /**
     * Creates a movie in the persistence storage.
     *
     * @param releaseYear The release year of the movie
     * @param title       The title of the movie
     * @return The object representation of the movie added to the persistence
     * storage.
     */
    public Movie createMovie(int releaseYear, String title) {
        Movie createdMovie=null;

        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")){
            int previousMaxID=raf.readInt();//we read the first entry of the file (an int)
            //Variable which will store the highest ID in the File,
            //this variable stores the highest ID found in the file,
            //I the go back at the begininning of the file, and update the value by adding 1 to it,
            //which will be the ID of the Movie which is currently added in the file.
            System.out.println("Previous max ID:" + previousMaxID);
            int currentMaxID = previousMaxID+1;
            raf.seek(raf.getFilePointer()-ID_SIZE);
            raf.writeInt(currentMaxID);

            raf.seek(raf.length());
            //now that we have the highest ID contained in the file and updated it at the beginning of it,
            //We set the pointer of the file at the end of the file, in order to add the new movie. with the current highest ID

            int idMovie = currentMaxID;
            raf.writeInt(idMovie);
            raf.writeInt(releaseYear);
            String image="no_image.png";
            raf.writeBytes(String.format("%-" + NAME_SIZE + "s",title).substring(0,NAME_SIZE));
            raf.writeBytes(String.format("%-" + IMAGE_SIZE + "s",image).substring(0,IMAGE_SIZE));
            System.out.printf("id: %d, Year:%d, name: %s added. %n",idMovie,releaseYear,title);
            createdMovie = new Movie(idMovie,releaseYear,title);
        } catch (IOException e) {
            System.out.println("In MovieDAO, error in the createMovie(int releaseYear, String title) method.");
            e.printStackTrace();
        }
        return createdMovie;
    }

    /**
     * Deletes a movie from the persistence storage.
     *
     * @param movie The movie to delete.
     */
    public void deleteMovie(Movie movie) throws IOException {
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")){
            raf.seek(ID_SIZE);//Initializing the pointer at the offset necessary to parse the file content, not its header.
            //Which contain the max ID contained in the file

            while(raf.getFilePointer() < raf.length()) {//We parse the file as long as the pointer return a value
                int id = raf.readInt();//if an id equal to the movie entered is found,
                if (id == movie.getId()) {
                    raf.seek(raf.getFilePointer()-ID_SIZE);//I back the pointer of the file with an offset equivalent
                    //to the length of an int as defined in the static variables.
                    raf.writeInt(EMPTY_ID);//and I write a value for it equivalent to -1 signifying to the other operation
                    //that this entry is invalid (movie has been deleted)
                    //I then end the execution of this method by entering return
                    return;
                } else {//If the Id is not a match, I continue the parsing by advancing the pointer with a value which is
                    //the total value of an entry (RECORD_SIZE) minus the current value of the pointer in the entry (4)
                    raf.skipBytes(RECORD_SIZE-ID_SIZE);//We advance the File pointer toward the next entry ID
                }
            }


        } catch (IOException e) {
            System.out.println("In MovieDAO, error in the delete(Movie movie) method.");
            e.printStackTrace();
        }
    }

    /**
     * Updates the movie in the persistence storage to reflect the values in the
     * given Movie object.
     *
     * @param movie The updated movie.
     */
    public void updateMovie(Movie movie) {
        String image;
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")){
            raf.seek(ID_SIZE);//Still advancing of an offset of 4 in order to jump over the header at the beginning of the file
            while(raf.getFilePointer() < raf.length()) {
                //reading the first data in the entry corresponding to the ID of the movie
                int id = raf.readInt();
                if (id == movie.getId()) {//if the ID of the movie matches the value found in the file we modify it
                    raf.writeInt(movie.getYear());
                    if(movie.getImgString()!=null)
                        image = movie.getImgString();
                    else
                        image="no_image2.png";
                    raf.writeBytes(String.format("%-" + NAME_SIZE + "s",movie.getTitle()).substring(0,NAME_SIZE));
                    raf.writeBytes(String.format("%-" + IMAGE_SIZE + "s",image).substring(0,IMAGE_SIZE));
                    //System.out.printf("id: %d, Year:%d, name: %s updated. %n",movie.getId(),movie.getYear(),movie.getYear());
                    return;
                } else {//if it is not a match, we continue the parsing of the file, by advancing the pointer to the next
                    //data entry.(Offset RECORD_SIZE which is the total length of an entry minus the already parsed part ID_SIZE)
                    raf.skipBytes(RECORD_SIZE-ID_SIZE);
                }
            }
        } catch (IOException e) {
            System.out.println("In MovieDAO, error in the updateMovie(Movie movie) method.");
            e.printStackTrace();
        }
    }

    /**
     * Gets a the movie with the given ID.
     *
     * @param id ID of the movie.
     * @return A Movie object.
     */
    public Movie getMovie(int id) {
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")){
            raf.seek(ID_SIZE);
            while(raf.getFilePointer() < raf.length()) {
                int currentID = raf.readInt();
                if (currentID == id) {//If the ID found in the file is the same as the movie searched,
                    int year = raf.readInt();
                    byte[] bytesName = new byte[NAME_SIZE];
                    raf.read(bytesName);
                    String title = new String(bytesName).trim();
                    byte[] bytesImage = new byte[IMAGE_SIZE];
                    raf.read(bytesImage);
                    String imageName = new String(bytesImage).trim();
                    //I build the Move object with the values found in the file, and return it
                    Movie m = new Movie(id,year,title);
                    return m;
                } else {//if not, we move the pointer to the next entry.
                    raf.skipBytes(RECORD_SIZE-ID_SIZE);
                }
            }
            System.out.println("No movie with this id found in the storage!");

        } catch (IOException e) {
            System.out.println("In MovieDAO, error in the getMovie(int id) method.");
            e.printStackTrace();
        }
        return null;
    }

}
