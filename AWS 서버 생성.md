# AWS 서버 생성

## 명령어 정리

Java Eclipse와 Android Studio를 연동하여 Eclipse의 Tomcat 서버를 실행, Android Studio에서 URL을 통해 Json으로 받은 데이터를 Android App에 이용하였다.

Java 파일(java.war)과 Tomcat 서버를 AWS를 이용하여 띄워 상시 구동이 가능하도록 만든다.

***

1. Git Bash 실행

2. EC2 설정 시 만들었던 key pair를 이용하여 Amazon Linux에 접속

3. "keypair이름.pem" 

   > **`" "`코테이션 절대 빼먹지 말것**

4. pem 확장자 파일을 편의상 바탕화면에 위치

5. `$ cd Desktop/` 

   > Git Bash에서 pem 확장자 파일이 위치한바탕화면으로 이동

6. `$ ssh -i "keypair이름.pem" ec2-user@ec2~어쩌구 IP~.compute.amazonaws.com`

   > keypair로 아마존 리눅스 서버에 접속

7. 접속을 확인한 후 `exit`

8. `chmod 400 keypair.pem`

   > 다시 윈도우의 Git Bash창에서 keypair 파일의 권한을 변경

9. 다시 6번의 명령어를 통해 아마존 서버에 접속

10. `java -version`

    > Java가 설치되어 있는지 확인

11. `sudo yum install -y java-1.8.0-openjdk-devel.x86_64`

    > 설치되어있지 않다면 설치해주도록 한다. 

12. 10번 명령어를 통해 java 설치 여부를 확인

13. `exit`

    > 다시 Git Bash로 나가기

14. Tomcat 페이지에서 apachetomcat-9.0.xx.tar.gz 버전 파일을 다운

15. 마찬가지로 편의상 바탕화면에 다운받은 파일을 위치

16. `scp -i "keypair이름.pem" apache-tomcat-9.0.30.tar.gz ec2-user@ec2-**-***-**-***.ap-northeast-2.compute.amazonaws.com:~/`

    > 아마존 서버로 윈도우에서 파일을 복사하는 명령어
    >
    > `:~/`는 파일이 복사될 경로를 뜻한다.

17. 6번의 명령어로 다시 아마존 서버에 접속한다.

18. `ls` 명령어로 tomcat이 잘 복사되었는지 확인

19. `tar xvfz apache-tomcat-9.0.30.tar.gz`

    > tomcat 파일을 압축 해제
    >
    > mkdir을 통해 만든 파일에 해제할 수 있으나
    >
    > 접속 경로가 생기게 되면 Permission Denied 뜰 수 있음

20. `cd apache-tomcat-9.0.30/bin`

    > tomcat 실행 : startup.sh 
    >
    > tomcat 중지 : shutdown.sh 

21. `ln -s startup.sh starttomcat`

    `ln -s shutdown.sh stoptomcat`

    > starttomcat과 stoptomcat으로 소프트 링크를 걸어준다.

22. `./starttomcat`

    `./stoptomcat`

    > 명령어로 tomcat 실행

23. 다시 윈도우의 Git Bash로 돌아가서 Tomcat 서버에 올릴 java.war파일을 tomcat.tar.gz 파일을 복사한 것 처럼 아마존 리눅스 서버에 복사

24. `scp -i "keypair이름.pem" java.war ec2-user@ec2-**-***-**-***.ap-northeast-2.compute.amazonaws.com:~/`

25. `cp -r java.war apache-tomcat-9.0.xx/webapps`

    > 복사한 war 파일은 apache-tomcat 밑의 `wepapps` 파일에 복사하여 넣어준다.

26. AWS 페이지 중 **탄력적 IP** 항목에서 IP를 할당 받는다.

27. 할당 받은 ip주소를 이용하여

    'http://ip주소:8080/war파일이름/war파일에서 불러올 jsp' 

    로 안드로이드 스튜디오의 Activity 에서 쓰였던 URL 경로를 변경해준다. 

28. 아마존 리눅스 서버에서 `./starttomcat` 후 안드로이드 스튜디오를 실행해본다. 