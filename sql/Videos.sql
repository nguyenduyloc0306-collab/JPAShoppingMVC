-- Bảng Videos: 1 User - n Video (user_id), mỗi video có thể thuộc 1 Category (cate_id).
-- Có thể bỏ qua file này vì hibernate.hbm2ddl.auto=update tự tạo bảng / thêm cột khi chạy ứng dụng.
-- Script chạy lại nhiều lần không lỗi.
USE JPAShopping;
GO

IF OBJECT_ID(N'dbo.Videos', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Videos(
        videoId     nvarchar(255) NOT NULL CONSTRAINT PK_Videos PRIMARY KEY,
        title       nvarchar(255) NULL,
        url         nvarchar(500) NULL,          -- link YouTube hoặc link trực tiếp .mp4/.webm/.ogg
        poster      nvarchar(255) NULL,          -- tên file ảnh trong thư mục upload
        description nvarchar(max) NULL,
        views       int           NOT NULL CONSTRAINT DF_Videos_views  DEFAULT 0,
        active      bit           NOT NULL CONSTRAINT DF_Videos_active DEFAULT 1,
        createddate datetime2     NULL,
        cate_id     int           NULL CONSTRAINT FK_Videos_Category REFERENCES dbo.Category(cate_id),
        user_id     int           NULL CONSTRAINT FK_Videos_Users    REFERENCES dbo.Users(id)
    );
END
GO

-- Bảng đã có sẵn (do Hibernate tạo trước đó): chỉ bổ sung các cột còn thiếu
IF COL_LENGTH(N'dbo.Videos', N'url')         IS NULL ALTER TABLE dbo.Videos ADD url         nvarchar(500) NULL;
IF COL_LENGTH(N'dbo.Videos', N'createddate') IS NULL ALTER TABLE dbo.Videos ADD createddate datetime2     NULL;
IF COL_LENGTH(N'dbo.Videos', N'user_id')     IS NULL ALTER TABLE dbo.Videos ADD user_id     int           NULL;
GO

-- Khóa ngoại user_id đặt ở batch riêng, sau khi chắc chắn cột đã tồn tại
IF NOT EXISTS (SELECT 1 FROM sys.foreign_keys
               WHERE name = N'FK_Videos_Users' AND parent_object_id = OBJECT_ID(N'dbo.Videos'))
   AND NOT EXISTS (SELECT 1 FROM sys.foreign_key_columns fkc
                   WHERE fkc.parent_object_id = OBJECT_ID(N'dbo.Videos')
                     AND fkc.parent_column_id = COLUMNPROPERTY(OBJECT_ID(N'dbo.Videos'), N'user_id', 'ColumnId'))
    ALTER TABLE dbo.Videos ADD CONSTRAINT FK_Videos_Users FOREIGN KEY (user_id) REFERENCES dbo.Users(id);
GO

-- (Tùy chọn) Video cũ chưa có chủ sở hữu thì không hiện ở /video của ai. Gán cho một user nếu cần:
-- UPDATE dbo.Videos SET user_id = (SELECT id FROM dbo.Users WHERE username = N'user') WHERE user_id IS NULL;
