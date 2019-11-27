package msp.device.backup;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class Customer {
    private Integer CUSTOMER_IDENTIFIER;
    private Timestamp CREATION_DATE;
    private Timestamp LAST_MODIFICATION_DATE;
    private String CREATION_USER;
    private String MODIFICATION_USER;

    public Integer getCUSTOMER_IDENTIFIER() {
        return CUSTOMER_IDENTIFIER;
    }

    public void setCUSTOMER_IDENTIFIER(Integer CUSTOMER_IDENTIFIER) {
        this.CUSTOMER_IDENTIFIER = CUSTOMER_IDENTIFIER;
    }

    public Timestamp getCREATION_DATE() {
        return CREATION_DATE;
    }

    public void setCREATION_DATE(Timestamp CREATION_DATE) {
        this.CREATION_DATE = CREATION_DATE;
    }

    public Timestamp getLAST_MODIFICATION_DATE() {
        return LAST_MODIFICATION_DATE;
    }

    public void setLAST_MODIFICATION_DATE(Timestamp LAST_MODIFICATION_DATE) {
        this.LAST_MODIFICATION_DATE = LAST_MODIFICATION_DATE;
    }

    public String getCREATION_USER() {
        return CREATION_USER;
    }

    public void setCREATION_USER(String CREATION_USER) {
        this.CREATION_USER = CREATION_USER;
    }

    public String getMODIFICATION_USER() {
        return MODIFICATION_USER;
    }

    public void setMODIFICATION_USER(String MODIFICATION_USER) {
        this.MODIFICATION_USER = MODIFICATION_USER;
    }

    public Customer(Integer CUSTOMER_IDENTIFIER, Timestamp CREATION_DATE, Timestamp LAST_MODIFICATION_DATE, String CREATION_USER, String MODIFICATION_USER) {
        this.CUSTOMER_IDENTIFIER = CUSTOMER_IDENTIFIER;
        this.CREATION_DATE = CREATION_DATE;
        this.LAST_MODIFICATION_DATE = LAST_MODIFICATION_DATE;
        this.CREATION_USER = CREATION_USER;
        this.MODIFICATION_USER = MODIFICATION_USER;
    }

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer{" +
                "CUSTOMER_IDENTIFIER=" + CUSTOMER_IDENTIFIER +
                ", CREATION_DATE=" + CREATION_DATE +
                ", LAST_MODIFICATION_DATE=" + LAST_MODIFICATION_DATE +
                ", CREATION_USER='" + CREATION_USER + '\'' +
                ", MODIFICATION_USER='" + MODIFICATION_USER + '\'' +
                '}';
    }

}
