package autotest.dto.custData;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@Generated("com.robohorse.robopojogenerator")
public class ClientDataItem {

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("docSNInf")
    private String docSNInf;

    @SerializedName("docSNChd")
    private String docSNChd;

    @SerializedName("docExpDateChd")
    private String docExpDateChd;

    @SerializedName("docSN")
    private String docSN;

    @SerializedName("sex")
    private String sex;

    @SerializedName("docExpDateInf")
    private String docExpDateInf;

    @SerializedName("birthDateInf")
    private String birthDateInf;

    @SerializedName("firstNameInf")
    private String firstNameInf;

    @SerializedName("birthDateChd")
    private String birthDateChd;

    @SerializedName("birthDate")
    private String birthDate;

    @SerializedName("firstNameChd")
    private String firstNameChd;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("docExpDate")
    private String docExpDate;

    @SerializedName("sexChd")
    private String sexChd;

    @SerializedName("sexInf")
    private String sexInf;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setDocSNInf(String docSNInf) {
        this.docSNInf = docSNInf;
    }

    public String getDocSNInf() {
        return docSNInf;
    }

    public void setDocSNChd(String docSNChd) {
        this.docSNChd = docSNChd;
    }

    public String getDocSNChd() {
        return docSNChd;
    }

    public void setDocExpDateChd(String docExpDateChd) {
        this.docExpDateChd = docExpDateChd;
    }

    public String getDocExpDateChd() {
        return docExpDateChd;
    }

    public void setDocSN(String docSN) {
        this.docSN = docSN;
    }

    public String getDocSN() {
        return docSN;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setDocExpDateInf(String docExpDateInf) {
        this.docExpDateInf = docExpDateInf;
    }

    public String getDocExpDateInf() {
        return docExpDateInf;
    }

    public void setBirthDateInf(String birthDateInf) {
        this.birthDateInf = birthDateInf;
    }

    public String getBirthDateInf() {
        return birthDateInf;
    }

    public void setFirstNameInf(String firstNameInf) {
        this.firstNameInf = firstNameInf;
    }

    public String getFirstNameInf() {
        return firstNameInf;
    }

    public void setBirthDateChd(String birthDateChd) {
        this.birthDateChd = birthDateChd;
    }

    public String getBirthDateChd() {
        return birthDateChd;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setFirstNameChd(String firstNameChd) {
        this.firstNameChd = firstNameChd;
    }

    public String getFirstNameChd() {
        return firstNameChd;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setDocExpDate(String docExpDate) {
        this.docExpDate = docExpDate;
    }

    public String getDocExpDate() {
        return docExpDate;
    }

    public void setSexChd(String sexChd) {
        this.sexChd = sexChd;
    }

    public String getSexChd() {
        return sexChd;
    }

    public void setSexInf(String sexInf) {
        this.sexInf = sexInf;
    }

    public String getSexInf() {
        return sexInf;
    }

    @Override
    public String toString() {
        return
                "ClientDataItem{" +
                        "lastName = '" + lastName + '\'' +
                        ",docSNInf = '" + docSNInf + '\'' +
                        ",docSNChd = '" + docSNChd + '\'' +
                        ",docExpDateChd = '" + docExpDateChd + '\'' +
                        ",docSN = '" + docSN + '\'' +
                        ",sex = '" + sex + '\'' +
                        ",docExpDateInf = '" + docExpDateInf + '\'' +
                        ",birthDateInf = '" + birthDateInf + '\'' +
                        ",firstNameInf = '" + firstNameInf + '\'' +
                        ",birthDateChd = '" + birthDateChd + '\'' +
                        ",birthDate = '" + birthDate + '\'' +
                        ",firstNameChd = '" + firstNameChd + '\'' +
                        ",firstName = '" + firstName + '\'' +
                        ",docExpDate = '" + docExpDate + '\'' +
                        ",sexChd = '" + sexChd + '\'' +
                        ",sexInf = '" + sexInf + '\'' +
                        "}";
    }

    public ClientDataItem() {
        updateClientData();
    }

    @JsonIgnore
    public void updateClientData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        this.setDocExpDate(LocalDate.now().plusMonths(18).format(formatter).toString());
        this.setDocExpDateInf(LocalDate.now().plusMonths(12).format(formatter).toString());
        this.setBirthDateInf(LocalDate.now().minusMonths(9).format(formatter).toString());
        this.setBirthDateChd(LocalDate.now().minusMonths(48).format(formatter).toString());
    }


}