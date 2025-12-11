package com.example.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class System_Administration extends JFrame {

    private final List<Account> accounts = new ArrayList<>();
    private final List<Account> archivedManagers = new ArrayList<>();
    private final List<Account> archivedStaff = new ArrayList<>();

    private JPanel topPanel; 
    private JPanel contentPanel; 

    private JTable activeTable;
    private DefaultTableModel activeModel;

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public System_Administration() {
        setTitle("System Administration");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());
        initTopPanel();
        initContentPanel(); // content panel starts empty
        initLogoutButton();
    }

    private void initTopPanel() {
        topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(10, 25, 47));
        topPanel.setPreferredSize(new Dimension(0, 300));
        getContentPane().add(topPanel, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        JLabel title = new JLabel("WELCOME ADMIN");
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.ipady = 20;
        topPanel.add(title, gbc);

        gbc.gridwidth = 1; gbc.ipady = 0; gbc.gridy = 1;

        // Cards
        JPanel card1 = createCard("Employee Registration", "Add new employees and assign roles.");
        card1.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { showRegistrationForm(); } });
        gbc.gridx = 0; topPanel.add(card1, gbc);

        JPanel card2 = createCard("Account Deactivation", "Disable employee accounts securely.");
        card2.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { showDeactivationForm(); } });
        gbc.gridx = 1; topPanel.add(card2, gbc);

        JPanel card3 = createCard("Archived Managers", "View archived managers.");
        card3.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { showArchivedManagersPanel(); } });
        gbc.gridx = 2; topPanel.add(card3, gbc);

        JPanel card4 = createCard("Archived Staff", "View archived staff.");
        card4.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { showArchivedStaffPanel(); } });
        gbc.gridx = 3; topPanel.add(card4, gbc);
    }

    private JPanel createCard(String title, String subtitle) {
        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(220, 110));
        card.setBackground(new Color(40, 70, 120));
        card.setBorder(new LineBorder(new Color(180, 180, 255), 2, true));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        lblTitle.setBounds(15, 10, 200, 25);
        card.add(lblTitle);

        JLabel lblSub = new JLabel("<html>" + subtitle + "</html>");
        lblSub.setForeground(Color.WHITE);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setBounds(15, 35, 200, 60);
        card.add(lblSub);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(52, 95, 150)); }
            public void mouseExited(MouseEvent e) { card.setBackground(new Color(40, 70, 120)); }
        });

        return card;
    }

    private void initContentPanel() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(15, 30, 60));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }

    private void initActiveAccountsTable() {
        String[] cols = {"Employee ID", "Full Name", "Role", "Employee Type", "Status", "Deact Reason", "Deact Date"};
        activeModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c){ return false; } };

        activeTable = new JTable(activeModel);
        activeTable.setFillsViewportHeight(true);
        activeTable.setAutoCreateRowSorter(true);
        activeTable.setRowHeight(28);
        activeTable.setBackground(Color.WHITE);
        activeTable.setGridColor(Color.LIGHT_GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        activeTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        activeTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        activeTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane sp = new JScrollPane(activeTable);
        sp.setBorder(new LineBorder(Color.WHITE,1,true));
        contentPanel.removeAll();
        contentPanel.add(sp, BorderLayout.CENTER);

        refreshActiveAccountsTable();
    }

    private void refreshActiveAccountsTable() {
        activeModel.setRowCount(0);
        for (Account a : accounts) {
            activeModel.addRow(new Object[]{
                    a.employeeId, a.fullName, a.role,
                    a.employeeType == null ? "" : a.employeeType,
                    a.status,
                    a.deactivationReason == null ? "" : a.deactivationReason,
                    a.deactivationDate == null ? "" : a.deactivationDate.format(dtf)
            });
        }
    }

    // -----------------------
    // Employee Registration Form
    // -----------------------
    private void showRegistrationForm() {
        contentPanel.removeAll();
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Employee Registration Form");
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        title.setForeground(Color.BLACK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;

        JLabel lblName = new JLabel("Full Name:"); lblName.setForeground(Color.BLACK); gbc.gridx = 0; panel.add(lblName, gbc);
        JTextField txtName = new JTextField(); gbc.gridx = 1; panel.add(txtName, gbc);

        JLabel lblID = new JLabel("Employee ID:"); lblID.setForeground(Color.BLACK); gbc.gridy = 2; gbc.gridx = 0; panel.add(lblID, gbc);
        JTextField txtID = new JTextField(); gbc.gridx = 1; panel.add(txtID, gbc);

        JLabel lblRole = new JLabel("Role:"); lblRole.setForeground(Color.BLACK); gbc.gridy = 3; gbc.gridx = 0; panel.add(lblRole, gbc);
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Manager","Staff","Employee"}); gbc.gridx = 1; panel.add(cmbRole, gbc);

        JLabel lblEmpType = new JLabel("Employee Type:"); lblEmpType.setForeground(Color.BLACK); gbc.gridy = 4; gbc.gridx = 0; panel.add(lblEmpType, gbc);
        JComboBox<String> cmbEmpType = new JComboBox<>(new String[]{"Regular","Part-Time"}); gbc.gridx = 1; panel.add(cmbEmpType, gbc);

        cmbRole.addActionListener(e -> cmbEmpType.setEnabled("Employee".equals(cmbRole.getSelectedItem())));
        cmbEmpType.setEnabled(true);

        JButton btnRegister = new JButton("REGISTER"); styleButton(btnRegister);
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(btnRegister, gbc);

        JLabel lblMsg = new JLabel(""); lblMsg.setForeground(Color.GREEN);
        gbc.gridy = 6; panel.add(lblMsg, gbc);

        btnRegister.addActionListener(e -> {
            String name = txtName.getText().trim();
            String empId = txtID.getText().trim();
            String role = (String) cmbRole.getSelectedItem();
            String empType = (String) cmbEmpType.getSelectedItem();

            if(name.isEmpty()||empId.isEmpty()||role==null){ lblMsg.setForeground(Color.ORANGE); lblMsg.setText("Fill required fields."); return; }
            if(findAccountById(empId)!=null){ lblMsg.setForeground(Color.RED); lblMsg.setText("Employee ID exists."); return; }

            Account a = new Account(empId, name, role, "Employee".equals(role)?empType:"");
            accounts.add(a);
            lblMsg.setForeground(Color.GREEN); lblMsg.setText("Account created ("+role+").");

            txtName.setText(""); txtID.setText(""); cmbRole.setSelectedIndex(0); cmbEmpType.setSelectedIndex(0);
        });

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // -----------------------
    // Account Deactivation Form
    // -----------------------
    private void showDeactivationForm() {
        contentPanel.removeAll();
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Account Deactivation");
        title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 22));
        title.setForeground(Color.BLACK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;

        JLabel lblSelect = new JLabel("Select Account:"); lblSelect.setForeground(Color.BLACK); gbc.gridx = 0; panel.add(lblSelect, gbc);
        JComboBox<String> cmbAccounts = new JComboBox<>(); gbc.gridx = 1; panel.add(cmbAccounts, gbc);
        for(Account a:accounts) if(a.isActive()) cmbAccounts.addItem(a.toString());

        JLabel lblReason = new JLabel("Deactivation Reason:"); lblReason.setForeground(Color.BLACK); gbc.gridy = 2; gbc.gridx = 0; panel.add(lblReason, gbc);
        JComboBox<String> cmbReason = new JComboBox<>(new String[]{"Resignation","Termination","Inactivity","Other"}); gbc.gridx = 1; panel.add(cmbReason, gbc);
        JTextField txtOther = new JTextField(); gbc.gridy = 3; gbc.gridx = 1; txtOther.setVisible(false); panel.add(txtOther, gbc);
        cmbReason.addActionListener(e -> txtOther.setVisible("Other".equals(cmbReason.getSelectedItem())));

        JButton btnDeactivate = new JButton("DEACTIVATE"); styleButton(btnDeactivate);
        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; panel.add(btnDeactivate, gbc);

        JLabel lblMsg = new JLabel(""); lblMsg.setForeground(Color.GREEN);
        gbc.gridy = 5; panel.add(lblMsg, gbc);

        btnDeactivate.addActionListener(e -> {
            int sel = cmbAccounts.getSelectedIndex();
            if(sel<0){ lblMsg.setForeground(Color.ORANGE); lblMsg.setText("No account selected."); return; }
            String empId = ((String)cmbAccounts.getSelectedItem()).split(" - ")[0];
            Account a = findAccountById(empId);
            if(a==null||!a.isActive()){ lblMsg.setForeground(Color.RED); lblMsg.setText("Account not found."); return; }

            String reason = (String)cmbReason.getSelectedItem();
            if("Other".equals(reason)) reason = txtOther.getText().trim();
            if(reason.isEmpty()) reason="No reason provided";

            a.deactivate(reason,LocalDateTime.now());
            if("Manager".equalsIgnoreCase(a.role)) archivedManagers.add(a);
            else if("Staff".equalsIgnoreCase(a.role)) archivedStaff.add(a);

            lblMsg.setForeground(Color.GREEN); lblMsg.setText("Account deactivated.");
            cmbAccounts.removeItemAt(sel);
        });

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // -----------------------
    // Archived Panels
    // -----------------------
    private void showArchivedManagersPanel() { showArchivedPanel("Archived Managers", archivedManagers); }
    private void showArchivedStaffPanel() { showArchivedPanel("Archived Staff", archivedStaff); }

    private void showArchivedPanel(String titleText, List<Account> archivedList){
        contentPanel.removeAll(); contentPanel.setLayout(new BorderLayout(8,8));

        JLabel title = new JLabel(titleText); title.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20)); title.setForeground(Color.WHITE); contentPanel.add(title, BorderLayout.NORTH);

        String[] cols = {"Employee ID","Full Name","Status","Deact Reason","Deact Date"};
        DefaultTableModel model = new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){return false;} };
        JTable table = new JTable(model); table.setFillsViewportHeight(true); table.setAutoCreateRowSorter(true);
        table.setBackground(Color.WHITE); table.setGridColor(Color.LIGHT_GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        for(Account a:archivedList) model.addRow(new Object[]{ a.employeeId,a.fullName,a.status,a.deactivationReason,a.deactivationDate==null?"":a.deactivationDate.format(dtf) });
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnReactivate = new JButton("REACTIVATE SELECTED"); 
        styleButton(btnReactivate);
        btnReactivate.setForeground(Color.WHITE); 
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); btnPanel.setOpaque(false); btnPanel.add(btnReactivate); contentPanel.add(btnPanel, BorderLayout.SOUTH);

        btnReactivate.addActionListener(e -> {
            int row = table.getSelectedRow(); if(row<0) return;
            String empId = (String) model.getValueAt(table.convertRowIndexToModel(row),0);
            Account a = findAccountById(empId); if(a==null) return;
            a.reactivate(); archivedList.remove(a); showArchivedPanel(titleText,archivedList);
            JOptionPane.showMessageDialog(this,"Account reactivated");
        });

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void initLogoutButton() {
        JButton logoutBtn = new JButton("LOGOUT");
        styleButton(logoutBtn);
        logoutBtn.setForeground(Color.WHITE); 
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(10,25,47));
        panel.add(logoutBtn);
        getContentPane().add(panel, BorderLayout.SOUTH);

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,"Are you sure you want to logout?","Confirm Logout",JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION){
                dispose(); 
            }
        });
    }

    private void styleButton(JButton btn){
        btn.setFont(new Font("Segoe UI Semibold",Font.BOLD,13));
        btn.setBackground(new Color(40,70,120));
        btn.setBorder(new LineBorder(new Color(180,180,180),2,true));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private Account findAccountById(String empId){
        for(Account a:accounts) if(a.employeeId.equals(empId)) return a;
        return null;
    }

    private static class Account{
        String employeeId, fullName, role, employeeType, status, deactivationReason;
        LocalDateTime deactivationDate;
        Account(String id,String name,String role,String empType){ this.employeeId=id; this.fullName=name; this.role=role; this.employeeType=empType==null?"":empType; this.status="Active"; }
        boolean isActive(){return "Active".equalsIgnoreCase(status);}
        void deactivate(String reason, LocalDateTime when){status="Deactivated"; deactivationReason=reason; deactivationDate=when;}
        void reactivate(){status="Active"; deactivationReason=null; deactivationDate=null;}
        @Override public String toString(){return employeeId+" - "+fullName+" ("+role+")";}
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new System_Administration().setVisible(true));
    }
}
