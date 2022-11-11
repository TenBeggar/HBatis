# HBatis

## 简介

HBatis 是一个 HBase Client 的增强工具，致力于使用简单，性能强悍。

## 快速开始

1. 引入 maven 依赖

```xml
<dependency>
    <groupId>org.tenbeggar</groupId>
    <artifactId>hbatis</artifactId>
    <version>2.4.12</version>
</dependency>
```

2. 添加 application.yaml 配置

```yaml
hbase:
  zookeeper:
    quorum: localhost
    property:
      clientPort: 2181
    znode:
      parent: /hbase
```

3. 添加 @EnableHBatis 启动类 和 @MapperScan 包路径扫描

```java
@MapperScan("com.test.mapper")
@EnableHBatis
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Entity

```java
@HBaseTable(name = "user", family = "info")
@Data
public class User {

    @HBaseRowKey
    private Integer id;
    @HBaseCell("name")
    private String username;
    private Integer age;
    private String sex;
    private LocalDateTime birthday;
}
```

## Mapper

```java
package com.test.mapper;

public interface UserMapper extends HBaseMapper<User> {
}
```

## Service

```java
@Service
public class UserService extends HBaseAbstractService<UserMapper, User> {

    //条件查询 年龄大于30 或者 姓张的男性
    public List<User> list() {
        return lambdaQueryWrapper()
                .gt(User::getAge, 30)
                .or(e -> e.likeLeft(User::getUsername, "张").eq(User::getSex, "M"))
                .list();
    }
}
```

## 注意事项

**注意：请务必开放 HBase 的2181、16000、16020端口。**

## 后续规划

1. socket 链接池

2. 支持缓存

3. 支持拦截器
