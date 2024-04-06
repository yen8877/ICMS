package src.UserInterface;
/*
 * @author Han Yeeun - s3912055
 */
public class User {
    private String userID;
    private String userPassword;
    private String userName;

    User(String userID, String userPassword, String userName) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    public String getUserID() {
        return this.userID;
    }


    public String getUserPassword() {
        return this.userPassword;
    }


    public String getUserName() {
        return this.userName;
    }

}
