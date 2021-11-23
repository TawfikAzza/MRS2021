/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import movierecsys.be.Movie;
import movierecsys.be.Rating;

/**
 *
 * @author pgn
 */
public class MovieRecommender
{
    /**
     * Returns a list of movie recommendations based on the highest total recommendations. Excluding already rated movies from the list of results.
     * @param allRatings List of all users ratings.
     * @param excludeRatings List of Ratings (aka. movies) to exclude.
     * @return Sorted list of movies recommended to the caller. Sorted in descending order.
     */
    public List<Movie> highAverageRecommendations(List<Movie> allMovies,List<Rating> allRatings, List<Rating> excludeRatings)
    {
        //TODO High average recommender
        boolean exist=false;
        List<Movie> resultRating = new ArrayList<>();

      /*  for (Rating ar: allRatings) {
            for (Rating er: excludeRatings) {
                if((ar.getMovie().getTitle().equals(er.getMovie().getTitle()))) {
                    exist=true;
                }
            }
            if(!exist) {
                if(!resultRating.contains(ar.getMovie())) {
                    resultRating.add(ar.getMovie());
                }
            }
            exist=false;
        }*/
        /*List<Rating> testList;
        testList=allRatings.stream()
                .filter(all -> excludeRatings
                                .stream()
                                .distinct()
                                .noneMatch(exc-> all
                                                .getMovie()
                                                .getId()==exc.getMovie()
                                                            .getId()))
                .toList();

        resultRating=testList.stream()
                            .map(x-> x.getMovie())
                            .distinct().toList();
        resultRating.stream().forEach(System.out::println);*/
        //resultRating.sort(Comparator.comparing(Movie::getTitle));
        resultRating = new ArrayList<>();

        List<Rating> userExcludedRatingList;
        System.out.println("All Movies size: "+allMovies.size()+" allRating: "+allRatings.size()+" excluded: "+excludeRatings.size());
        userExcludedRatingList=allRatings.stream()
                .filter(all -> excludeRatings
                        .stream()
                        .distinct()
                        .noneMatch(exc-> all
                                .getMovie()
                                .getId()==exc.getMovie()
                                .getId()))
                .toList();
        HashMap<Integer,Integer> sumScoreMovies = getMovieTotalScore(userExcludedRatingList);
        HashMap<Integer, Integer> countMovieRatings = getMovieTotalRatingCount(userExcludedRatingList);
        List<Movie> listMovies = setMovieRatings(allMovies,sumScoreMovies,countMovieRatings);
        /*System.out.println("TEST:"+userExcludedRatingList.size());
        for (Movie m: listMovies) {
            System.out.printf("IdMovie: %d, released Year: %d, title: %s averageRating: %f%n",m.getId(),m.getYear(),m.getTitle(),m.getAverageRatings());
        }
        System.out.println("List Total size: "+listMovies.size());*/


        return listMovies;
    }
    private List<Movie> setMovieRatings(List<Movie> movieList,HashMap<Integer,Integer> sumScoreMap,HashMap<Integer,Integer> countMap) {
        List<Movie> updatedMovieList = new ArrayList<>();
        for (Movie m: movieList) {//I parse the array of all movies and after the parsing
            //I'll end up with a List which only contains the movies which have "survived the trimming done one the excluded rating list from the
            //user excludedRatings.
            if(sumScoreMap.containsKey(m.getId())) {
                m.setSumOfRatings(sumScoreMap.get(m.getId()));
                m.setNumberOfRatings(countMap.get(m.getId()));
                updatedMovieList.add(m);
            }
        }
        updatedMovieList.sort(Comparator.comparing(Movie::getAverageRatings).reversed());
        //this should send me a list of movies sorted by the average of their rating in descending order
        return updatedMovieList;
    }


    /**
     * Returns a list of movie recommendations based on weighted recommendations. Excluding already rated movies from the list of results. 
     * @param allRatings List of all users ratings.
     * @param excludeRatings List of Ratings (aka. movies) to exclude. 
     * @return Sorted list of movies recommended to the caller. Sorted in descending order.
     */
    public List<Movie> weightedRecommendations(List<Rating> allRatings, List<Rating> excludeRatings)
    {
        List<Movie> resultMovie;
        List<Rating> tmpRatingList;
        List<Movie> weightedRecommendationList=new ArrayList<>();

       /* tmpRatingList=allRatings
                .stream()
                .filter(all -> excludeRatings.stream()
                .distinct()
                .noneMatch(exc -> all.getMovie().getTitle().equals(exc.getMovie().getTitle())))
                .collect(Collectors.toList());

        Comparator<Rating> ratingComparator = (r1,r2) -> r1.getMovie().getTitle().compareTo(r2.getMovie().getTitle());
        tmpRatingList.stream().distinct().sorted(ratingComparator).forEach(x-> System.out.println("Rating comparator: "+x.getRating()));
        tmpRatingList.stream().collect(Collectors.groupingBy(Rating::getMovie)).forEach((x,y)-> {
                            weightedRecommendationList.add(new Movie(x.getId(),x.getYear(),x.getTitle()));
                            System.out.println(x.getTitle()+" "+y
                            .stream()
                            .mapToInt(p-> p.getRating())
                            .summaryStatistics()
                            .getAverage());
                        });
        resultMovie=tmpRatingList.stream()
                                .map(movie -> movie.getMovie())
                                .distinct()
                                .collect(Collectors.toList());

        weightedRecommendationList.forEach(x-> System.out.println("lastTest: "+x));
        resultMovie.forEach(x-> System.out.println("resultMovie: "+x));*/

        return weightedRecommendationList;
    }

    private HashMap<Integer,Double> movieAvgScore(List<Rating> allRatings) {
        Comparator<Rating> comparator = Comparator.comparingInt(r -> r.getMovie().getId());
        HashMap<Integer,Integer> mapMovieScore= new HashMap<>();
        HashMap<Integer,Integer> mapOfRatingNumber = new HashMap<>();
        allRatings.sort(Comparator.comparingInt(r -> r.getMovie().getId()));
        allRatings.forEach(x-> System.out.println(x.getMovie().getId()));
        for (Rating r:allRatings) {
            if(mapMovieScore.containsKey(r.getMovie().getId())) {
                mapMovieScore.put(r.getMovie().getId(),r.getRating()+mapMovieScore.get(r.getMovie().getId()));

            } else {
                mapMovieScore.put(r.getMovie().getId(),r.getRating());

            }
            if(mapOfRatingNumber.containsKey(r.getMovie().getId())) {
                mapOfRatingNumber.put(r.getMovie().getId(),mapOfRatingNumber.get(r.getMovie().getId())+1);
            } else {
                mapOfRatingNumber.put(r.getMovie().getId(),1);
            }
        }

        HashMap<Integer,Double> mapToSend= new HashMap<>();
        for (Map.Entry entry: mapMovieScore.entrySet()) {
            mapToSend.put(Integer.parseInt(entry.getKey().toString()),Double.parseDouble(entry.getValue().toString())/(mapOfRatingNumber.get(entry.getKey())==0?1:mapOfRatingNumber.get(entry.getKey())));
        }
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(mapToSend.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue));
        HashMap<Integer,Double> mapSorted = new HashMap<>();

        return mapToSend;
    }

    private HashMap<Integer,Integer> getMovieTotalScore(List<Rating> allRatings) {
        HashMap<Integer,Integer> mapMovieScore= new HashMap<>();

        //allRatings.sort(Comparator.comparingInt(r -> r.getMovie().getId()));


        for (Rating r:allRatings) {
            if(mapMovieScore.containsKey(r.getMovie().getId())) {
                mapMovieScore.put(r.getMovie().getId(),r.getRating()+mapMovieScore.get(r.getMovie().getId()));

            } else {
                mapMovieScore.put(r.getMovie().getId(),r.getRating());
            }
        }
        return mapMovieScore;
    }
    private HashMap<Integer,Integer> getMovieTotalRatingCount(List<Rating> allRatings) {

        HashMap<Integer,Integer> mapOfRatingNumber = new HashMap<>();
        //allRatings.sort(Comparator.comparingInt(r -> r.getMovie().getId()));

        for (Rating r:allRatings) {
            if(mapOfRatingNumber.containsKey(r.getMovie().getId())) {
                mapOfRatingNumber.put(r.getMovie().getId(),mapOfRatingNumber.get(r.getMovie().getId())+1);
            } else {
                mapOfRatingNumber.put(r.getMovie().getId(),1);
            }
        }
        return mapOfRatingNumber;
    }
}
