CREATE TABLE facebook_post (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               post_url TEXT NOT NULL,
                               content TEXT,               -- Nội dung bài viết
                               image_url TEXT,             -- Link ảnh trong bài
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE facebook_comment (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  post_id BIGINT NOT NULL,    -- Liên kết tới bảng bài viết
                                  commenter_name VARCHAR(255),-- Tên người bình luận (nếu có thể cào)
                                  comment_text TEXT,          -- Nội dung bình luận
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (post_id) REFERENCES facebook_post(id) ON DELETE CASCADE
);

