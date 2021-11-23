/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movierecsys.bll.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import movierecsys.be.Movie;

/**
 *
 * @author pgn
 */
public class MovieSearcher
{
    public List<Movie> search(List<Movie> searchBase, String query)
    {

        List<Movie> resultSearch = new ArrayList<>();

        for (Movie m:searchBase) {
            if(m.getTitle().replaceAll(" ","").toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                resultSearch.add(m);
            }
        }

        return resultSearch;
    }
    
}
