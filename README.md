# HBatis

## 简介

HBatis 是一个 HBase Client 的增强工具，致力于使用简单，性能强悍。

## 快速入门

1. 配置 HBaseTemplate

```java

@MapperScan("com.test.mapper")
@Configuration
public class HBaseConfig {

    @Value("${hbase.zookeeper.quorum}")
    private String zookeeperQuorum;
    @Value("${hbase.zookeeper.property.clientPort}")
    private String clientPort;
    @Value("${zookeeper.znode.parent}")
    private String znodeParent;

    @Bean
    public HBaseTemplate hbaseTemplate() {
        return new HBaseTemplate(zookeeperQuorum, clientPort, znodeParent);
    }
}
```

2. 创建实体类

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
}
```

3. 构建 Mapper

```java

@Mapper
public interface UserMapper extends HBaseMapper<User> {
}
```

4. 使用示例

```java

@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    //分页查询 年龄大于30 或者 姓张的男性
    @Test
    public void test() {
        Page<User> page = userMapper.queryWrapper()
                .gt("age", 30)
                .or(e -> e.likeLeft("name", "张").eq("age", "M"))
                .page(IPageable.builder().startId(0).pageSize(10L).build());
        System.out.println(page);
    }
}
```

## 注意事项

**注意：请务必开放 HBase 的2181、16000、16020端口。**

## 后续规划

1. 支持 RowKey 条件查询

2. 支持 PUT 写入数据

3. 支持 Lambda 表达式

4. 支持拦截器