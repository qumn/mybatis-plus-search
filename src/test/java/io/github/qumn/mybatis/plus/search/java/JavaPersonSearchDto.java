package io.github.qumn.mybatis.plus.search.java;

import io.github.qumn.mybatis.plus.search.annotation.BETWEEN;
import io.github.qumn.mybatis.plus.search.annotation.GT;
import io.github.qumn.mybatis.plus.search.annotation.LIKE;
import io.github.qumn.mybatis.plus.search.annotation.Operation;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

public class JavaPersonSearchDto {
    // default eq
    private Long id;
    @Operation(GT.class)
    private Integer age;
    @Operation(LIKE.class)
    private String name;
    @Operation(BETWEEN.class)
    private Instant[] createAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant[] getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant[] createAt) {
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaPersonSearchDto that = (JavaPersonSearchDto) o;
        return Objects.equals(id, that.id) && Objects.equals(age, that.age) && Objects.equals(name, that.name) && Arrays.equals(createAt, that.createAt);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, age, name);
        result = 31 * result + Arrays.hashCode(createAt);
        return result;
    }
}
