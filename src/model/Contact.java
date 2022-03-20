package model;


/**
 * The Contact data model class, including constructor, getters, and setters.
 */
public class Contact {

    private int contactId;
    private String contactName;
    private String contactEmail;

    public Contact(int contactId, String contactName, String contactEmail){
        setContactId(contactId);
        setContactName(contactName);
        setContactEmail(contactEmail);
    }

    //setters
    public void setContactId(int contactId){
        this.contactId = contactId;
    }
    public void setContactName(String contactName){
        this.contactName = contactName;
    }
    public void setContactEmail(String contactEmail){
        this.contactEmail = contactEmail;
    }

    //getters
    public int getContactId(){
        return contactId;
    }
    public String getContactName(){
        return contactName;
    }
    public String getContactEmail(){
        return contactEmail;
    }
}
