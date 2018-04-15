package com.bob.cache.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 基于lombok生成基础方法
 *
 * @author bob   <bobyang_coder@163.com>
 * @version v1.0
 * @since 2018/2/12
 */
public class Person {

    @Getter
    @Setter
    private int age;
    @Getter
    @Setter
    private String name;


    private Person() {
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static class Builder implements IBuilder<Person> {
        private Person person;

        public Builder(Person person) {
            this.person = person;
        }

        public static Builder newBuilder() {
            return new Builder(new Person());
        }


        public Builder setInfo(int age, String name) {
            person.setAge(age);
            person.setName(name);
            return this;
        }

        @Override
        public Person build() {
            return person;
        }
    }


    public interface IBuilder<T> {
        T build();
    }

    public static void main(String[] args) {
        Person bob = Builder.newBuilder().setInfo(10, "bob").build();
    }
}
