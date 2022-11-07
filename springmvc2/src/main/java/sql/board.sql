create table board(
    num int primary key,   --게시글번호. 기본키.
    writer varchar2(30),   --글작성자이름
    pass varchar2(20),     --글 비밀번호. 
    subject varchar2(100), --제목
    content varchar2(4000),--내용
    file1 varchar2(100),   --첨부파일명
    boardid varchar2(1),   --게시판종류. 1:공지사항, 2:자유게시판, 3:QNA    
    regdate date,          --등록일시
    ip varchar2(20),       --작성자의 컴퓨터의 IP값
    readcnt number(10),    --조회수. 상세보기시 1씩 증가
    grp int,               --답변글시 원글의 게시글번호 
    grplevel number(3),    --답변글의 레벨. 원글 + 1
    grpstep number(5)    --글 그룹의 순서 원글 + 1, 기존의 내용이 있는 경우 +1
);
select * from board