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
import java.util.List;
import movierecsys.be.User;
import movierecsys.dal.interfaces.IUserDataAccess;

/**
 *
 * @author pgn
 */
public class UserDAO implements IUserDataAccess
{
    private static final int ID_USER_SIZE = Integer.BYTES; //4
    private static final int NAME_USER_SIZE = 50;
    private static final int RECORD_SIZE = ID_USER_SIZE + NAME_USER_SIZE;
    private static final int EMPTY_ID = -1;
    private static String USER_SOURCE= "data/users.binary";
    private static Path binaryFileUserPath =Path.of(USER_SOURCE);
    private static File file = new File(USER_SOURCE);
    /**
     * Gets a list of all known users.
     * @return List of users.
     */
    public List<User> getAllUsers()
    {
        List<User> allUsers = new ArrayList<>();
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")){
            raf.seek(ID_USER_SIZE);//Advance the pointer through the first entry of the file which contain the Max USER ID
            //contained in the file.
            while(raf.getFilePointer()<raf.length()){
                int idUser = raf.readInt();
                if(idUser!= EMPTY_ID) {//If user is not a deleted user thus ID != from -1,
                    byte[] bytesName = new byte[NAME_USER_SIZE];
                    raf.read(bytesName);
                    String nameUser = new String(bytesName).trim();
                    User user = new User(idUser,nameUser);
                    allUsers.add(user);
                } else {//If the user is a deleted one, we go to the next entry if it exists.
                    raf.skipBytes(RECORD_SIZE-ID_USER_SIZE);
                }
            }
            return allUsers;
        } catch (IOException e) {
            System.out.println("Error in class UserDAO with method getAllUsers().");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Gets a single User by its ID.
     * @param id The ID of the user.
     * @return The User with the ID.
     */
    public User getUser(int id)
    {
        try(RandomAccessFile raf = new RandomAccessFile(file,"r")){
            raf.seek(ID_USER_SIZE);
            while(raf.getFilePointer()<raf.length()) {
                int idUser = raf.readInt();
                if(idUser==id) {
                    byte[] bytesName = new byte[NAME_USER_SIZE];
                    raf.read(bytesName);
                    String nameUser = new String(bytesName).trim();
                    User userFound = new User(id,nameUser);
                    return userFound;
                } else {
                    raf.skipBytes(RECORD_SIZE-ID_USER_SIZE);
                }
            }
        } catch (IOException e){
            System.out.println("Error in class UserDAO with method getUser(int id)");
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Updates a user so the persistence storage reflects the given User object.
     * @param user The updated user.
     */
    public void updateUser(User user) throws IOException {
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")){
            raf.seek(ID_USER_SIZE);//Place the offset after the max ID user data.
            while(raf.getFilePointer()<raf.length()) {
                int idUser = raf.readInt();
                if(idUser==user.getId()) {//If user is found
                    raf.writeBytes(String.format("%-" + NAME_USER_SIZE + "s",user.getName()).substring(0,NAME_USER_SIZE));
                    return;
                } else {
                    raf.skipBytes(RECORD_SIZE-ID_USER_SIZE);
                    //go to the next entry in the file.
                }
            }
            System.out.println("No users with this id found!");
        }
    }

    public User createUser(String name) throws FileNotFoundException, IOException {
        try(RandomAccessFile raf = new RandomAccessFile(file,"rw")) {
            int maxIdUser= raf.readInt();
            maxIdUser = maxIdUser+1;
            raf.seek(-ID_USER_SIZE);
            raf.writeInt(maxIdUser);
            raf.seek(raf.length());
            raf.writeInt(maxIdUser);
            raf.writeBytes(String.format("%-" + NAME_USER_SIZE + "s",name).substring(0,NAME_USER_SIZE));
            User user = new User(maxIdUser,name);
            return user;
        }
    }
    
}
