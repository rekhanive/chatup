package com.niit.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.niit.Dao.BlogPostDao;
import com.niit.Dao.NotificationDao;
import com.niit.Dao.UserDao;
import com.niit.Models.BlogPost;
import com.niit.Models.ErrorClass;
import com.niit.Models.Notification;
import com.niit.Models.User;

@Controller
public class BlogPostController 
{
	@Autowired
	private BlogPostDao blogPostDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private NotificationDao  notificationDao;
		// input  : {'blogTitle':'..','blogContent':'.....'}
		@RequestMapping(value="/addblogpost",method=RequestMethod.POST)
		public ResponseEntity<?> addBlogPost(@RequestBody BlogPost blogPost,HttpSession session){
			String email=(String)session.getAttribute("email");
			//NOT LOGGED IN
			if(email==null){
				ErrorClass ErrorClass=new ErrorClass(6,"Please login...");
	    		return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);//login.html
			}
			blogPost.setPostedOn(new Date());
			User postedBy=userDao.getUser(email);
			blogPost.setPostedBy(postedBy);
			try{
			blogPostDao.addBlogPost(blogPost);
			}catch(Exception e){
				ErrorClass ErrorClass=new ErrorClass(8,"Unable to save blog post details.." + e.getMessage());
				return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
		}
		@RequestMapping(value="/blogswaitingforapproval",method=RequestMethod.GET)
		public ResponseEntity<?> getBlogsWaitingForApproval(HttpSession session){
			String email=(String)session.getAttribute("email");
			//NOT LOGGED IN
			if(email==null){
				ErrorClass ErrorClass=new ErrorClass(6,"Please login...");
	    		return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);//login.html
			}
			//ROLE - AUTHORIZATION
			User user=userDao.getUser(email);
			if(!user.getRole().equals("ADMIN")){
				ErrorClass ErrorClass=new ErrorClass(9,"You are not authorized to view the content..");
				return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);
			}
			List<BlogPost> blogsWaitingForApproval=blogPostDao.getBlogsWaitingForApproval();
			return new ResponseEntity<List<BlogPost>>(blogsWaitingForApproval,HttpStatus.OK);
		}
		@RequestMapping(value="/blogsapproved",method=RequestMethod.GET)
		public ResponseEntity<?> getBlogsApproved(HttpSession session){
			String email=(String)session.getAttribute("email");
			//NOT LOGGED IN
			if(email==null){
				ErrorClass ErrorClass=new ErrorClass(6,"Please login...");
	    		return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);//login.html
			}
			
			List<BlogPost> blogsApproved=blogPostDao.getBlogsApproved();
			return new ResponseEntity<List<BlogPost>>(blogsApproved,HttpStatus.OK);
		}
		@RequestMapping(value="/getblog/{blogId}",method=RequestMethod.GET)
		public ResponseEntity<?> getBlog(@PathVariable int blogId,HttpSession session){
			String email=(String)session.getAttribute("email");
			//NOT LOGGED IN
			if(email==null){
				ErrorClass ErrorClass=new ErrorClass(6,"Please login...");
	    		return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);//login.html
			}
			BlogPost blogPost=blogPostDao.getBlog(blogId);
			return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
		}
		@RequestMapping(value="/approveblogpost/{blogPostId}",method=RequestMethod.PUT)
		public ResponseEntity<?> approveBlogPost(@PathVariable int blogPostId,HttpSession session){
			String email=(String)session.getAttribute("email");
			//NOT LOGGED IN
			if(email==null){
				ErrorClass ErrorClass=new ErrorClass(6,"Please login...");
	    		return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);//login.html
			}
			//ROLE - AUTHORIZATION
			User user=userDao.getUser(email);
			if(!user.getRole().equals("ADMIN")){
				ErrorClass ErrorClass=new ErrorClass(9,"You are not authorized to view the content..");
				return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);
			}
			blogPostDao.approveBlogPost(blogPostId);
			
			BlogPost blogPost=blogPostDao.getBlog(blogPostId);
			Notification notification=new Notification();
			notification.setBlogTitle(blogPost.getBlogTitle());
			notification.setStatus("Approved");//value of status is "Approved"  or "Rejected"
			notification.setUserToBeNotified(blogPost.getPostedBy().getEmail());
			notificationDao.addNotification(notification);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		
		@RequestMapping(value="/rejectblogpost/{rejectionReason}",method=RequestMethod.PUT)
		public ResponseEntity<?> rejectBlogPost(@PathVariable String rejectionReason,@RequestBody BlogPost blogPost, HttpSession session){
			String email=(String)session.getAttribute("email");
			//NOT LOGGED IN
			if(email==null){
				ErrorClass ErrorClass=new ErrorClass(6,"Please login...");
	    		return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);//login.html
			}
			//ROLE - AUTHORIZATION
			User user=userDao.getUser(email);
			if(!user.getRole().equals("ADMIN")){
				ErrorClass ErrorClass=new ErrorClass(9,"You are not authorized to view the content..");
				return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);
			}
			blogPostDao.rejectblogPost(blogPost);
			
			Notification notification=new Notification();
			notification.setBlogTitle(blogPost.getBlogTitle());
			notification.setStatus("Rejected");
			notification.setUserToBeNotified(blogPost.getPostedBy().getEmail());
			notification.setRejectionReason(rejectionReason);
			notificationDao.addNotification(notification);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		@RequestMapping(value="/updateblogpost",method=RequestMethod.PUT)
		public ResponseEntity<?> updateBlogPost(@RequestBody BlogPost blogPost,HttpSession session){
			String email=(String)session.getAttribute("email");
			//NOT LOGGED IN
			if(email==null){
				ErrorClass ErrorClass=new ErrorClass(6,"Please login...");
	    		return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);//login.html
			}
			//SAME USER WHO POSTED THE BLOGPOST IS TRYING TO UPDATE
			//blogPost.getPostedBy().getEmail  - author of the blogpost (email of the user who posted the blog)
			//email  - logged in user
			if(!blogPost.getPostedBy().getEmail().equals(email)){
				ErrorClass ErrorClass=new ErrorClass(9,"You are not authorized to update this blogpost");
				return new ResponseEntity<ErrorClass>(ErrorClass,HttpStatus.UNAUTHORIZED);
			}
			blogPostDao.updateBlogPost(blogPost);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}

}
