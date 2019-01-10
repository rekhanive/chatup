package com.niit.DaoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.niit.Dao.BlogCommentDao;
import com.niit.Models.BlogComment;

@Repository
@Transactional
public class BlogCommentDaoImpl implements BlogCommentDao
{
	@Autowired
	private SessionFactory sessionFactory;
		public void addBlogComment(BlogComment blogComment) {
			Session session=sessionFactory.getCurrentSession();
			session.save(blogComment);
		}
		public List<BlogComment> getAllBlogComments(int blogPostId) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery("from BlogComment where blogPost.blogPostId=:blogId");
		query.setInteger("blogId", blogPostId);
		List<BlogComment> comments=query.list();
		return comments;
		}
	

}
