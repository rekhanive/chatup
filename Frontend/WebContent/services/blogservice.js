/**
 * 
 */
app.factory('BlogService',function($http){
	var blogService={}
	var BASE_URL="http://localhost:8081/Middleware"
		
	blogService.addBlog=function(blog){
		return $http.post(BASE_URL + "/addblogpost",blog)
	}
	
	blogService.blogsWaitingForApproval=function(){
		return $http.get(BASE_URL + "/blogswaitingforapproval")
	}
	
	blogService.blogsApproved=function(){
		return $http.get(BASE_URL + "/blogsapproved")
	}
	
	blogService.getBlog=function(blogId){
		return $http.get(BASE_URL + "/getblog/"+blogId)
	}
	blogService.approveBlogPost=function(blogPostId){
		return $http.put(BASE_URL + "/approveblogpost/"+blogPostId)
	}
	
    blogService.rejectBlog=function(blogPost,rejectionReason){
    	return $http.put(BASE_URL + "/rejectblogpost/"+rejectionReason,blogPost)
    }	
    blogService.hasUserLikedBlogPost=function(blogPostId){
    	return $http.get(BASE_URL + "/hasuserlikedblogpost/"+blogPostId)
    }
    
    blogService.updateLikes=function(blogPostId){
    	return $http.put(BASE_URL + "/updatelikes/"+blogPostId)
    }
    blogService.addBlogComment=function(blogComment){
    	return $http.post(BASE_URL + "/addblogcomment",blogComment)
    }
    
    blogService.getBlogComments=function(blogPostId){
    	return $http.get(BASE_URL + "/blogcomments/"+blogPostId)
    }
	return blogService
})