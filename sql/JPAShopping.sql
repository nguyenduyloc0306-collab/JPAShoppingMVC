IF DB_ID('JPAShopping') IS NULL CREATE DATABASE JPAShopping;
GO
USE JPAShopping;
GO
CREATE TABLE Users(
  id int IDENTITY(1,1) PRIMARY KEY,
  email nvarchar(255) NOT NULL UNIQUE,
  username nvarchar(100) NOT NULL UNIQUE,
  fullname nvarchar(255) NULL,
  [password] nvarchar(255) NOT NULL,
  avatar nvarchar(255) NULL,
  roleid int NOT NULL,
  phone nvarchar(20) NULL,
  createddate date NULL
);
GO
CREATE TABLE Category(
  cate_id int IDENTITY(1,1) PRIMARY KEY,
  cate_name nvarchar(255) NOT NULL,
  icons nvarchar(255) NULL
);
GO
-- roleid: 1 = admin, 2 = manager, 5 = người dùng thường
INSERT INTO Users(email,username,fullname,[password],roleid,phone,createddate) VALUES
 (N'admin@gmail.com',  N'admin',   N'Quản trị viên', N'123456', 1, N'0900000001', GETDATE()),
 (N'manager@gmail.com',N'manager', N'Quản lý',       N'123456', 2, N'0900000002', GETDATE()),
 (N'user@gmail.com',   N'user',    N'Người dùng',    N'123456', 5, N'0900000003', GETDATE());
INSERT INTO Category(cate_name,icons) VALUES (N'Quần áo nam',N'avatar.png'),(N'Quần áo nữ',N'avatar.png');
GO

ALTER TABLE users ADD
    email        NVARCHAR(255) NULL,
    isActive     BIT           NOT NULL DEFAULT 0,
    otpHash      VARCHAR(64)   NULL,
    otpExpiry    DATETIME      NULL,
    otpAttempts  INT           NOT NULL DEFAULT 0;

-- Tài khoản cũ đã tồn tại thì coi như đã kích hoạt
UPDATE users SET isActive = 1 WHERE isActive = 0;

