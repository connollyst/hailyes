package com.quane.hail.yes.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.quane.hail.yes.user.User;

public class UserList {

	private static Map<UUID, User> users = Collections
			.synchronizedMap(new HashMap<UUID, User>());

	public UserList() {

	}

	public void add(User user) {
		if (user.getId() == null) {
			user.setId(UUID.randomUUID());
		}
		users.put(user.getId(), user);
	}

	public void remove(User user) {
		users.remove(user.getId());
	}

	public int size() {
		return users.size();
	}
}
