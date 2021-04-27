# EsportsTalk
Instructions:

To view all items in search activity, click search on an empty string, and all items will be shown (posts, communities, users)

To access a post, click on the post title, or search for a post in the search activity

To access a community, click on the community part (under title) of a post, or search for a community in the search activity

To make a new post, navigate to a community and click the plus button


Users are not fully implemented at the moment

**Note: when making an image post or video post, if image/ video are not provided or an invalid link is provided, it defaults to a text post

Features Implemented in UI:

- Firebase database has been fully implemented, no further features to add at this moment to the database, only adding more communities manually
- Search for posts, communities, and users
	- Can click on posts or communities currently to view the corresponding post or 
	Community..user community not implemented
- Make a new post when visiting a community
	- Can upload an image, either from camera or from gallery
	- Can place a Youtube video link (note: other video links are not valid)
	- Can place a
- Can view all posts (albeit not in the correct order at the moment)
- Can view a community, and the posts inside it (and make a new post)
- Can comment on posts
- Click on the title of a post to go to Post Activity, where comments can be made
	

Features Implemented,  but not in UI/visible yet:

- Create a new user
- Load posts by user followed tags



Features to be implemented:
- Login/sign up
- Settings Fragment
- User Activity/Fragment
- Rotation
- Testing
- Saving to local database
- Better design/UI
- Dark mode / auto dark mode
- Help buttons
- main activity with fragments for settings, users, and posts
- ability to travel to communities, users from the post
- ability to follow communities
- Add buttons to return to home from search

Features that might be implemented:
- Special post types for different communities depending on need (maybe)
- Creating a new community
- Deleting a post
- Content filter
- Like button
- Encrypt Passwords

**NOTE: As of right now, there is no automatically reloading from the database. So, if you add a new post, or a new comment, you will need to reload posts to properly be able to view them. Comments are currently loaded with posts, so that’s why a reload needs to be for done that as well, but this might change in the future

Also, uploading an image can be slower than uploading the rest of the post, but this will not break things.

Right now, all posts are done under a single username, this will change in the near future.


