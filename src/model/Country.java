package model;

import java.time.LocalDateTime;

/**
 * The Country data model class, including constructor, getters, and setters.
 */
public class Country {

    private int countryId;
    private String name;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    public Country(int cId, String n, LocalDateTime cd, String cb,
                   LocalDateTime lu, String lub) {
        setCountryId(cId);
        setName(n);
        setCreateDate(cd);
        setCreatedBy(cb);
        setLastUpdate(lu);
        setLastUpdatedBy(lub);
    }

    //setters
    public void setCountryId(int cId) {
        this.countryId = cId;
    }
    public void setName(String n){
        this.name = n;
    }
    public void setCreateDate(LocalDateTime cd){
        this.createDate = cd;
    }
    public void setCreatedBy(String cb){
        this.createdBy = cb;
    }
    public void setLastUpdate(LocalDateTime lu){
        this.lastUpdate = lu;
    }
    public void setLastUpdatedBy(String lub){ this.lastUpdatedBy = lub; }

    //getters
    public int getCountryId() {
        return countryId;
    }
    public String getName(){
        return name;
    }
    public LocalDateTime getCreateDate(){
        return createDate;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
}
