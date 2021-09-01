package com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bean.CustomerBean;
import com.bean.LoginBean;


@Repository
public class CustomerDao {
	
	@Autowired
	JdbcTemplate stmt;

	public void addCustomer(CustomerBean customer) {
		stmt.update("insert into customer (firstName,email,password) values (?,?,?)", customer.getFirstName(),
				customer.getEmail(), customer.getPassword());
	}

	public CustomerBean authenticate(String email, String password) {

		CustomerBean customer = null;

		try {
			customer = stmt.queryForObject("select * from customer where email like ? and password like ?",
					new BeanPropertyRowMapper<CustomerBean>(CustomerBean.class), email, password);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return customer;
	}

	public void updateToken(int customerId, String token) {
		stmt.update("update customer set token = ? where customerId = ?", token, customerId);
	}

	public CustomerBean getCustomerByToken(String token) {
		CustomerBean customer = null;

		try {
			customer = stmt.queryForObject("select * from customer where token like ? ",
					new BeanPropertyRowMapper<CustomerBean>(CustomerBean.class), token);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return customer;
	}

	

	public List<CustomerBean> getCustomer() {
		List<CustomerBean> users= stmt.query("select * from customer",new BeanPropertyRowMapper<CustomerBean>(CustomerBean.class));
		return users;
	}

	public CustomerBean deleteCustomerById(int customerId) {
		CustomerBean customerBean = getCustomerById(customerId);
		if(customerBean != null) {
			stmt.update("delete from customer where customerId = ?",customerId);
		}
		return customerBean;
	}
	
	public CustomerBean getCustomerById(int customerId) {
		
		CustomerBean customerBean = null;	
		try {
			customerBean = stmt.queryForObject("select * from customer where customerId = ?", new BeanPropertyRowMapper<CustomerBean>(CustomerBean.class),customerId);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return customerBean;
	}
	
	

	public CustomerBean updateCustomer(CustomerBean bean) {
		stmt.update("update customer set firstname =?,email=? where token = ?",bean.getFirstName(),bean.getEmail(),bean.getToken());
		return bean;
	}

	public CustomerBean updateCustomerById(CustomerBean customer) {
		stmt.update("update customer set firstname =?,email=? where customerId = ?",customer.getFirstName(),customer.getEmail(),customer.getCustomerId());
		return customer;
	}
}