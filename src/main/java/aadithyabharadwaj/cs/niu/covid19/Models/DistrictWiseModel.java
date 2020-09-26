package aadithyabharadwaj.cs.niu.covid19.Models;

public class DistrictWiseModel
{
    private String district;
    private String confirmed;
    private String active;
    private String recovered;
    private String deceased;
    private String newConfirmed;
    private String newRecovered;
    private String newDeceased;

    public DistrictWiseModel(String district, String confirmed, String active, String recovered,
                             String deceased, String newConfirmed, String newRecovered, String newDeceased)
    {
        this.district = district;
        this.confirmed = confirmed;
        this.active = active;
        this.recovered = recovered;
        this.deceased = deceased;
        this.newConfirmed = newConfirmed;
        this.newRecovered = newRecovered;
        this.newDeceased = newDeceased;
    } // End DistrictWiseModel

    public String getDistrict()
    {
        return district;
    } // End getDistrict

    public String getConfirmed()
    {
        return confirmed;
    } // End getConfirmed

    public String getActive()
    {
        return active;
    } // End getActive

    public String getRecovered()
    {
        return recovered;
    } // End getRecovered

    public String getDeceased()
    {
        return deceased;
    } // End getDeceased

    public String getNewConfirmed()
    {
        return newConfirmed;
    } // End getNewConfirmed

    public String getNewRecovered()
    {
        return newRecovered;
    } // End getNewRecovered

    public String getNewDeceased()
    {
        return newDeceased;
    } // End getNewDeceased
} // End DistrictWiseModel Class
