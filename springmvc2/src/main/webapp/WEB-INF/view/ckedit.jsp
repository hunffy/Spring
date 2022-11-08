<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/springmvc2/src/main/webapp/WEB-INF/view/ckedit.jsp --%>    
<script>
window.parent.CKEDITOR.tools.callFunction
(${CKEditorFuncNum},'${fileName}','이미지 업로드 완료')
</script>