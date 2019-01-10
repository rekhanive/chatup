package com.niit.Dao;

import java.util.List;

import com.niit.Models.BlogPost;

public interface BlogPostDao 
{
	void addBlogPost(BlogPost blogPost);
	List<BlogPost> getBlogsWaitingForApproval();
	List<BlogPost> getBlogsApproved();
	BlogPost getBlog(int blogId);
	void approveBlogPost(int blogPostId);
	void updateBlogPost(BlogPost blogPost);
	void rejectblogPost(BlogPost blogPost);

}
