# spring-boot-oss

  这个分支主要解决阿里云oss的使用

## 参考的文档

* [快速搭建移动应用直传服务](https://help.aliyun.com/document_detail/31920.html?spm=a2c4g.11186623.6.1378.1b307d43Se1NJZ) 快速搭建移动应用直传服务

* [oss-sts](https://help.aliyun.com/document_detail/100624.html?spm=5176.10695662.1996646101.searchclickresult.64e0515aCLJzVr) STS（Security Token Service）进行临时授权访问

* [AssumeRole](https://helpcdn.aliyun.com/document_detail/28763.html?spm=a2c4g.11186623.2.16.481e7074k4fkgq#reference-clc-3sv-xdb) 调用AssumeRole接口获取一个扮演该角色的临时身份

* [sts-endpoint](https://helpcdn.aliyun.com/document_detail/66053.html?spm=a2c4g.11186623.2.15.481e7074bRCZ8B#reference-sdg-3pv-xdb) STS服务的所有接入地址

## 使用的关键jar

```xml
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-sts</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-core</artifactId>
    <version>3.5.0</version>
</dependency>
```