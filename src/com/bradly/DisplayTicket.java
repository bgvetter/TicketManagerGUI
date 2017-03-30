package com.bradly;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by sylentbv on 3/29/2017.
 */
public class DisplayTicket extends JFrame {
    private JPanel rootPanel;
    private JLabel ticketIDDisplay;
    private JLabel ticketDescriptionDisplay;
    private JLabel enteredByDisplay;
    private JLabel priorityDisplay;
    private JLabel dateResolvedDisplay;
    private JLabel dateEnteredDisplay;
    private JTextField resolutionTF;
    private JButton closeButton;
    private JButton resolveTicketButton;
    private LinkedList<Ticket> ticketQueue;
    private LinkedList<Ticket> resolvedTickets;
    private int ticketID;

    protected DisplayTicket(LinkedList<Ticket> ticketQueueIn, LinkedList<Ticket> resolvedticketIn, int ticketIDIn){
        super("Search Tickets");
        ticketQueue=ticketQueueIn;
        resolvedTickets=resolvedticketIn;
        ticketID=ticketIDIn;
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        buttonConfig();
        getRootPane().setDefaultButton(closeButton);
        setSize(new Dimension(400, 500));

        populateDisplay();
        setVisible(true);
    }

    private void populateDisplay() {
        boolean found = false;
        //search for and display the selected ticket info
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == ticketID) {
                ticketIDDisplay.setText(ticket.getTicketID()+"");
                ticketDescriptionDisplay.setText(ticket.getDescription());
                enteredByDisplay.setText(ticket.getReporter());
                dateEnteredDisplay.setText(ticket.getDateReported()+"");
                priorityDisplay.setText(ticket.getPriority()+"");
                found=true;
                break; //don't need loop any more.
            }
        }
        if(!found){
            //ticket not found in open tickets, search resolved tickets and hide resolve button option
            for (Ticket ticket : resolvedTickets) {
                if (ticket.getTicketID() == ticketID) {
                    ticketIDDisplay.setText(ticket.getTicketID()+"");
                    ticketDescriptionDisplay.setText(ticket.getDescription());
                    enteredByDisplay.setText(ticket.getReporter());
                    dateEnteredDisplay.setText(ticket.getDateReported()+"");
                    priorityDisplay.setText(ticket.getPriority()+"");
                    resolutionTF.setText(ticket.getResolution());
                    dateResolvedDisplay.setText(ticket.getDateResolved()+"");
                    resolveTicketButton.setVisible(false);
                    break; //don't need loop any more.
                }
            }
        }
    }

    private void buttonConfig() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quit = JOptionPane.showConfirmDialog(DisplayTicket.this,"Close Ticket?",
                        "Close",JOptionPane.OK_CANCEL_OPTION);
                if(quit==JOptionPane.OK_OPTION) {
                    //System.exit(0);
                    AllTickets allticketGUI = new AllTickets(ticketQueue,resolvedTickets);
                    DisplayTicket.this.dispose();
                }
            }
        });

        resolveTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String resolution = resolutionTF.getText();

                if(resolution.length()==0){
                    JOptionPane.showMessageDialog(DisplayTicket.this,
                            "Please enter a resolution description.");
                    return;
                }

                for (Ticket ticket : ticketQueue) {
                    if (ticket.getTicketID() == ticketID) {
                        ticket.setResolution(resolution);
                        ticket.setDateResolved(new Date());
                        resolvedTickets.add(ticket);
                        ticketQueue.remove(ticket);
                        dateResolvedDisplay.setText(ticket.getDateResolved()+"");
                        JOptionPane.showMessageDialog(DisplayTicket.this,String.format("Ticket %d resolved", ticketID));
                        break; //don't need loop any more.
                    }
                }

            }
        });
    }
}
