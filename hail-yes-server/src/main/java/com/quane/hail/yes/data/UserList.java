package com.quane.hail.yes.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.quane.hail.yes.user.BasicUser;

public class UserList {

	private static Map<UUID, BasicUser> users = Collections
			.synchronizedMap(new HashMap<UUID, BasicUser>());

	public UserList() {

	}

	public void add(BasicUser user) {
		if (user.getId() == null) {
			user.setId(UUID.randomUUID());
		}
		users.put(user.getId(), user);
	}

	public void remove(BasicUser user) {
		users.remove(user.getId());
	}

	public int size() {
		return users.size();
	}
}
