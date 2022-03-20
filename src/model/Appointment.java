package model;

import java.time.LocalDateTime;


/**
 * The Appointment data model class, including constructor, getters, and setters.
 */
public class Appointment {

    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastupdatedBy;
    private int customerId;     //foreign key to customers table
    private int userId;         //foreign key to users table
    private int contactId;      //foreign key to contact table


    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy,
                       int customerId, int userId, int contactId) {

        setAppointmentId(appointmentId);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setType(type);
        setStart(start);
        setEnd(end);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setUpdatedBy(lastUpdatedBy);
        setCustomerId(customerId);
        setUserId(userId);
        setContactId(contactId);
    }


    //setters
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description){
            this.description =description;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStart(LocalDateTime start) {
        this.start = start;
    }
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setUpdatedBy(String updatedBy) {
        this.lastupdatedBy = updatedBy;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }


    //getters
    public int getAppointmentId() {
        return appointmentId;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getLocation() { return location; }
    public String getType() {
        return type;
    }
    public LocalDateTime getStart() {
        return start;
    }
    public LocalDateTime getEnd() {
        return end;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
    public String getLastUpdatedBy() { return lastupdatedBy; }
    public int getCustomerId() {
        return customerId;
    }
    public int getUserId() {
        return userId;
    }
    public int getContactId() {
        return contactId;
    }
}
