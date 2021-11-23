package movierecsys.dal.DB;

import movierecsys.be.User;
import movierecsys.dal.interfaces.IUserDataAccess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDbDAO implements IUserDataAccess {
    ConnectionManager cm;
    public UserDbDAO() throws IOException {
        cm = new ConnectionManager();
    }
    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList();
        try (Connection con = cm.getConnection()) {
            String sqlcommandSelect = "SELECT * FROM Users";
            PreparedStatement pstmtSelect = con.prepareStatement(sqlcommandSelect);
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                allUsers.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"))
                );
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allUsers;
    }

    @Override
    public User getUser(int id) {
        User user=null;
        try (Connection con = cm.getConnection()) {
            String sqlcommandSelect = "SELECT * FROM Users WHERE id=?";
            PreparedStatement pstmtSelect = con.prepareStatement(sqlcommandSelect);
            pstmtSelect.setInt(1,id);
            ResultSet rs = pstmtSelect.executeQuery();
            while(rs.next())
            {
                user = new User(
                    rs.getInt("id"),
                    rs.getString("name")
                );
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    @Override
    public void updateUser(User user) throws IOException {
        try(Connection con = cm.getConnection()) {
            String sqlCommandUpdate = "UPDATE Users SET name=? WHERE id=?";
            PreparedStatement pstmtUpdate = con.prepareStatement(sqlCommandUpdate);
            pstmtUpdate.setString(1,user.getName());
            pstmtUpdate.setInt(2,user.getId());
            pstmtUpdate.executeUpdate();

        }catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public User createUser(String name) throws FileNotFoundException, IOException {
        User createdUser = null;
        try (Connection con = cm.getConnection()) {

            String sqlCommandInsert = "INSERT INTO Users VALUES(?)";
            PreparedStatement pstmtInsert = con.prepareStatement(sqlCommandInsert, Statement.RETURN_GENERATED_KEYS);
            pstmtInsert.setString(1,name);
            pstmtInsert.execute();
            ResultSet rs = pstmtInsert.getGeneratedKeys();

            while(rs.next()) {
                createdUser = new User(rs.getInt(1),name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDbDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return createdUser;
    }
}
