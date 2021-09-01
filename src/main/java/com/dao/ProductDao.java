package com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bean.ProductBean;
@Repository
public class ProductDao {
	@Autowired
	JdbcTemplate stmt;

	public boolean insertProduct(ProductBean product) {

		int i = stmt.update("insert into product (name,price,qty) values (?,?,?)", product.getName(),
				product.getPrice(), product.getQty());
		if (i == 0)
			return false;
		else
			return true;
	}

	public List<ProductBean> getAllProducts() {
		List<ProductBean> products =  stmt.query("select * from product",new BeanPropertyRowMapper<ProductBean>(ProductBean.class));
		return products;
	}

	public ProductBean deleteProductById(int productId) {
		ProductBean productBean = getProductById(productId);
		if(productBean != null) {
			stmt.update("delete from product where productId = ?",productId);
		}
		
		return productBean;
	}

	public ProductBean getProductById(int productId) {
		
		ProductBean product = null;
		try {
			product = stmt.queryForObject("select * from product where productId = ?", new BeanPropertyRowMapper<ProductBean>(ProductBean.class),productId);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return product;
	}
	
	
}
