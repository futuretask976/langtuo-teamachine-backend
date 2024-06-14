# gx-springboot3-demo

## 运行
### 通过SpringBoot直接运行
运行GxSpringBoot3DemoApplication中的main方法，启动SpringBoot内嵌的Tomcat，然后访问：http://localhost:8080/，用户名默认是user，密码可以在启动控制台中找到，类似Using generated security password: 3bc48f8d-87f1-4b7a-8218-04479fee556e。
访问：http://localhost:8080/test002，可以看到输出。
### docker容器中运行
通过mvn clean package打包后，会自动创建镜像文件，然后通过下面的命令运行镜像：
````
docker run --name demo-web --link mysql:dbhost \
    -d \
    -p 8080:8080 -v /etc/localtime:/etc/localtime \
    -v /Users/Miya/DockerSpace/gx-springboot3-demo/logs:/var/logs \
    gx-springboot3-demo/demo-web:1.0.0-SNAPSHOT
````
需要注意的是，如果是在docker中运行，需要将数据库的链接host改为db，如果不是在docker中运行，则是将数据库的链接host改为localhost。