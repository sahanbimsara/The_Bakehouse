package service;

import model.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerService {

    Boolean addCustomer (Customer customer) throws SQLException;

    Boolean updateCustomer (Customer customer);

    Customer searchById (String id);

    List<Customer> getAll ();

}
