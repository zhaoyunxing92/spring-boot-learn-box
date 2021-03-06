# spring-boot-rabbitmq  exchange

>代码地址：https://github.com/zhaoyunxing92/spring-boot-learn-box/tree/master/spring-boot-rabbitmq/rabbitmq-java

如果你没有`rabbitmq`环境可以先到[https://github.com/zhaoyunxing92/docker-case](https://github.com/zhaoyunxing92/docker-case)获取一个`rabbitmq`,当然你要会点docker(不过这个不难)

exchange可以理解为消息交换机，指定消息按照什么规则，路由到哪个队列

## 网上偷的图

![交换机消费模型](https://gitee.com/zhaoyunxing92/resource/raw/master/amqp/exchange.png)

## exchange属性
 
 * `name`:交换机名称
 * `type`:交换机类型`direct`、`topic`、`fanout`、`headers`
 * `durability`：是否需要持久化
 * `auto delete`:当最后一个绑定到exchange的队列删除后，该exchange自动删除
 * `internal`：是否用于rabbitmq内部使用的交换机，默认false
 * `arguments`：扩展参数

### exchange类型说明

#### direct exchange

直连交换机(direct exchange)根据消息的路由键(routing key)将消息投递到对应的队列.(routing key跟queue里的key保持一直才可以消费),直连不支持模糊路由，必须是一对一的
仔细观察下面会发现routingkey和queue相同,这个时候rabbitmq会使用`AMQP default`exchange发送消息,不用执行创建exchange和绑定queue

```java
@Slf4j
public class DirectTest {

    private Connection connection;
    private Channel channel;
    private String queueName = "direct_test";

    @Before
    public void setUp() throws Exception {
        //1.创建连接工程
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setPassword("123456");
        factory.setUsername("guest");
        /*虚拟机节点*/
        factory.setVirtualHost("/");
        //2.创建连接
        connection = factory.newConnection();

        //3.获取channel
        channel = connection.createChannel();
    }

    @After
    public void setDown() throws IOException, TimeoutException {
        log.info("[*] close channel");
        //5.关闭通道
        channel.close();
        connection.close();
    }

    /**
     * 消费者,先运行
     */
    @Test
    public void consumer() throws IOException {
        String exchangeName="direct_exchange";
        // 4.声明exchange
        //channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true);

        /**
         * 5.声明队列
         * @param queue 队列名称
         * @param durable 是否持久，即使服务重启
         * @param exclusive 是否只有这个队列消费,true后消费者停止了这个队列也就自动删除
         * @param autoDelete 不使用的时候自动删除
         * @param arguments 其他参数
         */
        channel.queueDeclare(queueName, false, true, true, null);

        // 6.绑定
       // channel.queueBind(queueName, exchangeName, "test.direct");
        log.info("[*] waiting for msg");
        //回调
        DeliverCallback callback = (consumerTag, delivery) -> {
            log.info(new String(delivery.getBody(), StandardCharsets.UTF_8));
            Envelope envelope = delivery.getEnvelope();
            log.info("[*] routing key is 【{}】", envelope.getRoutingKey());
            log.info("[*] delivery tag is 【{}】", envelope.getDeliveryTag());
            log.info("[*] exchange is 【{}】", envelope.getExchange());
        };

        //5.创建消费者
        channel.basicConsume("", true, callback, cancelled -> log.info("[*]callback when the consumer is cancelled。{}", cancelled));
        // 防止线程退出
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 生产者,在消费者之后运行
     */
    @Test
    public void product() throws IOException {
        log.info("[*] product msg");
        //4.发送消息
        String msg = "hello rabbitmq";
        //TODO:这时路由键设置的是queue名称
        /**
         * @param exchange 交换机
         * @param routingKey 路由键
         * @param props other properties for the message - routing headers etc
         * @param body 消息体
         */
        channel.basicPublish("", queueName, null, msg.getBytes());
    }
}
```

#### topic exchange

根据routing key把消息转发到对应的queue上,可以模糊匹配

##### 模糊匹配规则

* `*` 只匹配一个词，可以理解为只有一个占位符,例如：`test.*`,只能匹配到`test.direct`却匹配不到`test.direct.sunny`
* `#` 可以匹配多个词，可以理解为有多个占位符，例如：`test.#`,就可以匹配`test.direct`和`test.direct.sunny`

由于代码跟direct exchange差不多类似我这里就只写关键的代码,按照下面消费模式,只有第三条消息是消费不到的

```java
/**
* 消费者
*/
@Test
public void consumer() throws IOException {
    // 4.声明exchange 类型为topic
    channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true);
    // 5.声明队列
    String queueName = "topic_test";
    channel.queueDeclare(queueName, false, true, true, null);
    // 6.绑定
    channel.queueBind(queueName, exchangeName, "test.#");
    ...
}
/**
* 生产者
*/
@Test
public void product() throws IOException {
    log.info("[*] product msg");
    //4.发送消息
    String routingKey1 = "test.abc";
    String routingKey2 = "test.abc.efg";
    String routingKey3 = "acb.test.abc";
    
    AMQP.BasicProperties builder = new AMQP.BasicProperties.Builder()
            .contentType("application/json")
            .deliveryMode(2) //2标示消息持久化
            .priority(0) //消息优先级
            .build();
    channel.basicPublish(exchangeName, routingKey1, builder, routingKey1.getBytes());
    channel.basicPublish(exchangeName, routingKey2, builder, routingKey2.getBytes());
    channel.basicPublish(exchangeName, routingKey3, builder, routingKey3.getBytes());
}
```

#### fanout exchange

扇型交换机(fanout exchange),不处理路由键，只把队列绑定到交换机上，当有消息发送到扇型交换机上时，消息会被拷贝到所有绑定的队列，所有它是用来处理广播路由的，同理性能是最好的

由于代码跟direct exchange差不多类似我这里就只写关键的代码,我们只设置了exchange没有设置routing key，消息可以消费到

```java
/**
 * 消费者
 */
@Test
public void consumer() throws IOException, InterruptedException {
    // 4.声明exchange
    channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true);
    // 5.声明队列
    String queueName = "fanout_test";
    channel.queueDeclare(queueName, false, false, false, null);
    // 6.绑定 TODO: 绑定的时候没有设置路由键（routing key）
    channel.queueBind(queueName, exchangeName, "");
    ...
}

/**
 * 生产者
 */
@Test
public void product() throws IOException {
    log.info("[*] product msg");
    //4.发送消息 TODO:没有指定路由键直接发送消息
    String msg = "hello fanout exchange";
    channel.basicPublish(exchangeName, "", null, msg.getBytes());
}
```

#### headers exchange

头交换机(headers exchange),有时候消息的路由操作涉及到多个属性，使用routing key不容易表达就可以使用头交换机(没有使用场景就不写代码了)
