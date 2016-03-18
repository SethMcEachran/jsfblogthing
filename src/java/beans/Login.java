/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author c0611751
 */
@ManagedBean
@SessionScoped
public class Login {
    private String username;
    private String password;
    private boolean loggedin;
    private User currentUser;

    public Login() {
        username = null;
        password = null;
        loggedin = false;
        currentUser = null;
    }

    
    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String Login(){
        String passhash = Utils.hash(password);
        for (User u : new UserController().getUsers()){
        if(username.equals(u.getUsername()) && passhash.equals(u.getPasshash())){
            loggedin =true;
            currentUser = u;
            return "index";
       
        }
           
        
    } 
        currentUser = null;
        loggedin = false;
        return "index";
    }
}
