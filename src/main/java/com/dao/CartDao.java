package com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bean.CartBean;

@Repository
public class CartDao {
	
	@Autowired
	JdbcTemplate stmt;
	
	public void addCart(CartBean cart) {
		stmt.update("insert into cart(customerid,productid) values (?,?)",cart.getCustomerId(),cart.getProductId());	
		
	}

	public List<CartBean> getMyCart(int customerId) {

		return stmt.query("select p.productid,c.customerid,ct.cartid, p.name,p.price from product p,customer c,cart ct where ct.customerId = c.customerId and ct.productId = p.productId and c.customerId = ?",new BeanPropertyRowMapper<CartBean>(CartBean.class),customerId);
	}

}
