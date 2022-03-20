package model;


/**
 * The CustomersPerCountyData data model class, including constructor, getters, and setters.
 * Created for ease of use with the reportCustomersPerCountry fxml view/form.
 */
public class CustomersPerCountryData {

        private String cpcCustomerId;
        private String cpcCountryId;
        private String cpcCountry;


        public CustomersPerCountryData(String cId, String cCoId, String cCo){
            this.setCpcCustomerId(cId);
            this.setCpcCountryId(cCoId);
            this.setCpcCountry(cCo);

        }

        //setter
        public void setCpcCustomerId(String cId){
            this.cpcCustomerId = cId;
        }
        public void setCpcCountryId(String cCoId){
        this.cpcCountryId = cCoId;
    }
        public void setCpcCountry(String cCo){
            this.cpcCountry = cCo;
        }


        //getters
        public String getCpcCustomerId(){
            return cpcCustomerId;
        }
        public String getCpcCountryId(){
        return cpcCountryId;
    }
        public String getCpcCountry(){
            return cpcCountry;
        }

    }
