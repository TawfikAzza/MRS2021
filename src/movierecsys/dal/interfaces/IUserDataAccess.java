package movierecsys.dal.interfaces;

import movierecsys.be.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IUserDataAccess {
    List<User> getAllUsers();

    User getUser(int id);

    void updateUser(User user) throws IOException;

    public User createUser(String name) throws FileNotFoundException, IOException;


}
