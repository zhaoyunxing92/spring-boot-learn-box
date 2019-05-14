# hystrix

## hystrix的版本

 * spring boot 的版本是2.1.0.RELEASE
 * spring-cloud-starter-netflix-hystrix 2.1.0.RELEASE

## 测试

 启动访问: http://localhost:7810/hystrix/hello
 
## 注意事项

### fallbackMethod 指向的方法参数列表必须保持一致

```java
    @GetMapping
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50")
    }, fallbackMethod = "fallbackMethod")
    public String say(@RequestParam(required = false, defaultValue = "10") long time) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(time);
        log.info("{} sleep time {}毫秒", Thread.currentThread().getName(), time);
        return "hello hystrix";
    }
    // 参数列表必须跟say方法一致
    public String fallbackMethod(long time) {
        log.info("{} fallback ,sleep time {}毫秒", Thread.currentThread().getName(), time);
        return "fallback, hystrix intervention";
    }
``` 

### commandProperties 参数列表参考[configuration](https://github.com/Netflix/Hystrix/wiki/Configuration)