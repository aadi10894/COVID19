package aadithyabharadwaj.cs.niu.covid19.Models;

public class StateWiseModel
{
    private String state;
    private String confirmed;
    private String confirmed_new;
    private String active;
    private String death;
    private String death_new;
    private String recovered;
    private String recovered_new;
    private String lastupdate;

    public StateWiseModel(String state, String confirmed, String confirmed_new, String active,
                          String death, String death_new, String recovered, String recovered_new, String lastupdate)
    {
        this.state = state;
        this.confirmed = confirmed;
        this.confirmed_new = confirmed_new;
        this.active = active;
        this.death = death;
        this.death_new = death_new;
        this.recovered = recovered;
        this.recovered_new = recovered_new;
        this.lastupdate = lastupdate;
    } // End StateWiseModel

    public String getState()
    {
        return state;
    } // End getState

    public String getConfirmed()
    {
        return confirmed;
    } // End getConfirmed

    public String getConfirmed_new()
    {
        return confirmed_new;
    } // End getConfirmed_new

    public String getActive()
    {
        return active;
    } // End getActive

    public String getDeath()
    {
        return death;
    } // End getDeath

    public String getDeath_new()
    {
        return death_new;
    } // End getDeath_new

    public String getRecovered()
    {
        return recovered;
    } // End getRecovered

    public String getRecovered_new()
    {
        return recovered_new;
    } // End getRecovered_new

    public String getLastupdate()
    {
        return lastupdate;
    } // End getLastupdate
} // End StateWiseModel Class
