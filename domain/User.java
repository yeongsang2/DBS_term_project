package domain;



public class User {

    private String userId;
    private String name;
    private UserType userType;
    private String major;
    private boolean state;

    public User(String userId, String name, UserType userType, String major, boolean state) {
        this.userId = userId;
        this.name = name;
        this.userType = userType;
        this.major = major;
        this.state = state;
    }


    public String getUserId() {
        return userId;
    }


    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getMajor() {
        return major;
    }


    public boolean isState() {
        return state;
    }
}