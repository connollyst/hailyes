package com.quane.hail.yes.data;

import java.util.List;

import com.quane.hail.yes.HailLocation;
import com.quane.hail.yes.user.BasicUser;

public interface IDataAccessObject {

	public List<BasicUser> getUsersNearLocation(HailLocation location);

	public BasicUser saveUserLocation(BasicUser user);

	public void removeUserLocation(BasicUser user);
	
}
