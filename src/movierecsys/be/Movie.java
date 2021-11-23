/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.be;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pgn
 */
public class Movie
{

    private final int id;
    private String title;
    private int year;
    private Image imageMovie;
    private String imgString;
    private String PATH_IMAGE = "../../images/";
    private int sumOfRatings;
    private int numberOfRatings;

    public Movie(int id, int year, String title)
    {
        this.id = id;
        this.title = title;
        this.year = year;
    }
    public Movie(int id, int year, String title, String imgString)
    {
        this.id = id;
        this.title = title;
        this.year = year;
        setImageMovieFromString(imgString);
    }

    public double getAverageRatings() {
        return (double) sumOfRatings/(numberOfRatings==0?1:numberOfRatings);
    }
    public int getSumOfRatings() {
        return sumOfRatings;
    }

    public void setSumOfRatings(int sumOfRatings) {
        this.sumOfRatings = sumOfRatings;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public int getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public String getImgString() {
        return imgString;
    }

    @Override
    public String toString() {
        return String.format("%s %d",getTitle(),getYear());
    }
    public Image getImageMovie() {
        return imageMovie;
    }
    public void setImageMovieFromString(String imgString) {
        //System.out.println(getClass().getResource("../../images"));
        this.imgString=imgString;
        imageMovie = new Image(getClass().getResource(PATH_IMAGE + imgString).toString());
    }

}
