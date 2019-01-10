package com.niit.Dao;

import java.util.List;

import com.niit.Models.Friend;
import com.niit.Models.User;

public interface FriendDao 
{
	List<User> getAllSuggestedUsers(String email);

	void friendRequest(Friend friend);
	List<Friend> pendingRequests(String email);//email is email id of logged in user
    void acceptFriendRequest(Friend friend);
    void deleteFriendRequest(Friend friend);
    List<User> listOfFriends(String email);


	
	
	

}
