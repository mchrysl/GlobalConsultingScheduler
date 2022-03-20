package model;

/**
 * The ContactsAppointment data model class, including constructor, getters, and setters.
 * Created for ease of use with the reportContactsSchedules fxml view/form.
 */
public class ContactsAppointment {

    String caContactId;
    String caAppointmentId;
    String caTitle;
    String caType;
    String caDescription;
    String caStart;
    String caEnd;
    String caCustomerId;

    public ContactsAppointment(String contctId, String apptId, String ti, String ty, String desc, String apptStart, String apptEnd, String custId){
        setCaContactId(contctId);
        setCaAppointmentId(apptId);
        setCaTitle(ti);
        setCaDescription(desc);
        setCaType(ty);
        setCaStart(apptStart);
        setCaEnd(apptEnd);
        setCaCustomerId(custId);

    }

    //setters
    public void setCaContactId(String contctId){ this.caContactId = contctId; }
    public void setCaAppointmentId(String aId) {
        this.caAppointmentId = aId;
    }
    public void setCaTitle(String ti) {
        this.caTitle = ti;
    }
    public void setCaDescription(String desc){
        this.caDescription = desc;
    }
    public void setCaType(String ty) { this.caType = ty; }
    public void setCaStart(String apptStart) { this.caStart = apptStart; }
    public void setCaEnd(String apptEnd) { this.caEnd = apptEnd; }
    public void setCaCustomerId(String custId) { this.caCustomerId = custId; }


    //getters
    public String getCaContactId() { return caContactId; }
    public String getCaAppointmentId() {
        return caAppointmentId;
    }
    public String getCaTitle() {
        return caTitle;
    }
    public String getCaDescription(){
        return caDescription;
    }
    public String getCaType() {
        return caType;
    }
    public String getCaStart() {
        return caStart;
    }
    public String getCaEnd() {
        return caEnd;
    }
    public String getCaCustomerId() {
        return caCustomerId;
    }

}

