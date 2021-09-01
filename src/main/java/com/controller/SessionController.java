package com.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.ResponseBean;
import com.bean.UserBean;
import com.dao.UserDao;

@RestController
public class SessionController {
	@Autowired
	UserDao userDao;

	@GetMapping("/authenticate")
	public String authenticate() {
		return "royaleducation";
	}

	@PostMapping("/users")
	public ResponseBean<UserBean> signup(UserBean user) {
		userDao.insertUser(user);
		ResponseBean<UserBean> res = new ResponseBean<>();
		res.setStatus(200);
		res.setMessage("signup done");
		res.setData(user);
		return res;//
	}

	@PutMapping("/users")
	public ResponseBean<UserBean> udpateUser(UserBean user) {
		user = userDao.updateUser(user);
		ResponseBean<UserBean> res = new ResponseBean<>();

		if (user == null) {
			res.setStatus(-1);
			res.setMessage("invalid userId");
		} else {
			res.setStatus(200);
			res.setMessage("user updated...");
			res.setData(user);
		}

		return res;//
	}

	@GetMapping("/users")
	public ResponseBean<ArrayList<UserBean>> getUsers() {

		ResponseBean<ArrayList<UserBean>> res = new ResponseBean<>();
		res.setData(userDao.getAllUsers());
		res.setMessage("users retrived");
		res.setStatus(200);
		return res;
	}

	@GetMapping("/users/{userId}")
	public ResponseBean<UserBean> getUserById(@PathVariable("userId") int userId) {
		UserBean user = userDao.getUserById(userId);
		ResponseBean<UserBean> res = new ResponseBean<>();

		if (user == null) {
			res.setStatus(-1);//
			res.setMessage("Invalid UserId");
		} else {
			res.setStatus(200);
			res.setData(user);
			res.setMessage("User Retrived");
		}
		return res;
	}
}
