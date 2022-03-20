package model;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * The UpcomingAppointment data model class, including constructor, getters, and setters.
 * Created for ease of use when finding any appointments within 15 minutes of login for an Alert dialog.
 */
public class UpcomingAppointment {

    String uaAppointmentId;
    LocalTime uaStartTime;
    LocalDate uaStartDate;

    public UpcomingAppointment(String uaId, LocalDate uaSD, LocalTime uaST){
        setUaAppointmentId(uaId);
        setUaStartTime(uaST);
        setUaStartDate(uaSD);
    }

    //setters
    private void setUaAppointmentId(String uaId) {
        this.uaAppointmentId = uaId;
    }
    private void setUaStartTime(LocalTime uaST) {
        this.uaStartTime = uaST;
    }
    private void setUaStartDate(LocalDate uaSD) {
        this.uaStartDate = uaSD;
    }

    //getters
    public String getUaAppointmentId() {
        return uaAppointmentId;
    }
    public LocalTime getUaStartTime() {
        return uaStartTime;
    }
    public LocalDate getUaStartDate() {
        return uaStartDate;
    }
}
