package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import service.CustomerServiceImpl;
import util.CrudUtil;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colID;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colPhone;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TableView<Customer> txtTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String id = txtId.getText();
        String name = txtName.getText();
        String address = txtAddress.getText();
        String phone = txtPhone.getText();


        if (id.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Please fill all fields.").show();
            return;
        }

        // check if customer ID Already Exist
        try {
            ResultSet rs = CrudUtil.execute("SELECT customer_id FROM customer WHERE customer_id=?", id);
            if (rs.next()){
                new Alert(Alert.AlertType.ERROR,"Customer with ID " + id + "already exists ").show();
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Customer newCustomer = new Customer(id,name,address,phone);

        try {
            CustomerServiceImpl customerService = new CustomerServiceImpl();
            Boolean b = customerService.addCustomer(newCustomer);


            if (b){
                new Alert(Alert.AlertType.INFORMATION, "Customer Added Successfully! ").show();
                loadTable();
            }else {
                new Alert(Alert.AlertType.ERROR, "Failed to add Customer").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Customer selectedCustomer = txtTable.getSelectionModel().getSelectedItem();
        String idToDelete;

        if (selectedCustomer != null) {
            //  Row selected
            idToDelete = selectedCustomer.getId();
        } else {

            idToDelete = txtId.getText();
            if (idToDelete.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select a customer from the Table or enter an ID.").show();
                return;
            }
        }

        try {
            boolean isDeleted = CrudUtil.execute("DELETE FROM customer WHERE customer_id = ?", idToDelete);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully").show();
                    // Clear input fields (optional)
            } else {
                new Alert(Alert.AlertType.ERROR, "Customer not found or could not be deleted.").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
        new Alert(Alert.AlertType.INFORMATION, "Table Reloaded.").show();
    }



    private void loadTable(){
        List<Customer> customerList = new ArrayList<>();

        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM customer");
            while (resultSet.next())
                customerList.add(new Customer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                ));

            colID.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

            ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
            customerList.forEach( customer-> {
                customerObservableList.add(customer);
        });
            txtTable.setItems(customerObservableList);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
