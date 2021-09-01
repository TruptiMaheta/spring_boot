package com.dao;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.bean.UserBean;

@Repository
public class UserDao {
	public ArrayList<UserBean> users = new ArrayList<>();

	public void insertUser(UserBean user) {
		users.add(user);
	}

	public ArrayList<UserBean> getAllUsers() {
		return users;
	}

	public UserBean getUserById(int userId) {

		for (UserBean user : users) {
			if (user.getUserId() == userId) {
				return user;
			}
		}

		return null;
	}

	public UserBean updateUser(UserBean userB) {
		int i = 0;
		for (i = 0; i < users.size(); i++) {
			if (users.get(i).getUserId() == userB.getUserId()) {
				break;
			}
		}
		if (i == users.size()) {
			return null;
		} else {
			users.get(i).setFirstName(userB.getFirstName());
			return users.get(i);
		}
	}

	
//	spring.jpa.hibernate.ddl-auto=create
//			spring.datasource.url=jdbc:mysql://localhost:3306/codejavadb
//			spring.datasource.username=root
//			spring.datasource.password=password
//			spring.jpa.properties.hibernate.format_sql=true
}
