package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.ShoesController;
import controller.ShoesJsonController;
import model.ShoesModel;

public class AdminView extends JFrame {
    private JFrame frame;
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel1, titleLabel2;
    private JButton addButton, updateButton, deleteButton;
    private JTable shoesTable;
    private JTextField searchNameField, searchBrandField;
    private DefaultTableModel tableModel;

    private ArrayList<ShoesModel> shoesList = new ArrayList<>();
    ShoesController shoesController = new ShoesController();

    private final int WIDTH = 1440, HEIGHT = 1024;

    Color whiteColor = new Color(235, 235, 235);
    Color blackColor = new Color(0, 1, 1);
    Color orangeColor = new Color(245, 73, 1);
    Color grayColor = new Color(200, 200, 200);

    private Font fontBold40, fontBold32, fontRegular;

    public AdminView() {
        File fontPathBold = new File("src/utils/font/Poppins-Bold.ttf");
        File fontPathRegular = new File("src/utils/font/Poppins-Regular.ttf");
        try {
            fontBold40 = Font.createFont(Font.TRUETYPE_FONT,
                    fontPathBold).deriveFont(40f);
            fontBold32 = Font.createFont(Font.TRUETYPE_FONT,
                    fontPathBold).deriveFont(32f);
            fontRegular = Font.createFont(Font.TRUETYPE_FONT,
                    fontPathRegular).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        shoesList = shoesController.getAllShoes();
        ShoesJsonController jsonController = new ShoesJsonController();
        jsonController.loadDatabase();

        frame = new JFrame("Management Gudang Sepatu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(WIDTH, HEIGHT);

        leftPanel = new JPanel();
        leftPanel.setSize(265, HEIGHT);
        frame.add(leftPanel);
        leftComponents(leftPanel);

        rightPanel = new JPanel();
        rightPanel.setSize(265 - WIDTH, HEIGHT);
        frame.add(rightPanel);
        rightComponents(rightPanel);

        refreshTable();
        frame.setVisible(true);
    }

    private void leftComponents(JPanel leftPanel) {
        leftPanel.setLayout(null);
        leftPanel.setBackground(blackColor);

        searchNameField = new JTextField();
        searchNameField.setBounds(41, 340, 184, 80);
        addPlaceholder(searchNameField, "Cari Nama Sepatu");

        searchBrandField = new JTextField();
        searchBrandField.setBounds(41, 472, 184, 80);
        addPlaceholder(searchBrandField, "Cari Brand Sepatu");

        searchNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchData();
            }
        });

        searchBrandField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchData();
            }
        });

        updateButton = new JButton("Update");
        updateButton.setOpaque(true);
        updateButton.setBorderPainted(false);
        updateButton.setBackground(grayColor);
        updateButton.setForeground(blackColor);
        updateButton.setFont(fontRegular);
        updateButton.setBounds(41, 604, 184, 80);

        updateButton.addActionListener(event -> {
            try {
                int searchById = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan Id Sepatu: "));
                String updatedName = JOptionPane.showInputDialog(this, "Masukkan Nama Sepatu: ");
                String updatedBrand = JOptionPane.showInputDialog(this, "Masukkan Brand Sepatu: ");
                String updatedType = JOptionPane.showInputDialog(this, "Masukkan Tipe Sepatu: ");
                int updatedStock = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan Stock Sepatu: "));
                boolean updated = shoesController.updateShoes(searchById, updatedName, updatedBrand, updatedType,
                        updatedStock);

                if (updated) {
                    JOptionPane.showMessageDialog(this, "Data Sepatu Telah Diupdate!", "Update Berhasil!",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Data Sepatu Gagal Diupdate!", "Update Gagal!",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salah Memasukkan Input!", "Input Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        deleteButton.setBackground(grayColor);
        deleteButton.setForeground(blackColor);
        deleteButton.setFont(fontRegular);
        deleteButton.setBounds(41, 736, 184, 80);

        deleteButton.addActionListener(event -> {
            try {
                int searchById = Integer.parseInt(JOptionPane.showInputDialog(this, "Masukkan Id Sepatu: "));
                boolean deleted = shoesController.deleteShoes(searchById);

                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Data Sepatu Telah Dihapus!", "Hapus Berhasil!",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Data Sepatu Gagal Dihapus!", "Hapus Gagal!",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Salah Memasukkan Input!", "Input Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        leftPanel.add(searchNameField);
        leftPanel.add(searchBrandField);
        leftPanel.add(updateButton);
        leftPanel.add(deleteButton);
    }

    private void rightComponents(JPanel rightPanel) {
        rightPanel.setLayout(null);
        rightPanel.setBackground(whiteColor);

        titleLabel1 = new JLabel("Management");
        titleLabel1.setForeground(blackColor);
        titleLabel1.setFont(fontBold40);
        titleLabel1.setBounds(321, 57, 300, 60);

        titleLabel2 = new JLabel("Gudang Sepatu");
        titleLabel2.setForeground(blackColor);
        titleLabel2.setFont(fontBold40);
        titleLabel2.setBounds(321, 117, 340, 60);

        addButton = new JButton("Tambah");
        addButton.setOpaque(true);
        addButton.setBorderPainted(false);
        addButton.setBackground(orangeColor);
        addButton.setForeground(blackColor);
        addButton.setFont(fontRegular);
        addButton.setBounds(1148, 81, 184, 80);

        addButton.addActionListener(event -> {
            String shoesName = JOptionPane.showInputDialog(this, "Masukkan Nama Sepatu: ");
            String shoesBrand = JOptionPane.showInputDialog(this, "Masukkan Brand Sepatu: ");
            String shoesType = JOptionPane.showInputDialog(this, "Masukkan Tipe Sepatu: ");
            String shoesStock = JOptionPane.showInputDialog(this, "Masukkan Stock Sepatu: ");

            ShoesModel addShoes = new ShoesModel(shoesController.generateUUID(), shoesName, shoesBrand, shoesType,
                    Integer.parseInt(shoesStock));
            shoesController.addShoes(addShoes);
            refreshTable();
        });

        String[] columnNames = { "ID", "Name", "Brand", "Type", "Stock" };
        tableModel = new DefaultTableModel(null, columnNames);
        shoesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(shoesTable);
        scrollPane.setBounds(265, 257, 1175, 767);

        rightPanel.add(titleLabel1);
        rightPanel.add(titleLabel2);
        rightPanel.add(addButton);
        rightPanel.add(scrollPane);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<ShoesModel> refreshShoesList = shoesController.getAllShoes();
        if (refreshShoesList != null) {
            for (ShoesModel shoes : refreshShoesList) {
                Object[] rowData = { shoes.getId(), shoes.getName(), shoes.getBrand(), shoes.getType(),
                        shoes.getStock() };
                tableModel.addRow(rowData);
            }
        } else {
            System.out.println("Sepatu Kosong!");
        }
    }

    private void searchData() {
        String nama = searchNameField.getText();
        String brand = searchBrandField.getText();

        if (nama.equals("Cari Nama Sepatu")) {
            nama = "";
        }
        if (brand.equals("Cari Brand Sepatu")) {
            brand = "";
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        shoesTable.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + nama, 1);
        RowFilter<DefaultTableModel, Object> brandFilter = RowFilter.regexFilter("(?i)" + brand, 2);

        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();
        filters.add(rowFilter);
        filters.add(brandFilter);

        RowFilter<DefaultTableModel, Object> compoundRowFilter = RowFilter.andFilter(filters);
        sorter.setRowFilter(compoundRowFilter);
    }

    private void addPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
    }

}