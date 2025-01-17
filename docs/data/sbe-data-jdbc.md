# SpringBoot 教程之通过 JDBC 访问数据
<!-- TOC depthFrom:2 depthTo:3 -->

- [简介](#简介)
- [API](#api)
    - [execute](#execute)
    - [update](#update)
    - [query](#query)
- [实战](#实战)
    - [配置数据源](#配置数据源)
    - [注入 JdbcTemplate](#注入-jdbctemplate)
    - [完整示例](#完整示例)
- [引申和引用](#引申和引用)

<!-- /TOC -->

## 简介

Spring Data 包含对 JDBC 的存储库支持，并将自动为 `CrudRepository` 上的方法生成 SQL。对于更高级的查询，提供了 `@Query` 注解。

当 classpath 上存在必要的依赖项时，Spring Boot 将自动配置 Spring Data 的 JDBC 存储库。它们可以通过 `spring-boot-starter-data-jdbc` 的单一依赖项添加到项目中。如有必要，可以通过将 `@EnableJdbcRepositories` 批注或 `JdbcConfiguration` 子类添加到应用程序来控制 Spring Data JDBC 的配置。

> 更多 Spring Data JDBC 细节，可以参考 [Spring Data JDBC 官方文档](http://spring.io/projects/spring-data-jdbc)。

## API

`spring-boot-starter-data-jdbc` 引入了 `spring-jdbc` ，其 JDBC 特性就是基于 `spring-jdbc`。

而 `spring-jdbc` 最核心的 API 无疑就是 `JdbcTemplate`，可以说所有的 JDBC 数据访问，几乎都是围绕着这个类去工作的。

Spring 对数据库的操作在 Jdbc 层面做了深层次的封装，利用依赖注入，把数据源配置装配到 `JdbcTemplate` 中，再由 `JdbcTemplate` 负责具体的数据访问。

`JdbcTemplate` 主要提供以下几类方法：

- `execute` 方法：可以用于执行任何 SQL 语句，一般用于执行 DDL 语句；
- `update` 方法及 `batchUpdate` 方法：`update` 方法用于执行新增、修改、删除等语句；`batchUpdate` 方法用于执行批处理相关语句；
- `query` 方法及 `queryForXXX` 方法：用于执行查询相关语句；
- `call` 方法：用于执行存储过程、函数相关语句。

为了方便演示，以下增删改查操作都围绕一个名为 user 的表（该表的主键 id 是自增序列）进行，该表的数据实体如下：

```java
public class User {
    private Integer id;
    private String name;
    private Integer age;

    // 省略 getter/setter
}
```

数据实体只要是一个纯粹的 Java Bean 即可，无需任何注解修饰。

### execute

使用 execute 执行 DDL 语句，创建一个名为 test 的数据库，并在此数据库下新建一个名为 user 的表。

```java
public void recreateTable() {
    jdbcTemplate.execute("DROP DATABASE IF EXISTS test");
    jdbcTemplate.execute("CREATE DATABASE test");
    jdbcTemplate.execute("USE test");
    jdbcTemplate.execute("DROP TABLE if EXISTS user");
    jdbcTemplate.execute("DROP TABLE if EXISTS user");
    // @formatter:off
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE TABLE user (id int (10) unsigned NOT NULL AUTO_INCREMENT,\n")
        .append("name varchar (64) NOT NULL DEFAULT '',\n")
        .append("age tinyint (3) NOT NULL DEFAULT 0,\n")
        .append("PRIMARY KEY (ID));\n");
    // @formatter:on
    jdbcTemplate.execute(sb.toString());
}
```

### update

新增数据

```java
public void insert(String name, Integer age) {
    jdbcTemplate.update("INSERT INTO user(name, age) VALUES(?, ?)", name, age);
}
```

删除数据

```java
public void delete(String name) {
    jdbcTemplate.update("DELETE FROM user WHERE name = ?", name);
}
```

修改数据

```java
public void update(User user) {
    jdbcTemplate.update("UPDATE USER SET name=?, age=? WHERE id=?", user.getName(), user.getAge(), user.getId());
}
```

批处理

```java
public void batchInsert(List<User> users) {
    String sql = "INSERT INTO user(name, age) VALUES(?, ?)";

    List<Object[]> params = new ArrayList<>();

    users.forEach(item -> {
        params.add(new Object[] {item.getName(), item.getAge()});
    });
    jdbcTemplate.batchUpdate(sql, params);
}
```

### query

查单个对象

```java
public User queryByName(String name) {
    try {
        return jdbcTemplate
            .queryForObject("SELECT * FROM user WHERE name = ?", new BeanPropertyRowMapper<>(User.class), name);
    } catch (EmptyResultDataAccessException e) {
        return null;
    }
}
```

查多个对象

```java
public List<User> list() {
    return jdbcTemplate.query("select * from USER", new BeanPropertyRowMapper(User.class));
}
```

获取某个记录某列或者 count、avg、sum 等函数返回唯一值

```java
public Integer count() {
    try {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
    } catch (EmptyResultDataAccessException e) {
        return null;
    }
}
```

## 实战

### 配置数据源

在 `src/main/resource` 目录下添加 `application.properties` 配置文件，内容如下：

```properties
spring.datasource.url = jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&useSSL=false
spring.datasource.username = root
spring.datasource.password = root
spring.datasource.driver-class-name = com.mysql.cj.jdbc.Driver
```

需要根据实际情况，替换 `url`、`username`、`password`。

### 注入 JdbcTemplate

```java
@Service
public class UserDAOImpl implements UserDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ...
}
```

### 完整示例

请参考：[源码](https://github.com/dunwu/spring-boot-tutorial/tree/master/codes/data/spring-boot-data-jdbc)

## 引申和引用

**引申**

- [Spring Boot 教程](https://github.com/dunwu/spring-boot-tutorial)

**参考**

- [Spring Boot 官方文档之 boot-features-data-jdbc](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-data-jdbc)
