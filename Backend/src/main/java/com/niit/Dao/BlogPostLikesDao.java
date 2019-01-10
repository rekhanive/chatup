package com.niit.Dao;

import com.niit.Models.BlogPost;
import com.niit.Models.BlogPostLikes;

public interface BlogPostLikesDao 
{
	BlogPostLikes hasUserLikedBlogPost(int blogPostId,String email);//email is loggedin user's email id

	BlogPost updateLikes(int blogPostId, String email);
	
	
	

}
