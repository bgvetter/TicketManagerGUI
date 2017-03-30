package com.bradly;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

/**
 * Created by sylentbv on 3/29/2017.
 */
public class SearchByIssue extends JFrame{
    private JPanel rootPanel;
    private JTextField searchIssueTF;
    private JList issueJList;
    private JButton searchIssuesButton;
    private JButton closeButton;
    private JTextField ticketIDTF;
    private LinkedList<Ticket> ticketQueue;
    private LinkedList<Ticket> resolvedTickets;

    DefaultListModel<Ticket> issueList;

    protected SearchByIssue(LinkedList<Ticket> ticketQueueIn, LinkedList<Ticket> resolvedticketIn){
        super("Search Tickets");
        ticketQueue=ticketQueueIn;
        resolvedTickets=resolvedticketIn;
        issueList = new DefaultListModel<>();
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        buttonConfig();
        listConfig();
        issueJList.setModel(issueList);
        getRootPane().setDefaultButton(searchIssuesButton);
        setSize(new Dimension(400, 500));
        setVisible(true);
        ticketIDTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                searchIssueTF.setText("");
            }
        });
        searchIssueTF.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                ticketIDTF.setText("");
            }
        });
    }

    private void listConfig() {
        issueJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = issueJList.getSelectedIndex();

                if(selectedIndex!=-1){
                    int issueID = issueList.get(selectedIndex).getTicketID();
                    //call issue display form
                    DisplayTicket displaygui = new DisplayTicket(ticketQueue,resolvedTickets,issueID);
                    SearchByIssue.this.dispose();
                }
            }
        });
    }

    private void buttonConfig() {
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quit = JOptionPane.showConfirmDialog(SearchByIssue.this,"Stop Searching Tickets?",
                        "Close",JOptionPane.OK_CANCEL_OPTION);
                if(quit==JOptionPane.OK_OPTION) {
                    //System.exit(0);
                    SearchByIssue.this.dispose();
                }
            }
        });

        searchIssuesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                issueList.removeAllElements();
                String issueSearch=searchIssueTF.getText();
                String ticketIDSearch = ticketIDTF.getText();
                int ticketID;
                if(ticketIDSearch.length()>0) {
                    try {
                        ticketID = Integer.parseInt(ticketIDSearch);

                        for (Ticket t : ticketQueue) {
                            if (t.ticketID == ticketID) {
                                issueList.addElement(t);
                            }
                        }
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(SearchByIssue.this, "Please enter a numeric value for Ticket ID!");
                        return;
                    }

                }
                else {
                    for (Ticket t : ticketQueue) {
                        if (t.getDescription().toUpperCase().contains(issueSearch.toUpperCase())) {
                            issueList.addElement(t);
                        }
                    }
                }
            }
        });
    }
}
