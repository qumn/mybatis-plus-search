# mybatis-plus-search
## Why
在项目存在很多根据前端传递过来请求设置查询条件, 例如
有一个 `PersonSearchReq` 类定义如下
```java
public class PersonSearchReq {
    // default eq
    private Long id;
    @Operation(GT.class)
    private Integer age;
    @Operation(LIKE.class)
    private String name;
    @Operation(BETWEEN.class)
    private Instant[] createAt;
    ... // get set constructor hashCode equals ...
}
```
正常使用 mybatis plus 如下设置查询条件
```java
LambdaQueryWrapper<Person> wp = new LambdaQueryWrapper<>();
wp.eq(searchReq.getId() != null, Person::getId, searchReq.getId());
wp.gt(searchReq.getAge() != null, Person::getAge, searchReq.getAge());
wp.like(searchReq.getName() != null, Person::getName, searchReq.getName());
wp.between(searchReq.getCreateAt() != null, Person::getCreateAt, searchReq.getCreateAt()[0], searchReq.getCreateAt()[1]);
```
而这个库就是通过反射去遍历 `searchDto` 类, 找到不为空的字段并根据字段上的注解设置查询条件
```java
LambdaQueryWrapper<Person> wp = new LambdaQueryWrapper<>();
setSearchCondition(wp, Person.class, searchReq);
```
对于kotlin可以更加简单
```kotlin
val wp = LambdaQueryWrapper<Person>()
wp.setSearchCondition(searchReq)
```
