package com.niit.Dao;

import java.util.List;

import com.niit.Models.BlogComment;

public interface BlogCommentDao 
{
	void addBlogComment(BlogComment blogComment);
	List<BlogComment> getAllBlogComments(int blogPostId);
	

}
