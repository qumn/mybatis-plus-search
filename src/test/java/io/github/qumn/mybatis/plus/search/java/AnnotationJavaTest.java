package io.github.qumn.mybatis.plus.search.java;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.qumn.mybatis.plus.search.Entity.Person;
import io.github.qumn.mybatis.plus.search.mapper.PersonMapper;

import java.time.Duration;
import java.time.Instant;

import static io.github.qumn.mybatis.plus.search.annotation.OperationHandlerKt.setSearchCondition;

public class AnnotationJavaTest {
    public String testForJava(PersonMapper personMapper) {
        LambdaQueryWrapper<Person> wp = new LambdaQueryWrapper<>();
        JavaPersonSearchDto searchReq = new JavaPersonSearchDto();
        searchReq.setAge(16);
        searchReq.setId(1L);
        searchReq.setCreateAt(new Instant[]{Instant.now().minus(Duration.ofDays(1L)), Instant.now()});
        searchReq.setName("zs");

        setSearchCondition(wp, Person.class, searchReq);

        String targetSql = wp.getTargetSql();
        System.out.println(targetSql);
        return targetSql;
    }

    public void test() {
        LambdaQueryWrapper<Person> wp = new LambdaQueryWrapper<>();
        JavaPersonSearchDto searchReq = new JavaPersonSearchDto();
        searchReq.setAge(16);
        searchReq.setId(1L);
        searchReq.setCreateAt(new Instant[]{Instant.now().minus(Duration.ofDays(1L)), Instant.now()});
        searchReq.setName("zs");

        wp.eq(searchReq.getId() != null, Person::getId, searchReq.getId());
        wp.gt(searchReq.getAge() != null, Person::getAge, searchReq.getAge());
        wp.like(searchReq.getName() != null, Person::getName, searchReq.getName());
        wp.between(searchReq.getCreateAt() != null, Person::getCreateAt, searchReq.getCreateAt()[0], searchReq.getCreateAt()[1]);

    }
}
