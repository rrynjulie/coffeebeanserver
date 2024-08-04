select
    userId, userName, password, role, regDate, reliability, nickName, email
from user order by userId DESC;

show columns from user;

show tables ;

ALTER TABLE user DROP column birthday;

delete from user;
