/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author c0611751
 */
@ApplicationScoped
@ManagedBean
public class UserController {
    private List<User> users;
    private static UserController instance;
    
    /**
     * 
     * @return list?
     */

    public List<User> getUsers() {
        return users;
    }
    /**
     * 
     * @return instance
     */
    public static UserController getInstance(){
        return instance;
    }
    /**
     * 
     * @param id use it to fetch username
     * @return 
     */
public String getUsernameById(int id){
    for (User u : users){
        if(u.getId() == id){
            return u.getUsername();
        }
    }
    return null;
}
/**
 * 
 * @param username use it to fetch id
 * @return 
 */
public int getUserIdByUsername(String username){
     for (User u : users){
         if(u.getUsername().equals(username)){
             return u.getId();
         }
     }
     return -1;
}
/**
 * 
 */
    public UserController(){
       
        updateUsersFromDatabase();
        instance = this.instance;
    }
    /**
     * 
     */
    private void updateUsersFromDatabase() {
        try {
            
            //make a conection
            Connection conn = Utils.getConnection();
          
       users = new ArrayList<>();
        
          //build a query
       
        
        Statement stmt = conn.createStatement();
         String sql = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(sql);
        //parse the results
        while (rs.next()){
        User u = new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("passhash")
        );
                users.add(u);
    }
           
    }
      catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            users = new ArrayList();
        }
}}
