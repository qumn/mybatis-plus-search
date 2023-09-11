package xyz.qumn.mybatis.plus.search.java;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import xyz.qumn.mybatis.plus.search.Entity.Person;
import xyz.qumn.mybatis.plus.search.mapper.PersonMapper;

import java.time.Duration;
import java.time.Instant;

import static xyz.qumn.mybatis.plus.search.annotation.OperationHandlerKt.handle;
import static xyz.qumn.mybatis.plus.search.util.MybatisUtilKt.getSessionFactory;

public class AnnotationJavaTest {
    public void test() {
        SqlSessionFactory sessionFactory = getSessionFactory();
        SqlSession session = sessionFactory.openSession(true);
        PersonMapper personMapper = session.getMapper(PersonMapper.class);
        LambdaQueryWrapper<Person> wp = new LambdaQueryWrapper<>();
        JavaPersonSearchDto searchReq = new JavaPersonSearchDto();
        searchReq.setAge(16);
        searchReq.setId(1L);
        searchReq.setInstant(new Instant[]{Instant.now().minus(Duration.ofDays(1L)), Instant.now()});
        searchReq.setName("zs");
        handle(wp, Person.class, searchReq);
        // TODO why have not id condition
        System.out.println(wp.getTargetSql());
    }
}
