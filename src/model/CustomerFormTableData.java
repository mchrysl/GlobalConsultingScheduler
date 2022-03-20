package model;


/**
 * The CustomerFormTableData data model class, including constructor, getters, and setters.
 * Created for ease of use with the tableView in the customerInformation fxml view/form.
 */
public class CustomerFormTableData {

    private String cCustomerId;
    private String cCustomerName;
    private String cAddress;
    private String cDivision;
    private String cCountry;
    private String cPostalCode;
    private String cPhone;

    public CustomerFormTableData(String cId, String cName, String cAddy, String cDiv, String cCo,
                                 String cPcode, String cPh){
        this.setCCustomerId(cId);
        this.setCCustomerName(cName);
        this.setCAddress(cAddy);
        this.setCDivision(cDiv);
        this.setCCountry(cCo);
        this.setCPostalCode(cPcode);
        this.setCPhone(cPh);
    }

    //setter
    public void setCCustomerId(String cId){
        this.cCustomerId = cId;
    }
    public void setCCustomerName(String cName){
        this.cCustomerName = cName;
    }
    public void setCAddress(String cAddy){
        this.cAddress = cAddy;
    }
    public void setCDivision(String cDiv){
        this.cDivision = cDiv;
    }
    public void setCCountry(String cCo){
        this.cCountry = cCo;
    }
    public void setCPostalCode(String cPcode){
        this.cPostalCode = cPcode;
    }
    public void setCPhone(String cPh){
        this.cPhone = cPh;
    }

    //getters
    public String getCCustomerId(){
        return cCustomerId;
    }
    public String getCCustomerName(){
        return cCustomerName;
    }
    public String getCAddress(){
        return cAddress;
    }
    public String getCDivision(){
        return cDivision;
    }
    public String getCCountry(){
        return cCountry;
    }
    public String getCPostalCode(){
        return cPostalCode;
    }
    public String getCPhone(){
        return cPhone;
    }
}
