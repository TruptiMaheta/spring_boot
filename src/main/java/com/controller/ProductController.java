package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bean.CustomerBean;
import com.bean.ProductBean;
import com.bean.ResponseBean;
import com.dao.CustomerDao;
import com.dao.ProductDao;

@RestController
public class ProductController {
	@Autowired
	ProductDao productDao;

	@Autowired
	CustomerDao customerDao;

	@PostMapping("/products")
	public ResponseBean<ProductBean> addProduct(@RequestBody ProductBean product,
			@RequestHeader("authToken") String token) {

		ResponseBean<ProductBean> resp = new ResponseBean<>();

		if (customerDao.getCustomerByToken(token) == null) {
			resp.setStatus(-1);
			resp.setMessage("invalid authorization");
			resp.setData(product);
			return resp;
		} else {
			
			if (token == null) {

				resp.setStatus(-1);
				resp.setMessage("invalid authorization");
				resp.setData(product);
				return resp;
			} else {

				boolean status = productDao.insertProduct(product);
				if (status) {
					resp.setStatus(200);
					resp.setMessage("Product saved");
					resp.setData(product);

				} else {
					resp.setStatus(-1);
					resp.setMessage("Product not saved");
					resp.setData(product);
				}

				return resp;
			}
		}
	}

	@GetMapping("/products")
	public ResponseBean<List<ProductBean>> getAllProducts() {

		ResponseBean<List<ProductBean>> res = new ResponseBean<>();
		List<ProductBean> products = productDao.getAllProducts();

		res.setStatus(200);
		res.setMessage("Products Retrieved");
		res.setData(products);

		return res;
	}

	@DeleteMapping("/products/{productId}")
	public ResponseBean<ProductBean> deleteProdcutById(@PathVariable("productId") int productId) {

		ResponseBean<ProductBean> res = new ResponseBean<>();

		ProductBean product = productDao.deleteProductById(productId);

		if (product == null) {
			res.setData(null);
			res.setMessage("Invalid product Id");
			res.setStatus(-1);
		} else {
			res.setData(product);
			res.setMessage("Product removed");
			res.setStatus(200);
		}
		return res;
	}
}
