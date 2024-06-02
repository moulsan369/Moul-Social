package com.ntu.moulsocial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "socialApp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_AVATAR = "user_avatar";

    private static final String TABLE_POSTS = "posts";
    private static final String COLUMN_POST_ID = "post_id";
    private static final String COLUMN_POST_USER_ID = "post_user_id";
    private static final String COLUMN_POST_CONTENT = "post_content";
    private static final String COLUMN_POST_IMAGE = "post_image";
    private static final String COLUMN_POST_LIKES = "post_likes";

    private static final String TABLE_COMMENTS = "comments";
    private static final String COLUMN_COMMENT_ID = "comment_id";
    private static final String COLUMN_COMMENT_POST_ID = "comment_post_id";
    private static final String COLUMN_COMMENT_USER_ID = "comment_user_id";
    private static final String COLUMN_COMMENT_CONTENT = "comment_content";

    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String COLUMN_NOTIFICATION_ID = "notification_id";
    private static final String COLUMN_NOTIFICATION_MESSAGE = "notification_message";

    private static final String TABLE_FRIENDS = "friends";  // Bảng mới cho bạn bè
    private static final String COLUMN_FRIEND_ID = "friend_id";
    private static final String COLUMN_FRIEND_NAME = "friend_name";
    private static final String COLUMN_FRIEND_AVATAR = "friend_avatar";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " TEXT PRIMARY KEY, " +
                COLUMN_USER_NAME + " TEXT, " +
                COLUMN_USER_AVATAR + " TEXT)";

        String createPostsTable = "CREATE TABLE " + TABLE_POSTS + " (" +
                COLUMN_POST_ID + " TEXT PRIMARY KEY, " +
                COLUMN_POST_USER_ID + " TEXT, " +
                COLUMN_POST_CONTENT + " TEXT, " +
                COLUMN_POST_IMAGE + " TEXT, " +
                COLUMN_POST_LIKES + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_POST_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        String createCommentsTable = "CREATE TABLE " + TABLE_COMMENTS + " (" +
                COLUMN_COMMENT_ID + " TEXT PRIMARY KEY, " +
                COLUMN_COMMENT_POST_ID + " TEXT, " +
                COLUMN_COMMENT_USER_ID + " TEXT, " +
                COLUMN_COMMENT_CONTENT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_COMMENT_POST_ID + ") REFERENCES " + TABLE_POSTS + "(" + COLUMN_POST_ID + "), " +
                "FOREIGN KEY(" + COLUMN_COMMENT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        String createNotificationsTable = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COLUMN_NOTIFICATION_ID + " TEXT PRIMARY KEY, " +
                COLUMN_NOTIFICATION_MESSAGE + " TEXT)";

        String createFriendsTable = "CREATE TABLE " + TABLE_FRIENDS + " (" +
                COLUMN_FRIEND_ID + " TEXT PRIMARY KEY, " +
                COLUMN_FRIEND_NAME + " TEXT, " +
                COLUMN_FRIEND_AVATAR + " TEXT)"; // Tạo bảng bạn bè

        db.execSQL(createUsersTable);
        db.execSQL(createPostsTable);
        db.execSQL(createCommentsTable);
        db.execSQL(createNotificationsTable);
        db.execSQL(createFriendsTable); // Thêm bảng bạn bè vào cơ sở dữ liệu
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS); // Xóa bảng bạn bè nếu tồn tại
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_AVATAR, user.getAvatarUri());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_AVATAR},
                COLUMN_USER_ID + "=?", new String[]{userId}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            User user = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            cursor.close();
            return user;
        } else {
            return null;
        }
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUserName());
        values.put(COLUMN_USER_AVATAR, user.getAvatarUri());
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{user.getUserId()});
    }

    public void addPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_ID, post.getId());
        values.put(COLUMN_POST_USER_ID, post.getUserId());
        values.put(COLUMN_POST_CONTENT, post.getContent());
        values.put(COLUMN_POST_IMAGE, post.getImageUri());
        values.put(COLUMN_POST_LIKES, post.getLikes());
        db.insert(TABLE_POSTS, null, values);
        db.close();
    }

    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_POSTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4));
                postList.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return postList;
    }

    public List<Post> getPostsByUser(String userId) {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_POSTS, null, COLUMN_POST_USER_ID + "=?", new String[]{userId},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Post post = new Post(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4));
                postList.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return postList;
    }

    public void updatePost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_POST_CONTENT, post.getContent());
        values.put(COLUMN_POST_IMAGE, post.getImageUri());
        values.put(COLUMN_POST_LIKES, post.getLikes());
        db.update(TABLE_POSTS, values, COLUMN_POST_ID + "=?", new String[]{post.getId()});
        db.close();
    }

    public void deletePost(String postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POSTS, COLUMN_POST_ID + "=?", new String[]{postId});
        db.close();
    }

    public void addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMMENT_ID, comment.getId());
        values.put(COLUMN_COMMENT_POST_ID, comment.getPostId());
        values.put(COLUMN_COMMENT_USER_ID, comment.getUserId());
        values.put(COLUMN_COMMENT_CONTENT, comment.getContent());
        db.insert(TABLE_COMMENTS, null, values);
        db.close();
    }

    public List<Comment> getCommentsByPost(String postId) {
        List<Comment> commentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMMENTS, null, COLUMN_COMMENT_POST_ID + "=?", new String[]{postId},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Comment comment = new Comment(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                commentList.add(comment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return commentList;
    }


    public void deleteComment(String commentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COMMENTS, COLUMN_COMMENT_ID + "=?", new String[]{commentId});
        db.close();
    }

    public void addNotification(Notification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_ID, notification.getId());
        values.put(COLUMN_NOTIFICATION_MESSAGE, notification.getMessage());
        db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
    }

    public List<Notification> getAllNotifications() {
        List<Notification> notificationList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTIFICATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Notification notification = new Notification(cursor.getString(0), cursor.getString(1));
                notificationList.add(notification);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notificationList;
    }

    public void addFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRIEND_ID, friend.getFriendId());
        values.put(COLUMN_FRIEND_NAME, friend.getFriendName());
        values.put(COLUMN_FRIEND_AVATAR, friend.getAvatarUri());
        db.insert(TABLE_FRIENDS, null, values);
        db.close();
    }

    public List<Friend> getAllFriends() {
        List<Friend> friendList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FRIENDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Friend friend = new Friend(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                friendList.add(friend);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return friendList;
    }
}

