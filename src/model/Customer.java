package model;

import java.time.LocalDateTime;


/**
 * The Customer data model class, including constructor, getters, and setters.
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionId;

    public Customer(int customerId, String customerName, String address, String postalCode, String phone,
                    LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy,
                    int divisionId){
        setCustomerId(customerId);
        setCustomerName(customerName);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setCreateDate(createDate);
        setCreatedBy(createdBy);
        setLastUpdate(lastUpdate);
        setLastUpdatedBy(lastUpdatedBy);
        setDivisionId(divisionId);

    }

    //setters
    public void setCustomerId(int customerId){
        this.customerId = customerId;
    }
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setCreateDate(LocalDateTime createDate){
        this.createDate = createDate;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }
    public void setLastUpdate(LocalDateTime lastUpdate){
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdatedBy(String lastUpdatedBy){
        this.lastUpdatedBy = lastUpdatedBy;
    }
    public void setDivisionId(int divisionId) { this.divisionId = divisionId; }

    //getters
    public int getCustomerId(){
        return customerId;
    }
    public String getCustomerName(){
        return customerName;
    }
    public String getCustomerAddress(){
        return address;
    }
    public String getPostalCode(){
        return postalCode;
    }
    public String getPhone(){
        return phone;
    }
    public LocalDateTime getCreateDate(){
        return createDate;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public LocalDateTime getLastUpdate(){
        return lastUpdate;
    }
    public String getLastUpdatedBy(){
        return lastUpdatedBy;
    }
    public int getDivisionId() { return divisionId; }
}
