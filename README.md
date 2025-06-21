# spring-bean-call

通过 http 接口调用 spring 中的 bean，方便处理数据、测试，支持公有、私有方法

示例：com.kuajingnet.springbean.call.example.Main

## 使用

依赖中引入

```xml

<dependency>
    <groupId>com.kuajingnet</groupId>
    <artifactId>spring-bean-cll</artifactId>
    <version>1.0.0</version>
</dependency>
```

程序启动的时候添加如下代码,并暴露端口：

```java
BeanHttpExpose.build(9009,triggerService);
```

通过 http 调用：

- 公有方法

```shell
curl --location --request POST 'http://127.0.0.1:9009/demoBean/testCallPublic' \
--header 'Content-Type: application/json' \
--data-raw '["public"]'
```

- 私有方法

```shell
curl --location --request POST 'http://127.0.0.1:9009/demoBean/testCallPublic' \
--header 'Content-Type: application/json' \
--data-raw '["private"]'
```

调用的 url 格式：http://127.0.0.1:9009/{beanName}/{methodName}

post body 中是个字符串数组，对应调用方法的入参
