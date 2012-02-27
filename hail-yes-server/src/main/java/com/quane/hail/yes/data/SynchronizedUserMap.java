package com.quane.hail.yes.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.quane.hail.yes.user.User;

/**
 * 
 * @author Sean Connolly
 */
public class SynchronizedUserMap {

	private static final Map<UUID, User> users = Collections
			.synchronizedMap(new HashMap<UUID, User>());

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

	public List<User> getList() {
		return new ArrayList<User>(users.values());
	}

}
