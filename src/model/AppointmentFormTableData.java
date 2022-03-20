package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The AppointmentFormTableData data model class, including constructor, getters, and setters.
 * Created for ease of use with the tableView in the appointments fxml view/form.
 */
public class AppointmentFormTableData {
    private String aId;
    private String aTitle;
    private String aDescription;
    private String aLocation;
    private String aType;
    private LocalDate aLocalDate;
    private LocalTime aStartTime;
    private String aStartDateTime;
    private LocalTime aEndTime;
    private String aEndDateTime;
    private String aContactName;
    private String aCustomerId;

    public AppointmentFormTableData(String id, String title, String desc, String loc, String type, LocalDate aLDate,
                                    LocalTime startTime, LocalTime endTime, String contactName, String custId) {
        this.setAId(id);
        this.setATitle(title);
        this.setADescription(desc);
        this.setALocation(loc);
        this.setAType(type);
        this.setALocalDate(aLDate);
        this.setAStartTime(startTime);
        this.setAStartDateTime(startTime, aLDate);
        this.setAEndTime(endTime);
        this.setAEndDateTime(endTime, aLDate);
        this.setAContactName(contactName);
        this.setACustomerId(custId);
    }

    //setters
    public void setAId(String Id) {
        this.aId = Id;
    }
    public void setATitle(String title) {
        this.aTitle = title;
    }
    public void setADescription(String Desc) {
        this.aDescription = Desc;
    }
    public void setALocation(String Loc) {
        this.aLocation = Loc;
    }
    public void setAType(String Type) {
        this.aType = Type;
    }
    public void setALocalDate(LocalDate date){
        this.aLocalDate = date;
    }
    public void setAStartTime(LocalTime start) {
        //Need to fuss with this more?
        this.aStartTime = start;
    }
    public void setAStartDateTime(LocalTime start, LocalDate date){
        LocalDateTime startDateTime = LocalDateTime.of(date, start);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy', 'hh:mm a");
        this.aStartDateTime = dtf.format(startDateTime);
    }
    public void setAEndTime(LocalTime end) {
        //Need to fuss with this more?
        this.aEndTime = end;
    }
    public void setAEndDateTime(LocalTime end, LocalDate date){
        LocalDateTime endDateTime = LocalDateTime.of(date, end);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy', 'hh:mm a");
        this.aEndDateTime = dtf.format(endDateTime);
    }
    public void setAContactName(String contactName) {
        this.aContactName = contactName;
    }
    public void setACustomerId(String custId) {
        this.aCustomerId = custId;
    }


    //getters
    public String getAId() {
        return aId;
    }
    public String getATitle() {
        return aTitle;
    }
    public String getADescription() {
        return aDescription;
    }
    public String getALocation() {
        return aLocation;
    }
    public String getAType() { return aType; }
    public LocalDate getaLocalDate(){
        return aLocalDate;
    }
    public LocalDate getALocalDate(){
        return aLocalDate;
    }
    public LocalTime getAStartTime() { return aStartTime; }
    public String getAStartDateTime(){
        return aStartDateTime;
    }
    public LocalTime getAEndTime() { return aEndTime; }
    public String getAEndDateTime(){
        return aEndDateTime;
    }
    public String getAContactName() {
        return aContactName;
    }
    public String getACustomerId() {
        return aCustomerId;
    }

}