package com.bradly;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.LinkedList;

/**
 * Created by sylentbv on 3/29/2017.
 */
public class AllTickets extends JFrame {
    private JPanel rootPanel;
    private JList openTicketsJList;
    private JList resolvedTicketsJList;
    private JButton closeButton;
    private LinkedList<Ticket> ticketQueue;
    private LinkedList<Ticket> resolvedTickets;

    DefaultListModel<Ticket> openTicketList;
    DefaultListModel<Ticket> resolvedTicketList;

    public AllTickets(LinkedList<Ticket> ticketQueueIn, LinkedList<Ticket> resolvedticketIn) {
        super("Ticket List");
        ticketQueue=ticketQueueIn;
        resolvedTickets=resolvedticketIn;
        openTicketList=new DefaultListModel<>();
        resolvedTicketList = new DefaultListModel<>();
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        openTicketsJList.setModel(openTicketList);
        resolvedTicketsJList.setModel(resolvedTicketList);
        buttonConfig();
        listConfig();
        populateLists();
        getRootPane().setDefaultButton(closeButton);
        setSize(new Dimension(400, 500));
        setVisible(true);

    }

    private void populateLists() {
        openTicketList.removeAllElements();
        for (Ticket t : ticketQueue) {
            openTicketList.addElement(t);
        }
        resolvedTicketList.removeAllElements();
        for (Ticket t : resolvedTickets) {
            resolvedTicketList.addElement(t);
        }
    }

    private void listConfig() {
        openTicketsJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = openTicketsJList.getSelectedIndex();

                if(selectedIndex!=-1){
                    int issueID = openTicketList.get(selectedIndex).getTicketID();
                    //call issue display form
                    DisplayTicket displaygui = new DisplayTicket(ticketQueue,resolvedTickets,issueID);
                    AllTickets.this.dispose();
                }
            }
        });

        resolvedTicketsJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = resolvedTicketsJList.getSelectedIndex();

                if(selectedIndex!=-1){
                    int issueID = resolvedTicketList.get(selectedIndex).getTicketID();
                    //call issue display form
                    DisplayTicket displaygui = new DisplayTicket(ticketQueue,resolvedTickets,issueID);
                    AllTickets.this.dispose();
                }
            }
        });
    }

    private void buttonConfig() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quit = JOptionPane.showConfirmDialog(AllTickets.this,"Close Tickets List?",
                        "Close",JOptionPane.OK_CANCEL_OPTION);
                if(quit==JOptionPane.OK_OPTION) {
                    //System.exit(0);
                    AllTickets.this.dispose();
                }
            }
        });

    }
}
