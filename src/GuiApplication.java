/*
 Copyright 2008 Christopher Painter-Wakefield
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class GuiApplication implements ActionListener, ListSelectionListener {
    // Model
    GuiModel model;
    //GuiModel[] modelArray;
    
    // UI components
    JFrame 		frame;
    
    JPanel		searchPane;
    JLabel      cityLabel;
    JComboBox	citySelection;
    
    JLabel      searchLabel;
    JComboBox   searchType;
    JTextField  searchText;
    JButton		searchButton;
    
    JPanel      resultsPane;
    JTable      searchResultsTable;
    
    JPanel      buttonPane;
    JButton     addArtistButton;
    JButton     addAlbumButton;
    JButton     editAlbumButton;
    JButton     deleteAlbumButton;
    JButton     quitButton;
    
    final City startingCity = City.Chicago;
    
    public static void main(String[] args) {
        GuiApplication app = new GuiApplication();
        app.open();
    }
    
    void open() {
        model = new GuiModel();
        model.setColumns(ChicagoDataLabelArray);
        GuiLoginDialog loginDialog = new GuiLoginDialog(model);
        loginDialog.open();
        
        frame = new JFrame("Crime Database Interface");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container cp = frame.getContentPane();
        
        searchPane = new JPanel();
        searchPane.setLayout(new FlowLayout());
        searchPane.setBorder(new EtchedBorder());
        cp.add(searchPane, BorderLayout.NORTH);
        
        cityLabel = new JLabel("City:");
        searchPane.add(cityLabel);
        
        citySelection = new JComboBox();
        citySelection.addItem("Chicago");
        citySelection.addItem("Denver");
        citySelection.addItem("LA");
        citySelection.addActionListener(this);
        searchPane.add(citySelection);
        //The stuff for the search options that may vary between cities
        searchLabel = new JLabel("Search by:");
        searchPane.add(searchLabel);
        
        searchType = new JComboBox();
        // searchType.addItem("option 1");
        //searchType.addItem("option 2");
        // searchType.addItem("option 3");
        updateSearchByOptions(startingCity);
        searchType.addActionListener(this);
        searchPane.add(searchType);
        
        searchText = new JTextField(10);
        searchPane.add(searchText);
        
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPane.add(searchButton);
        
        resultsPane = new JPanel();
        resultsPane.setLayout(new FlowLayout());
        cp.add(resultsPane, BorderLayout.CENTER);
        
        
        searchResultsTable = new JTable();
        searchResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultsTable.setShowGrid(false);
        searchResultsTable.getSelectionModel().addListSelectionListener(this);
        searchResultsTable.setModel(model);
        JScrollPane scroller = new JScrollPane(searchResultsTable);
        scroller.setPreferredSize(new Dimension(600, 350));
        resultsPane.add(scroller);
        
        buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        cp.add(buttonPane, BorderLayout.SOUTH);
        
        quitButton = new JButton("Quit");
        quitButton.addActionListener(this);
        
        buttonPane.add(quitButton);
        
        frame.pack();
        frame.setVisible(true);
        
    }
    
    // process action events
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.searchButton) {
            search();
        }
        else if (e.getSource() == this.addArtistButton) {
            //addArtist();
        }
        else if (e.getSource() == this.addAlbumButton) {
            //addAlbum();
        }
        else if (e.getSource() == this.editAlbumButton) {
            //editAlbum();
        }
        else if (e.getSource() == this.deleteAlbumButton) {
            //deleteAlbum();
        }
        else if (e.getSource() == this.quitButton) {
            System.exit(0);
        }
        else if(e.getSource() == this.citySelection){
            onCityTypeChanged();
        }
    }
    private void onCityTypeChanged(){
        City c = City.valueOf((String)citySelection.getSelectedItem());
        updateSearchByOptions(c);
        //Will also need to change the column labels in the list of rows
        updateDataLabels(c);
    }
    static Object[] LADataLabelArray = {"CrimeDate", "Description", "Address", "Zip"};
    static Object[] ChicagoDataLabelArray = {"CrimeDate", "Description", "CityBlock"};
    static Object[] DenverDataLabelArray = {"CrimeDate", "Description", "Address", "Neighborhood"};
    static public int currentCityColumnCount(City city){
        switch(city){
            case Chicago:
                return ChicagoDataLabelArray.length;
            case Denver:
                return DenverDataLabelArray.length;
            case LA:
                return LADataLabelArray.length;
            default:
                break;
        }
        System.out.println("failed to match city in currentCityColumnCount(City)");
        return -1;
    }
    private void updateDataLabels(City c){
        switch(c){
            case Chicago:
                ChicagoDataLabels();
                break;
            case Denver:
                DenverDataLabels();
                break;
            case LA:
                LADataLabels();
                break;
            default:
                break;
        }
    }
    private void LADataLabels(){
        model.setColumns(LADataLabelArray);
    }
    private void ChicagoDataLabels(){
        model.setColumns(ChicagoDataLabelArray);
    }
    private void DenverDataLabels(){
        model.setColumns(DenverDataLabelArray);
    }
    private void updateSearchByOptions(City c){
        searchType.removeAllItems();
        switch(c){
            case Chicago:
                ChicagoSearchByOptions();
                break;
            case Denver:
                DenverSearchByOptions();
                break;
            case LA:
                LASearchByOptions();
                break;
            default:
                System.out.println("This should be impossible - updateSearchByOptions");
                searchType.addItem("ERROR");
                break;
        }
    }
    private void LASearchByOptions(){
        for(int i = 0; i < LADataLabelArray.length; i++){
            searchType.addItem(LADataLabelArray[i]);
        }
    }
    private void ChicagoSearchByOptions(){
        for(int i = 0; i < ChicagoDataLabelArray.length; i++){
            searchType.addItem(ChicagoDataLabelArray[i]);
        }
    }
    private void DenverSearchByOptions(){
        for(int i = 0; i < DenverDataLabelArray.length; i++){
            searchType.addItem(DenverDataLabelArray[i]);
        }
    }
    
    private void search() {
        City c = City.valueOf((String)citySelection.getSelectedItem());
        try {
            model.search(c, (String)searchType.getSelectedItem(), searchText.getText());
        } catch (SQLException ex) {
            sqlExceptionHandler(ex);
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
        if (searchResultsTable.getSelectedRow() == -1) {
            editAlbumButton.setEnabled(false);
            deleteAlbumButton.setEnabled(false);
        }
        else {
            editAlbumButton.setEnabled(true);
            deleteAlbumButton.setEnabled(true);
        }
        model.setSelectedRow(searchResultsTable.getSelectedRow());
    }
    
    private void sqlExceptionHandler(SQLException e) {
        JOptionPane.showMessageDialog(frame,
                                      "Database error: " + e.getMessage(),
                                      "Database error",
                                      ERROR_MESSAGE);
    }
    
}
