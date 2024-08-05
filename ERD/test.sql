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