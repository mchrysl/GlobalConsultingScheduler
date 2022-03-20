package model;

import java.util.Date;

public class User {

    private int userId;
    private String userName;
    private String password;
    private Date createDate;
    private String createdBy;
    private Date lastUpdate;
    private String lastUpdatedBy;

    public User(int userId, String userName, String password, Date createDate, String createdBy,
                Date lastUpdate, String lastUpdatedBy){
        setUserId(userId);
        setUserName(userName);
        setPassword(password);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastUpdatedBy(lastUpdatedBy);
    }

    //setters
    public void setUserId(int userId){
        this.userId = userId;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    public void setLastUpdate(Date lastUpdate){
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdatedBy(String lastUpdatedBy){
        this.lastUpdatedBy = lastUpdatedBy;
    }


    //getters
    public int getUserId(){
        return userId;
    }
    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public Date getCreateDate(){
        return createDate;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public Date getLastUpdate(){
        return lastUpdate;
    }
    public String getLastUpdatedBy(){
        return lastUpdatedBy;
    }

}
