package com.example.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Leave_Management extends JFrame {

    private JTable tblApproved;
    private JTable tblRejected;

    private JPanel approvedPanel;
    private JPanel rejectedPanel;

    private JPanel dynamicTablePanel;
    private JTable dynamicTable;
    private DefaultTableModel dynamicTableModel;

    private JPanel chartPanel;

    private List<Leave> leaves = new ArrayList<>();

    public Leave_Management() {
        initDummyData();
        initUI();
    }

    private void initDummyData() {
        leaves.add(new Leave("E001", "Juan Dela Cruz", "Sick Leave", "2025-12-01", "2025-12-03", "Pending", ""));
        leaves.add(new Leave("E002", "Maria Santos", "Vacation Leave", "2025-12-10", "2025-12-12", "Pending", ""));
        leaves.add(new Leave("E003", "Jose Rizal", "Vacation Leave", "2025-11-20", "2025-11-25", "Approved", ""));
        leaves.add(new Leave("E004", "Ana Lopez", "Maternity Leave", "2025-10-01", "2025-10-30", "Approved", ""));
        leaves.add(new Leave("E005", "Pedro Reyes", "Emergency Leave", "2025-12-05", "2025-12-06", "Rejected", "Personal emergency"));
        leaves.add(new Leave("E006", "Luis Dela Cruz", "Unpaid Leave", "2025-12-02", "2025-12-04", "Pending", ""));
    }

    private void initUI() {
        setTitle("Leave Management Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 740);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        buildHeader();
        buildSummaryPanel();
        buildApprovedPanel();
        buildRejectedPanel();
        buildDynamicTablePanel();
        buildChartPanel();

        JButton btnDashboardBack = new JButton("Back");
        addHoverEffect(btnDashboardBack);
        btnDashboardBack.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        btnDashboardBack.setBounds(20, 680, 100, 35);
        btnDashboardBack.addActionListener(e -> dispose());
        getContentPane().add(btnDashboardBack);
    }

    private void buildHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 25, 70));
        headerPanel.setBounds(0, 0, 1120, 72);
        headerPanel.setLayout(null);
        getContentPane().add(headerPanel);

        JLabel lblTitle = new JLabel("Leave Management System");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(20, 18, 400, 36);
        headerPanel.add(lblTitle);
    }

    private void buildSummaryPanel() {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(1, 6, 12, 0));
        summaryPanel.setBounds(20, 86, 1080, 110);
        summaryPanel.setOpaque(false);
        getContentPane().add(summaryPanel);

        String[] cardTitles = {
                "Leave of Absence", "Sick Leave", "Vacation Leave",
                "Emergency Leave", "Maternity Leave", "Unpaid Leave"
        };

        Color start = new Color(160, 210, 255);
        Color end = new Color(120, 170, 235);

        for (String title : cardTitles) {
            JPanel card = createSummaryCard(title, start, end);
            summaryPanel.add(card);
        }
    }

    private JPanel createSummaryCard(String title, Color start, Color end) {
        ModernCard card = new ModernCard(start, end);

        int count = getPendingLeavesCount(title);
        JLabel lblTitle = new JLabel("<html><center>" + title + "<br>" + count + "</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 180, 70);
        card.setLayout(null);
        card.add(lblTitle);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDynamicTable(title);
            }
        });

        return card;
    }

    private int getPendingLeavesCount(String type) {
        int count = 0;
        for (Leave l : leaves) {
            if (l.type.equals(type) && l.status.equals("Pending")) count++;
        }
        return count;
    }

    private void buildApprovedPanel() {
        approvedPanel = new JPanel();
        approvedPanel.setBorder(new TitledBorder(new EtchedBorder(), "Approved Leaves"));
        approvedPanel.setBounds(20, 200, 780, 200);
        approvedPanel.setLayout(new BorderLayout());
        approvedPanel.setBackground(Color.WHITE);
        getContentPane().add(approvedPanel);

        String[] columns = {"EMPID", "EMP NAME", "TYPE", "STATUS"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblApproved = new JTable(model);
        styleTable(tblApproved);

        for (Leave l : leaves) if (l.status.equals("Approved"))
            model.addRow(new Object[]{l.empId, l.name, l.type, l.status});

        approvedPanel.add(new JScrollPane(tblApproved), BorderLayout.CENTER);
    }

    private void buildRejectedPanel() {
        rejectedPanel = new JPanel();
        rejectedPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rejected Leaves"));
        rejectedPanel.setBounds(20, 410, 780, 250);
        rejectedPanel.setLayout(new BorderLayout());
        rejectedPanel.setBackground(Color.WHITE);
        getContentPane().add(rejectedPanel);

        String[] columns = {"EMPID", "EMP NAME", "TYPE", "REASON"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblRejected = new JTable(model);
        styleTable(tblRejected);

        for (Leave l : leaves) if (l.status.equals("Rejected"))
            model.addRow(new Object[]{l.empId, l.name, l.type, l.reason});

        rejectedPanel.add(new JScrollPane(tblRejected), BorderLayout.CENTER);
    }

    private void buildDynamicTablePanel() {
        dynamicTablePanel = new JPanel();
        dynamicTablePanel.setBorder(new TitledBorder(new EtchedBorder(), "Leave Details"));
        dynamicTablePanel.setBounds(20, 200, 1080, 460);
        dynamicTablePanel.setLayout(new BorderLayout());
        dynamicTablePanel.setBackground(Color.WHITE);
        dynamicTablePanel.setVisible(false);
        getContentPane().add(dynamicTablePanel);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JButton btnBack = new JButton("Back to Dashboard");
        addHoverEffect(btnBack);
        btnBack.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
        btnBack.setBackground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            dynamicTablePanel.setVisible(false);
            approvedPanel.setVisible(true);
            rejectedPanel.setVisible(true);
            chartPanel.setVisible(true);
        });

        topPanel.add(btnBack);
        dynamicTablePanel.add(topPanel, BorderLayout.NORTH);

        dynamicTableModel = new DefaultTableModel(new Object[]{"EMPID", "EMP NAME", "TYPE", "START DATE", "END DATE", "STATUS"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        dynamicTable = new JTable(dynamicTableModel);
        styleTable(dynamicTable);

        dynamicTablePanel.add(new JScrollPane(dynamicTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAccept = new JButton("Accept");
        JButton btnReject = new JButton("Reject");
        addHoverEffect(btnAccept);
        addHoverEffect(btnReject);

        btnAccept.addActionListener(e -> updateSelectedStatus(dynamicTable, "Approved"));
        btnReject.addActionListener(e -> {
            int[] selectedRows = dynamicTable.getSelectedRows();
            for (int row : selectedRows) {
                String reason = JOptionPane.showInputDialog(this, "Enter reason for rejection:", "Rejection Reason", JOptionPane.PLAIN_MESSAGE);
                if (reason != null && !reason.trim().isEmpty()) {
                    dynamicTable.setValueAt("Rejected", row, 5);
                    dynamicTable.setValueAt(reason.trim(), row, 5); // also store temporarily
                    // update main data
                    String empId = (String) dynamicTable.getValueAt(row, 0);
                    for (Leave l : leaves) {
                        if (l.empId.equals(empId)) {
                            l.status = "Rejected";
                            l.reason = reason.trim();
                        }
                    }
                }
            }
            refreshTables();
        });

        btnPanel.add(btnAccept);
        btnPanel.add(btnReject);
        dynamicTablePanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void refreshTables() {
        // Refresh Approved
        DefaultTableModel approvedModel = (DefaultTableModel) tblApproved.getModel();
        approvedModel.setRowCount(0);
        for (Leave l : leaves) if (l.status.equals("Approved"))
            approvedModel.addRow(new Object[]{l.empId, l.name, l.type, l.status});

        // Refresh Rejected
        DefaultTableModel rejectedModel = (DefaultTableModel) tblRejected.getModel();
        rejectedModel.setRowCount(0);
        for (Leave l : leaves) if (l.status.equals("Rejected"))
            rejectedModel.addRow(new Object[]{l.empId, l.name, l.type, l.reason});

        chartPanel.repaint();
    }

    private void showDynamicTable(String type) {
        approvedPanel.setVisible(false);
        rejectedPanel.setVisible(false);
        chartPanel.setVisible(false);
        dynamicTablePanel.setVisible(true);

        dynamicTableModel.setRowCount(0);
        for (Leave l : leaves)
            if (l.type.equals(type))
                dynamicTableModel.addRow(new Object[]{l.empId, l.name, l.type, l.start, l.end, l.status});

        dynamicTablePanel.revalidate();
        dynamicTablePanel.repaint();
    }

    private void buildChartPanel() {
        chartPanel = new JPanel() {
            private double displayedPercentage = 0;
            private double displayedCount = 0;
            private double targetPercentage = 0;
            private double targetCount = 0;
            private float gradientOffset = 0f;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int totalLeaves = leaves.size();
                int pendingLeaves = 0;
                for (Leave l : leaves) if (l.status.equals("Pending")) pendingLeaves++;

                targetPercentage = totalLeaves == 0 ? 0 : (double) pendingLeaves / totalLeaves;
                targetCount = pendingLeaves;

                displayedPercentage += (targetPercentage - displayedPercentage) * 0.2;
                displayedCount += (targetCount - displayedCount) * 0.2;
                gradientOffset += 0.02f;

                int width = getWidth();
                int height = getHeight();
                int diameter = Math.min(width, height) - 40;
                int centerX = width / 2;
                int centerY = height / 2;

                int arcAngle = (int) (displayedPercentage * 360);

                g2.setColor(new Color(220, 220, 220));
                g2.fillOval(centerX - diameter/2, centerY - diameter/2, diameter, diameter);

                g2.setPaint(new GradientPaint(centerX + gradientOffset * diameter, centerY,
                        new Color(70, 130, 180), centerX - gradientOffset * diameter, centerY,
                        new Color(100, 180, 250), true));
                g2.fillArc(centerX - diameter/2, centerY - diameter/2, diameter, diameter, 90, -arcAngle);

                int hole = diameter / 2;
                g2.setColor(Color.WHITE);
                g2.fillOval(centerX - hole/2, centerY - hole/2, hole, hole);

                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Segoe UI Semibold", Font.BOLD, 18));
                String text = "Pending: " + (int) Math.round(displayedCount);
                int textWidth = g2.getFontMetrics().stringWidth(text);
                int textHeight = g2.getFontMetrics().getHeight();
                g2.drawString(text, centerX - textWidth / 2, centerY + textHeight / 4);

                repaint();
            }
        };

        chartPanel.setBounds(830, 200, 250, 460);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new TitledBorder("Pending Leaves"));
        getContentPane().add(chartPanel);
    }

    private void updateSelectedStatus(JTable table, String status) {
        int[] selectedRows = table.getSelectedRows();
        for (int row : selectedRows) {
            String empId = (String) table.getValueAt(row, 0);
            for (Leave l : leaves) {
                if (l.empId.equals(empId)) {
                    l.status = status;
                    if (status.equals("Rejected") && l.reason.isEmpty()) {
                        l.reason = "No reason provided";
                    }
                }
            }
            table.setValueAt(status, row, 5);
        }
        refreshTables();
    }

    private void addHoverEffect(JButton button) {
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { button.setBackground(new Color(220,220,220)); }
            public void mouseExited(MouseEvent evt) { button.setBackground(Color.WHITE); }
        });
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(50, 75, 150));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        header.setReorderingAllowed(false);
    }

    class ModernCard extends JPanel {
        private Color startColor;
        private Color endColor;
        private boolean hovered = false;

        public ModernCard(Color startColor, Color endColor){
            this.startColor = startColor;
            this.endColor = endColor;
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter(){
                @Override public void mouseEntered(MouseEvent e){ hovered=true; repaint();}
                @Override public void mouseExited(MouseEvent e){ hovered=false; repaint();}
            });
        }

        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = 20;
            int shadowOffset = hovered?6:3;
            g2.setColor(new Color(0,0,0,hovered?50:30));
            g2.fillRoundRect(shadowOffset, shadowOffset, getWidth()-shadowOffset*2, getHeight()-shadowOffset*2, arc, arc);
            GradientPaint gp = new GradientPaint(0,0,startColor,getWidth(),getHeight(),endColor);
            g2.setPaint(gp);
            g2.fillRoundRect(0,0,getWidth()-shadowOffset,getHeight()-shadowOffset,arc,arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class Leave {
        String empId, name, type, start, end, status, reason;
        public Leave(String empId, String name, String type, String start, String end, String status, String reason){
            this.empId = empId;
            this.name = name;
            this.type = type;
            this.start = start;
            this.end = end;
            this.status = status;
            this.reason = reason;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Leave_Management ex = new Leave_Management();
            ex.setVisible(true);
        });
    }
}
