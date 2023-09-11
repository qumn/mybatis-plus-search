package xyz.qumn.mybatis.plus.search.java

import io.kotest.core.spec.style.StringSpec

class AnnotationWithJava : StringSpec({
    "annotation with java should work" {
        val annotationJavaTest = AnnotationJavaTest()
        annotationJavaTest.test()
    }
})