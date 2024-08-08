USE dife_local;

SELECT * FROM member;
SELECT * FROM notification_token;
SELECT * FROM post;
SELECT * FROM comment;
SELECT * FROM post_likes;
SELECT * FROM comment_likes;
SELECT * FROM language;
SELECT * FROM hobby;
SELECT * FROM bookmark;
SELECT * FROM chatroom;
SELECT * FROM chatroom_setting;
SELECT * FROM chatroom_member;
SELECT * FROM group_purpose;

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, COUNTRY, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('1', 'poream3387@kookmin.ac.kr', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', 'poream', '이승호', '20181663', '소프트웨어학과', 'KO', 1, 'INTJ', 'ADMIN', 'hiiii', 1);

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, COUNTRY, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('2', '211_0@kookmin.ac.kr', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', '211_0', '하은영','20210298', '소프트웨어학과', 'KO', 1, 'INTJ', 'ADMIN', 'hiiii', 1);

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, COUNTRY, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('3', 'stgood@kookmin.ac.kr', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', 'stgood', '구수연', '20211863', '소프트웨어학과', 'KO', 1, 'ENTJ', 'ADMIN', 'hiiii', 1);

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, COUNTRY, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('4', 'syr820@kookmin.ac.kr', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', 'syr820', '서예린', '20221575', '공업디자인학과', 'KO', 1, 'ENFJ', 'ADMIN', 'hiiii', 1);

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, COUNTRY, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('5', 'gusuyeon23@gmail.com', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', 'sooya', '구수연', '20211863', '소프트웨어학과', 'US', 1, 'ENTJ', 'ADMIN', 'Backend Suyeon Test account', 1);

INSERT INTO language(ID, MEMBER_ID, NAME)
VALUES ('1', '5', 'KOREAN');
INSERT INTO language(ID, MEMBER_ID, NAME)
VALUES ('2', '5', 'ENGLISH');
INSERT INTO language(ID, MEMBER_ID, NAME)
VALUES ('3', '5', 'SPANISH');

INSERT INTO language(ID, MEMBER_ID, NAME)
VALUES ('4', '2', 'ENGLISH');
INSERT INTO language(ID, MEMBER_ID, NAME)
VALUES ('5', '3', 'SPANISH');

INSERT INTO hobby(ID, MEMBER_ID, NAME)
VALUES ('1', '5', 'SOCCER');
INSERT INTO hobby(ID, MEMBER_ID, NAME)
VALUES ('2', '5', 'BASEBALL');
INSERT INTO hobby(ID, MEMBER_ID, NAME)
VALUES ('3', '5', 'MUKBANG');
INSERT INTO hobby(ID, MEMBER_ID, NAME)
VALUES ('4', '5', 'SOCCER');

INSERT INTO notification_token(ID, MEMBER_ID, DEVICE_ID, PUSH_TOKEN)
VALUES ('1', '1', 'XXXX-XXXX-XXXX', 'pushToken1');
INSERT INTO notification_token(ID, MEMBER_ID, DEVICE_ID, PUSH_TOKEN)
VALUES ('2', '2', 'XXXX-XXXX-XXXX', 'pushToken2');
INSERT INTO notification_token(ID, MEMBER_ID, DEVICE_ID, PUSH_TOKEN)
VALUES ('3', '3', 'XXXX-XXXX-XXXX', 'pushToken3');
INSERT INTO notification_token(ID, MEMBER_ID, DEVICE_ID, PUSH_TOKEN)
VALUES ('4', '4', 'XXXX-XXXX-XXXX', 'pushToken4');
INSERT INTO notification_token(ID, MEMBER_ID, DEVICE_ID, PUSH_TOKEN)
VALUES ('5', '5', 'XXXX-XXXX-XXXX', 'pushToken5');

INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('1', 1, '5', 'FREE 공개 게시글 테스트 1', '게시글 테스트 내용', 'FREE', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('2', 1, '5', 'FREE 공개 게시글 테스트 2', '게시글 테스트 내용', 'FREE', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('3', 0, '5', 'FREE 비공개 게시글 테스트 3', '게시글 테스트 내용', 'FREE', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('4', 0, '3', 'FREE 비공개 게시글 테스트 4', '게시글 테스트 내용', 'FREE', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('5', 1, '4', 'FREE 공개 게시글 테스트 5', '게시글 테스트 내용', 'FREE', NOW());

INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('6', 1, '2', 'TIP 공개 게시글 테스트 1', '게시글 테스트 내용', 'TIP', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('7', 0, '1', 'TIP 비공개 게시글 테스트 2', '게시글 테스트 내용', 'TIP', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('8', 1, '5', 'TIP 공개 게시글 테스트 3', '게시글 테스트 내용', 'TIP', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('9', 0, '5', 'TIP 비공개 게시글 테스트 4', '게시글 테스트 내용', 'TIP', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('10', 1, '5', 'TIP 공개 게시글 테스트 5', '게시글 테스트 내용', 'TIP', NOW());

INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, CONTENT, CREATED)
VALUES ('1', 1, '5', '1', '공개 댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, CONTENT, CREATED)
VALUES ('2', 1, '5', '1', '공개 댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, CONTENT, CREATED)
VALUES ('3', 0, '5', '1', '비공개 댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, CONTENT, CREATED)
VALUES ('4', 0, '5', '2', '비공개 댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, CONTENT, CREATED)
VALUES ('5', 1, '5', '3', '공개 댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, CONTENT, CREATED)
VALUES ('6', 1, '5', '4', '공개 댓글 내용', NOW());

INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, PARENT_ID, CONTENT, CREATED)
VALUES ('7', 1, '5', '1', '1', '공개 대댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, PARENT_ID, CONTENT, CREATED)
VALUES ('8', 1, '5', '1', '1', '공개 대댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, PARENT_ID, CONTENT, CREATED)
VALUES ('9', 0, '5', '1', '1', '비공개 대댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, PARENT_ID, CONTENT, CREATED)
VALUES ('10', 0, '5', '1', '2', '비공개 대댓글 내용', NOW());

INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, PARENT_ID, CONTENT, CREATED)
VALUES ('11', 0, '5', '2', '1', '비공개 대댓글 내용', NOW());
INSERT INTO comment (ID, IS_PUBLIC, WRITER_ID, POST_ID, PARENT_ID, CONTENT, CREATED)
VALUES ('12', 1, '5', '2', '1', '비공개 대댓글 내용', NOW());

INSERT INTO post_likes(ID, MEMBER_ID, POST_ID, CREATED)
VALUES ('1', '5', '1', NOW());
INSERT INTO post_likes(ID, MEMBER_ID, POST_ID, CREATED)
VALUES ('2', '4', '1', NOW());
INSERT INTO post_likes(ID, MEMBER_ID, POST_ID, CREATED)
VALUES ('3', '2', '1', NOW());
INSERT INTO post_likes(ID, MEMBER_ID, POST_ID, CREATED)
VALUES ('4', '5', '2', NOW());

INSERT INTO comment_likes(ID, MEMBER_ID, COMMENT_ID, CREATED)
VALUES ('1', '5', '1', NOW());
INSERT INTO comment_likes(ID, MEMBER_ID, COMMENT_ID, CREATED)
VALUES ('2', '5', '3', NOW());
INSERT INTO comment_likes(ID, MEMBER_ID, COMMENT_ID, CREATED)
VALUES ('3', '5', '7', NOW());
INSERT INTO comment_likes(ID, MEMBER_ID, COMMENT_ID, CREATED)
VALUES ('4', '2', '1', NOW());

INSERT INTO bookmark (ID, POST_ID, MEMBER_ID, CREATED)
VALUES ('1', '1', '5', NOW());
INSERT INTO bookmark (ID, POST_ID, MEMBER_ID, CREATED)
VALUES ('2', '1', '2', NOW());


INSERT INTO chatroom(ID, NAME, CHATROOM_TYPE, MANAGER_ID, CREATED)
VALUES ('1', 'name', 0, '5', NOW());
INSERT INTO chatroom_setting(ID, IS_PUBLIC, DESCRIPTION)
VALUES ('1', 0, 'description');

INSERT INTO chatroom_member(CHATROOM_ID, MEMBER_ID)
VALUES ('1', '5');
INSERT INTO chatroom_member(CHATROOM_ID, MEMBER_ID)
VALUES ('1', '3');

UPDATE chatroom
SET CHATROOM_SETTING_ID = 1
WHERE ID = 1;

INSERT INTO language(ID,CHATROOM_SETTING_ID, NAME)
VALUES ('6', '1', 'SPANISH');
INSERT INTO group_purpose(ID,CHATROOM_SETTING_ID, NAME)
VALUES ('1', '1', 'COMMUNICATION');
