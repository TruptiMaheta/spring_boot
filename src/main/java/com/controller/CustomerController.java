package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.CustomerBean;
import com.bean.LoginBean;
import com.bean.ResponseBean;
import com.dao.CustomerDao;
import com.util.TokenGenerator;

@RestController
@RequestMapping("/api/")
public class CustomerController {
	@Autowired
	CustomerDao customerDao;

	@Autowired
	TokenGenerator tokenGenerator;

	@PostMapping("/addcustomers")
	public ResponseBean<CustomerBean> addCustomer(@RequestBody CustomerBean customer) {

		ResponseBean<CustomerBean> res = new ResponseBean<>();
		customerDao.addCustomer(customer);

		res.setData(customer);
		res.setMessage("customer save");
		res.setStatus(200);

		return res;
	}

	@PostMapping("/authenticate")
	public ResponseBean<CustomerBean> authenticate(@RequestBody LoginBean login) {
		ResponseBean<CustomerBean> res = new ResponseBean<>();

		CustomerBean customer = customerDao.authenticate(login.getEmail(), login.getPassword());
		if (customer == null) {
			res.setStatus(-1);
			res.setData(customer);
			res.setMessage("Invalid Credentials");
		} else {
			String token = tokenGenerator.generateToken();
			customer.setToken(token);
			customerDao.updateToken(customer.getCustomerId(), token);
			res.setData(customer);
			res.setStatus(200);
			res.setMessage("authentication done");
		}

		return res;
	}

	@GetMapping("/getcustomerbytoken/{token}")
	public ResponseBean<CustomerBean> getCustomerByToken(@PathVariable("token") String token) {

		ResponseBean<CustomerBean> res = new ResponseBean<>();

		CustomerBean customer = customerDao.getCustomerByToken(token);

		if (customer == null) {
			res.setStatus(-1);
			res.setData(customer);
			res.setMessage("Invalid token");
		} else {
			res.setData(customer);
			res.setStatus(200);
			res.setMessage("customer retrieved...");
		}
		return res;
	}

	@GetMapping("/showcustomers")
	public ResponseBean<List<CustomerBean>> getUsers() {

		ResponseBean<List<CustomerBean>> res = new ResponseBean<>();
		List<CustomerBean> customer = customerDao.getCustomer();

		res.setData(customer);
		res.setMessage("customers retrived");
		res.setStatus(200);
		return res;
	}

	@DeleteMapping("/customers/{customerId}")
	public ResponseBean<CustomerBean> deleteProdcutById(@PathVariable("customerId") int customerId) {

		ResponseBean<CustomerBean> res = new ResponseBean<>();

		CustomerBean customer = customerDao.deleteCustomerById(customerId);

		if (customer == null) {
			res.setData(null);
			res.setMessage("Invalid customer Id");
			res.setStatus(-1);
		} else {
			res.setData(customer);
			res.setMessage("customer removed");
			res.setStatus(200);
		}
		return res;
	}

	@GetMapping("/customers/{customerId}")
	public ResponseBean<CustomerBean> getCustomerById(@PathVariable("customerId") int customerId) {

		CustomerBean customer = customerDao.getCustomerById(customerId);
		ResponseBean<CustomerBean> res = new ResponseBean<>();

		if (customer == null) {
			res.setStatus(-1);
			res.setMessage("invalid Id");
		} else {
			res.setStatus(200);
			res.setMessage("user retrived");
			res.setData(customer);
		}
		return res;

	}

	@PutMapping("/updatecustomers")
	public ResponseBean<CustomerBean> updateUser(@RequestBody CustomerBean customer,
			@RequestHeader("authToken") String authToken) {

		ResponseBean<CustomerBean> res = new ResponseBean<>();
		System.out.println(authToken);
		if (customerDao.getCustomerByToken(authToken) == null) {
			res.setStatus(-1);
			res.setMessage("invalid authorization");
			res.setData(null);
			return res;
		} else {
			customer.setToken(authToken);
			customer = customerDao.updateCustomer(customer);
			if (customer == null) {
				res.setStatus(-1);
				res.setMessage("invalid token");
			} else {

				res.setStatus(200);
				res.setMessage("user updated");
				res.setData(customer);
			}
			return res;
		}
	}
	
	@PutMapping("/updatecustomerbyId/{customerId}")
	public ResponseBean<CustomerBean> updateUser(@RequestBody CustomerBean customer,
			@PathVariable("customerId")int customerId) {

		ResponseBean<CustomerBean> res = new ResponseBean<>();
		
		if (customerDao.getCustomerById(customerId) == null) {
			res.setStatus(-1);
			res.setMessage("invalid authorization");
			res.setData(null);
			return res;
		} else {
			customer.setCustomerId(customerId);
			
			customer = customerDao.updateCustomerById(customer);
			if (customer == null) {
				res.setStatus(-1);
				res.setMessage("invalid Id");
			} else {

				res.setStatus(200);
				res.setMessage("user updated");
				res.setData(customer);
			}
			return res;
		}
	}
}
