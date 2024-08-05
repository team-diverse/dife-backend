USE dife_local;

SELECT * FROM member;
SELECT * FROM post;

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, IS_KOREAN, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('1', 'poream3387@kookmin.ac.kr', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', 'poream', '이승호', '20181663', '소프트웨어학과', 1, 1, 'INTJ', 'ADMIN', 'hiiii', 1);

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, IS_KOREAN, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('2', '211_0@kookmin.ac.kr', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', '211_0', '하은영','20210298', '소프트웨어학과', 1, 1, 'INTJ', 'ADMIN', 'hiiii', 1);

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, IS_KOREAN, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('3', 'stgood@kookmin.ac.kr', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', 'stgood', '구수연', '20211863', '소프트웨어학과', 1, 1, 'ENTJ', 'ADMIN', 'hiiii', 1);

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, IS_KOREAN, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('4', 'syr820@kookmin.ac.kr', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', 'syr820', '서예린', '20221575', '공업디자인학과', 1, 1, 'ENFJ', 'ADMIN', 'hiiii', 1);

INSERT INTO member (ID, EMAIL, PASSWORD, USERNAME, NAME, STUDENT_ID, MAJOR, IS_KOREAN, IS_PUBLIC, MBTI, ROLE, BIO, IS_VERIFIED)
VALUES ('5', 'gusuyeon23@gmail.com', '$2a$10$Y/3KpS26JfwZl/.MCmpXd.n56NnFwfjkwaHQ5726.j69UQQ/gzgWi', 'sooya', '구수연', '20211863', '소프트웨어학과', 1, 1, 'ENTJ', 'ADMIN', 'Backend Suyeon Test account', 1);

INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('1', 1, '5', 'FREE 게시글 테스트 1', '게시글 테스트 내용', 'FREE', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('2', 1, '5', 'FREE 게시글 테스트 2', '게시글 테스트 내용', 'FREE', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('3', 1, '5', 'FREE 게시글 테스트 3', '게시글 테스트 내용', 'FREE', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('4', 1, '3', 'FREE 게시글 테스트 4', '게시글 테스트 내용', 'FREE', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('5', 1, '4', 'FREE 게시글 테스트 5', '게시글 테스트 내용', 'FREE', NOW());

INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('6', 1, '2', 'TIP 게시글 테스트 1', '게시글 테스트 내용', 'TIP', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('7', 1, '1', 'TIP 게시글 테스트 2', '게시글 테스트 내용', 'TIP', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('8', 1, '5', 'TIP 게시글 테스트 3', '게시글 테스트 내용', 'TIP', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('9', 1, '5', 'TIP 게시글 테스트 4', '게시글 테스트 내용', 'TIP', NOW());
INSERT INTO post (ID, IS_PUBLIC, MEMBER_ID, TITLE, CONTENT, BOARD_TYPE, CREATED)
VALUES ('10', 1, '5', 'TIP 게시글 테스트 5', '게시글 테스트 내용', 'TIP', NOW());
