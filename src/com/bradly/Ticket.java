package com.bradly;

/**
 * Created by sylentbv on 3/15/2017.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ticket {

    private int priority;

    public String getReporter() {
        return reporter;
    }

    public Date getDateReported() {
        return dateReported;
    }

    private String reporter; //Stores person or department who reported issue
    private String description;
    private Date dateReported;
    private Date dateResolved;
    private String resolution;

    //STATIC Counter - accessible to all Ticket objects.
    //If any Ticket object modifies this counter, all Ticket objects will have the modified value
    //Make it private - only Ticket objects should have access
    private static int staticTicketIDCounter = 1;

    public int getTicketID() {
        return ticketID;
    }

    public static int getStaticTicketIDCounter() {
        return staticTicketIDCounter;
    }

    public static void setStaticTicketIDCounter(int staticTicketIDCounter) {
        Ticket.staticTicketIDCounter = staticTicketIDCounter;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(Date dateResolved) {
        this.dateResolved = dateResolved;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    //The ID for each ticket - instance variable. Each Ticket will have its own ticketID variable
    protected int ticketID;

    public Ticket(String desc, int p, String rep, Date date) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = staticTicketIDCounter;
        staticTicketIDCounter++;
    }

    public Ticket(String desc, int p, String rep, Date date, int id) {
        this.description = desc;
        this.priority = p;
        this.reporter = rep;
        this.dateReported = date;
        this.ticketID = id;
    }

    protected int getPriority() {
        return priority;
    }

    @Override
    public String toString(){
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        return("ID: " + this.ticketID + "; Issue: " + this.description + "; Priority: " +
                this.priority + "; Reported by: " + this.reporter + "; Reported on: " + formatter.format(this.dateReported));
    }
}
