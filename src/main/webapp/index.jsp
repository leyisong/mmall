<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<body>
<h2>Hello World!</h2>
springMVC文件上传
<form action="/manage/product/upload.do" name="form1" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springMVC文件上传"/>
</form>

富文本文件上传
<form action="/manage/product/richtest_img_upload.do" name="form2" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="springMVC文件上传"/>
</form>
</body>
</html>
