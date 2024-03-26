package src.UserInterface;
/**
 * @author Han Yeeun - s3912055
 */
public class User {
    private String userID;
    private String userPassword;
    private String userName;

    User() {} //기본 생성자
    User(String userID,String userPassword,String userName) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    // Getter & Setter
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
