package service;

import model.Customer;
import util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class CustomerServiceImpl implements CustomerService{
    @Override
    public Boolean addCustomer(Customer customer) throws SQLException {

         return  CrudUtil.execute("INSERT INTO customer (customer_id, name ,address, phone_number) VALUES (?,?,?,?)",
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone()
        );

    }

    @Override
    public Boolean updateCustomer(Customer customer) {
        return null;
    }

    @Override
    public Customer searchById(String id) {
        return null;
    }

    @Override
    public List<Customer> getAll() {
        return List.of();
    }
}
