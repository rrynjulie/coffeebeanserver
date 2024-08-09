select
    userId, userName, password, role
from user order by userId DESC;

SELECT * FROM attachment ORDER BY attachmentId DESC;

SELECT * FROM authority ORDER BY authorityId DESC;

SELECT * FROM car ORDER BY carId DESC;

SELECT * FROM chat_room ORDER BY chatRoomId DESC;
ALTER TABLE chat_room DROP column isJoin;

SELECT * FROM dealing_type ORDER BY dealingTypeId DESC;

SELECT * FROM dips ORDER BY dipsId DESC;

SELECT * FROM message ORDER BY messageId DESC;

SELECT * FROM post ORDER BY postId DESC;

SELECT * FROM product ORDER BY productId DESC;

SELECT * FROM property ORDER BY propertyId DESC;

SELECT * FROM quit ORDER BY quitId DESC;

SELECT * FROM review ORDER BY reviewId DESC;

SELECT * FROM sample_review ORDER BY sampleReviewId DESC;

SHOW COLUMNS FROM car WHERE Field = 'status';

INSERT INTO product (userId, name, description, price, dealingStatus, category1, category2, category3, status, dealingType, desiredArea, regDate)
VALUES (3, '아이폰15Pro', '상태 좋은 아이폰입니다. 아내 건데 사용하지 않는 것 같아 싸게 내놓습니다!', 20000, '판매중', '모바일/태블릿', '스마트폰', '애플', '중고', '직거래', '한남동', now());
