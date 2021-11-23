package movierecsys;

public class InjectionModel {
    private int idMovie;
    private int idUser;
    private int rating;

    public InjectionModel(int idMovie, int idUser, int rating) {
        this.idMovie = idMovie;
        this.idUser = idUser;
        this.rating = rating;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getRating() {
        return rating;
    }
}
