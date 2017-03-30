package com.bradly;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        LinkedList<Ticket> ticketQueue = new LinkedList<>();
        //Scanner scan = new Scanner(System.in);
        //LinkedList<Ticket> resolvedTickets = new LinkedList<>();

        open(ticketQueue);

        TicketManager ticketManagerGUI = new TicketManager(ticketQueue);

        /*
        while(true){
            System.out.println("1. Enter Ticket\n" +
                    "2. Search Ticket by Issue\n" +
                    "3. Delete Ticket by Issue\n" +
                    "4. Delete Ticket by ID\n" +
                    "6. Display Open Tickets\n" +
                    "7. Display Resolved Tickets\n" +
                    "8. Display All Tickets\n" +
                    "9. Quit");
            int task = Input.getPositiveIntInput(null);//Integer.parseInt(scan.nextLine());

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue,resolvedTickets);

            } else if (task == 2) {
                //delete a ticket
                searchTicketByIssue(ticketQueue);

            } else if (task == 3) {
                //delete a ticket
                deleteTicketByIssue(ticketQueue,resolvedTickets);

            } else if (task == 4) {
                //delete a ticket
                deleteTicketByID(ticketQueue,resolvedTickets);

            } else if ( task == 6 ) {
                //Print list of tickets
                printOpenTickets(ticketQueue);

            } else if ( task == 7 ) {
                //Print list of tickets
                printResolvedTickets(resolvedTickets);
            } else if ( task == 8 ) {
                //Print list of tickets
                printAllTickets(ticketQueue,resolvedTickets);

            } else if ( task == 9 ) {
                //Quit. Future prototype may want to save all tickets to a file
                close(ticketQueue,resolvedTickets);
                System.out.println("Quitting program");
                break;
            }
            else {
                //this will happen for option 3 or any other selection that is a valid int
                System.out.println("Invalid selection.");
            }
        }
        scan.close();
        */
    }

    protected static void open(LinkedList<Ticket> ticketQueue){
        String openFilename;
        openFilename="open_tickets.txt";

        try(BufferedReader bufReader = new BufferedReader(new FileReader(openFilename))){
            String line = bufReader.readLine();
            while (line != null) {
                //Split string into each section
                String[] sInfo = line.split(";");
                String fid, fissue, fpriority,freportedby,freportedon;
                int id, priority;
                Date reportedon;

                if(sInfo.length>1) {
                    //get first numeric value, removing all alpha characters
                    fid = sInfo[0].replaceAll("[a-zA-Z :]", "");
                    //convert to int
                    id=Integer.parseInt(fid);
                    //get total value after label
                    fissue=sInfo[1].substring(8);
                    //get first numeric value, removing all alpha characters
                    fpriority=sInfo[2].replaceAll("[a-zA-Z :]", "");
                    //convert to int
                    priority=Integer.parseInt(fpriority);
                    //get total value after label
                    freportedby=sInfo[3].substring(14);
                    //get total value after label
                    freportedon=sInfo[4].substring(14);
                    //convert to date
                    try {
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                        reportedon = formatter.parse(freportedon);
                    }
                    catch(ParseException ex){
                        reportedon=new Date();
                    }

                    //add ticket
                    Ticket t = new Ticket(fissue, priority, freportedby, reportedon,id);
                    addTicketInPriorityOrder(ticketQueue, t);
                }
                else
                {
                    //read last ticket id, and set static ticket number to that value
                    fid=sInfo[0].replaceAll("[a-zA-Z :]", "");
                    //convert to int
                    id=Integer.parseInt(fid);
                    Ticket.setStaticTicketIDCounter(id);
                }

                // read in the next line ...
                line = bufReader.readLine();
            }
            bufReader.close();
        }
        catch (IOException ex){
            System.out.println("An error was encountered reading existing open tickets.");
            System.out.println(ex.getMessage());
        }
    }
/*
    protected static void close(LinkedList<Ticket> ticketQueue,LinkedList<Ticket> resolvedTickets){
        String openFilename,resolvedFilename;
        openFilename="open_tickets.txt";
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        resolvedFilename="Resolved_Tickets_for_"+dateFormat.format(date)+".txt";

        try(BufferedWriter filewriter = new BufferedWriter(new FileWriter(openFilename))){
            for(Ticket t : ticketQueue){
                filewriter.write(t.toString()+"\n");
            }
            filewriter.write("NextTicketID:"+Ticket.getStaticTicketIDCounter());

            filewriter.close();
        }
        catch (IOException ex){
            System.out.println("An error was encountered writing open tickets.");
            System.out.println(ex.getMessage());
        }

        try(BufferedWriter filewriter = new BufferedWriter(new FileWriter(resolvedFilename,true))){
            for(Ticket t : resolvedTickets){
                filewriter.write(t+"; Resolved On: " +
                        dateFormat.format(date) + "; Resolution: " + t.getResolution()+"\n");
            }

            filewriter.close();
        }
        catch (IOException ex){
            System.out.println("An error was encountered writing resolved tickets.");
            System.out.println(ex.getMessage());
        }
    }


    protected static void searchTicketByIssue(LinkedList<Ticket> ticketQueue){
        String issueSearch;
        issueSearch=Input.getStringInput("Enter Issue term to search for:");

        for(Ticket t : ticketQueue){
            if(t.getDescription().toUpperCase().contains(issueSearch.toUpperCase())){
                System.out.println(t);
            }
        }
    }

    protected static void deleteTicketByIssue(LinkedList<Ticket> ticketQueue,LinkedList<Ticket> resolvedTickets) {

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        searchTicketByIssue(ticketQueue);   //display list for user

        deleteTicket(ticketQueue,resolvedTickets);

    }

    protected static void deleteTicketByID(LinkedList<Ticket> ticketQueue,LinkedList<Ticket> resolvedTickets) {

        if (ticketQueue.size() == 0) {    //no tickets!
            System.out.println("No tickets to delete!\n");
            return;
        }

        printAllTickets(ticketQueue,resolvedTickets);   //display list for user

        deleteTicket(ticketQueue,resolvedTickets);

    }

    protected static void deleteTicket(LinkedList<Ticket> ticketQueue,LinkedList<Ticket> resolvedTickets) {

        boolean found = false;
        while(!found) {
            System.out.println("Enter ID of ticket to delete");
            int deleteID = Input.getPositiveIntInput(null);

            //Loop over all tickets. Delete the one with this ticket ID

            for (Ticket ticket : ticketQueue) {
                if (ticket.getTicketID() == deleteID) {
                    found = true;
                    String resolution;
                    resolution=Input.getStringInput("Enter a resolution:");
                    ticket.setResolution(resolution);
                    ticket.setDateResolved(new Date());
                    resolvedTickets.add(ticket);
                    ticketQueue.remove(ticket);
                    System.out.println(String.format("Ticket %d deleted", deleteID));
                    break; //don't need loop any more.
                }
            }
            if (found == false) {
                System.out.println("Ticket ID not found, no ticket deleted.");
                System.out.println("Please try again.");
            }
        }
        printAllTickets(ticketQueue,resolvedTickets);  //print updated list

    }

    protected static void addTickets(LinkedList<Ticket> ticketQueue,LinkedList<Ticket> resolvedTickets) {
        Scanner sc = new Scanner(System.in);
        boolean moreProblems = true;
        String description, reporter;
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems){
            System.out.println("Enter problem");
            description = sc.nextLine();
            System.out.println("Who reported this issue?");
            reporter = sc.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(sc.nextLine());

            Ticket t = new Ticket(description, priority, reporter, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);

            printAllTickets(ticketQueue,resolvedTickets);

            System.out.println("More tickets to add?");
            String more = sc.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }
*/
    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket){

        //Logic: assume the list is either empty or sorted

        if (tickets.size() == 0 ) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size() ; x++) {    //use a regular for loop so we know which element we are looking at
            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }

        //Will only get here if the ticket is not added in the loop
        //If that happens, it must be lower priority than all other tickets. So, add to the end.
        tickets.addLast(newTicket);
    }
/*
    protected static void printAllTickets(LinkedList<Ticket> tickets,LinkedList<Ticket> resolvedTickets) {
        printOpenTickets(tickets);
        printResolvedTickets(resolvedTickets);
    }

    protected static void printOpenTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All open tickets ----------");

        for (Ticket t : tickets ) {
            System.out.println(t); //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");
    }
    protected static void printResolvedTickets(LinkedList<Ticket> tickets) {
        System.out.println(" ------- All resolved tickets ----------");
        //display ticket info, adding resolution date and description
        for (Ticket t : tickets ) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = t.getDateResolved();
            System.out.println(t+"; Resolved On: " +
                    dateFormat.format(date) + "; Resolution: " + t.getResolution());
            //Write a toString method in Ticket class
            //println will try to call toString on its argument
        }
        System.out.println(" ------- End of ticket list ----------");
    }
*/
}
