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
public class EnterNewTicket extends JFrame{
    private JPanel rootPanel;
    private JTextField problemDescriptionTF;
    private JTextField reportByTF;
    private JComboBox<Integer> priorityCB;
    private JButton submitButton;
    private JButton closeButton;
    private LinkedList<Ticket> ticketQueue;

    protected EnterNewTicket(LinkedList<Ticket> ticketQueueIn){
        super("Enter New Ticket");
        ticketQueue=ticketQueueIn;
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        populateCombo();
        buttonConfig();
        getRootPane().setDefaultButton(submitButton);
        setSize(new Dimension(400, 210));
        setVisible(true);
    }

    private void populateCombo() {
        for(int x=1;x<=5;x++){
            priorityCB.addItem(x);
        }
    }

    private void buttonConfig() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quit = JOptionPane.showConfirmDialog(EnterNewTicket.this,"Stop Adding Tickets?",
                        "Close",JOptionPane.OK_CANCEL_OPTION);
                if(quit==JOptionPane.OK_OPTION) {
                    //System.exit(0);
                    EnterNewTicket.this.dispose();
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Date dateReported = new Date();
                String description = problemDescriptionTF.getText();
                String reporter = reportByTF.getText();
                int priority = priorityCB.getItemAt(priorityCB.getSelectedIndex());

                Ticket t = new Ticket(description, priority, reporter, dateReported);
                //ticketQueue.add(t);
                addTicketInPriorityOrder(ticketQueue, t);

                JOptionPane.showMessageDialog(EnterNewTicket.this,"Ticket Entered.");
                problemDescriptionTF.setText("");
                reportByTF.setText("");
                priorityCB.setSelectedIndex(0);
            }
        });
    }

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
}
