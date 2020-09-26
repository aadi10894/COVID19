package aadithyabharadwaj.cs.niu.covid19.Models;

public class CountryWiseModel {
    private String country;
    private String confirmed;
    private String newConfirmed;
    private String active;
    private String deceased;
    private String newDeceased;
    private String recovered;
    private String tests;
    private String flag;

    public CountryWiseModel(String country, String confirmed, String newConfirmed, String active, String deceased, String newDeceased,
                            String recovered, String tests, String flag)
    {
        this.country = country;
        this.confirmed = confirmed;
        this.newConfirmed = newConfirmed;
        this.active = active;
        this.deceased = deceased;
        this.newDeceased = newDeceased;
        this.recovered = recovered;
        this.tests = tests;
        this.flag = flag;
    } // End CountryWiseModel

    public String getCountry()
    {
        return country;
    } // End getCountry

    public String getConfirmed()
    {
        return confirmed;
    } // End getConfirmed

    public String getNewConfirmed()
    {
        return newConfirmed;
    } // End getNewConfirmed

    public String getActive()
    {
        return active;
    } // End getActive

    public String getDeceased()
    {
        return deceased;
    } // End getDeceased

    public String getNewDeceased()
    {
        return newDeceased;
    } // End getNewDeceased

    public String getRecovered()
    {
        return recovered;
    } // End getRecovered

    public String getTests()
    {
        return tests;
    } // End getTests

    public String getFlag()
    {
        return flag;
    } // End getFlag
} // End CountryWiseModel
