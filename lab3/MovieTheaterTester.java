import java.io.*;
import java.util.*;


class Movie{
    private String title, genre;
    private int year;
    private double avgRating;

    public Movie(String title, String genre, int year, double avgRating){
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.avgRating = avgRating;
    }

    public String title(){
        return title;
    }

    public String genre(){
        return genre;
    }

    public int year(){
        return year;
    }

    public double avgRating(){
        return avgRating;
    }

    @Override
    public String toString(){
        return title + ", " + genre + ", " + year + ", " + String.format("%.2f", avgRating);
    }
}

class SortByRating implements Comparator<Movie>{
    public int compare(Movie a, Movie b){
        return Double.compare(b.avgRating(), a.avgRating());
    }
}

class SortByYear implements Comparator<Movie>{
    public int compare(Movie a, Movie b){
        return Integer.compare(a.year(), b.year());
    }
}

class SortByGenre implements Comparator<Movie>{
    public int compare(Movie a, Movie b){
        return a.genre().compareTo(b.genre());
    }
}

class SortByTitle implements Comparator<Movie>{
    public int compare(Movie a, Movie b){
        return a.title().compareTo(b.title());
    }
}

class MovieTheater{
    private ArrayList<Movie> movies;

    public MovieTheater(){
        movies = new ArrayList<Movie>();
    }

    public void readMovies(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try{
            int n = Integer.parseInt(br.readLine());
            for(int i = 0; i < n;i++){
                String title = br.readLine();
                String genre = br.readLine();
                int year = Integer.valueOf(br.readLine());
                String[] ratings = br.readLine().split(" ");
                double avgRating = 0;
                for(String s : ratings){
                    double rating = Double.valueOf(s);
                    avgRating += rating;
                }
                avgRating /= ratings.length;
                movies.add(new Movie(title, genre, year, avgRating));
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void printByGenreAndTitle(){
        movies.sort(new SortByTitle());
        movies.sort(new SortByGenre());
        System.out.print(this.toString());
    }

    public void printByYearAndTitle(){
        movies.sort(new SortByTitle());
        movies.sort(new SortByYear());
        System.out.print(this.toString());
    }

    public void printByRatingAndTitle(){
        movies.sort(new SortByTitle());
        movies.sort(new SortByRating());
        System.out.print(this.toString());
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Movie m : movies){
            sb.append(m + "\n");
        }
        return sb.toString();
    }
}

public class MovieTheaterTester {
    public static void main(String[] args) {
        MovieTheater mt = new MovieTheater();
        mt.readMovies(System.in);
        System.out.println("SORTING BY RATING");
        mt.printByRatingAndTitle();
        System.out.println("\nSORTING BY GENRE");
        mt.printByGenreAndTitle();
        System.out.println("\nSORTING BY YEAR");
        mt.printByYearAndTitle();
    }
}
