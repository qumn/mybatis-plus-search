package io.github.qumn.mybatis.plus.search.java

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import org.apache.ibatis.session.SqlSession
import org.apache.ibatis.session.SqlSessionFactory
import io.github.qumn.mybatis.plus.search.mapper.PersonMapper
import io.github.qumn.mybatis.plus.search.util.getSessionFactory

class AnnotationWithJava : StringSpec({

    lateinit var sessionFactory: SqlSessionFactory
    lateinit var session: SqlSession
    lateinit var personMapper: PersonMapper
    beforeAny {
        sessionFactory = getSessionFactory()
        session = sessionFactory.openSession(true)
        personMapper = session.getMapper(PersonMapper::class.java)
    }

    "annotation with java should work" {
        val annotationJavaTest = AnnotationJavaTest()
        val sql = annotationJavaTest.testForJava(personMapper)
        sql shouldContain "uname like '%?%'"
        sql shouldContain "uid = ?"
        sql shouldContain "create_at between ? and ?"
        sql shouldContain "age > ?"
    }
})