# sentinel
> [sentinel注解实现](https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81)

> [sentinel](https://github.com/alibaba/Sentinel)

## sentinel的版本
 * spring boot 的版本是2.1.0.RELEASE
 * sentinel 1.6.0
## 测试
 启动访问: http://localhost:7820/sentinel/hello
## sentinel-dashboard
sentinel依赖[sentinel-dashboard](https://github.com/alibaba/Sentinel/tree/master/sentinel-dashboard),首先要编译一份jar
## 启动参数说明
[启动配置项](https://github.com/alibaba/Sentinel/wiki/%E5%90%AF%E5%8A%A8%E9%85%8D%E7%BD%AE%E9%A1%B9)

simple
```shell
java -jar -Dcsp.sentinel.dashboard.server=localhost:8100 -Dproject.name=spring-boot-sentinel
```
* `csp.sentinel.dashboard.server` sentinel-dashboard服务地址
* `project.name` 注册的项目名称
## 说明
 * sentinel启动后默认开启`8719`端口向[sentinel-dashboard](https://github.com/alibaba/Sentinel/tree/master/sentinel-dashboard) 传输数据
## 使用流程
* 添加maven依赖
```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-annotation-aspectj</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-transport-simple-http</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-parameter-flow-control</artifactId>
        </dependency>
    </dependencies>
```
* SentinelResourceAspect bean注入到spring boot中
 ```java
public class SentinelConfig implements ImportSelector {
     // SentinelResourceAspect bean注入到spring中
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{SentinelResourceAspect.class.getName()};
    }
}
```
* 使用注解可参考[sentinel注解实现](https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81)
```java
    /**
     * 对应的 `handleException` 函数需要位于 `ExceptionUtil` 类中，并且必须为 static 函数.
     */
    @Override
    @SentinelResource(value = "test", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
    public void test() {
        log.info("test blockHandler");
    }
```
* 添加jvm参数
```shell
-Dcsp.sentinel.dashboard.server=localhost:8100 -Dproject.name=spring-boot-sentinel
```