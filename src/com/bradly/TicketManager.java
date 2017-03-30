package com.bradly;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by sylentbv on 3/29/2017.
 */
public class TicketManager extends  JFrame{
    private JPanel rootPanel;
    private JButton enterNewTicketButton;
    private JButton searchTicketsButton;
    private JButton displayAllTicketsButton;
    private JButton exitButton;
    private LinkedList<Ticket> ticketQueue;
    private LinkedList<Ticket> resolvedTickets;

    public TicketManager(LinkedList<Ticket> ticketQueueIn) {
        super("Ticket Manager");
        ticketQueue=ticketQueueIn;
        resolvedTickets=new LinkedList<>();
        setContentPane(rootPanel);
        pack();
        // set to do nothing so the Exit button must be used!
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        buttonConfig();
        getRootPane().setDefaultButton(enterNewTicketButton);
        setSize(new Dimension(300, 300));
        setVisible(true);
    }

    private void buttonConfig(){
        enterNewTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //call enter ticket form
                EnterNewTicket newTicket = new EnterNewTicket(ticketQueue);
            }
        });
        searchTicketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //add search ticket form
                SearchByIssue searchGUI = new SearchByIssue(ticketQueue,resolvedTickets);
            }
        });
        displayAllTicketsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //display list with both items
                AllTickets allTicketsGUI = new AllTickets(ticketQueue,resolvedTickets);
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quit = JOptionPane.showConfirmDialog(TicketManager.this,"Are you sure you want to exit?",
                        "Quit",JOptionPane.OK_CANCEL_OPTION);
                if(quit==JOptionPane.OK_OPTION) {
                    close(ticketQueue,resolvedTickets);
                    System.exit(0);
                }
            }
        });
    }

    protected void close(LinkedList<Ticket> ticketQueue,LinkedList<Ticket> resolvedTickets){
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
            JOptionPane.showMessageDialog(TicketManager.this,
                    "An error was encountered writing open tickets./n"+ex.getMessage());
            //System.out.println("An error was encountered writing open tickets.");
            //System.out.println(ex.getMessage());
        }

        try(BufferedWriter filewriter = new BufferedWriter(new FileWriter(resolvedFilename,true))){
            for(Ticket t : resolvedTickets){
                filewriter.write(t+"; Resolved On: " +
                        dateFormat.format(date) + "; Resolution: " + t.getResolution()+"\n");
            }

            filewriter.close();
        }
        catch (IOException ex){
            JOptionPane.showMessageDialog(TicketManager.this,
                    "An error was encountered writing resolved tickets./n"+ex.getMessage());
            //System.out.println("An error was encountered writing resolved tickets.");
            //System.out.println(ex.getMessage());
        }
    }
}
