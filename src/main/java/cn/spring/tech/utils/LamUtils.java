package cn.spring.tech.utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class LamUtils {

    public static void main(String[] args) {
        List<Apple> appleList = new ArrayList<>();//存放apple对象集合

        Apple apple1 =  new Apple(1,"苹果1",new BigDecimal("1.0"),10);
        Apple apple12 = new Apple(1,"苹果2",new BigDecimal("1.0"),20);
        Apple apple2 =  new Apple(2,"香蕉",new BigDecimal("10"),30);
        Apple apple3 =  new Apple(3,"荔枝",new BigDecimal("100"),40);

        appleList.add(apple1);
        appleList.add(apple12);
        appleList.add(apple2);
        appleList.add(apple3);

        //按照某个属性求和
        BigDecimal sum = appleList.stream().map(Apple::getMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(sum);

        //极值
        Apple max = appleList.stream().collect(Collectors.maxBy(Comparator.comparing(Apple::getMoney))).get();
        Apple min = appleList.stream().collect(Collectors.minBy(Comparator.comparing(Apple::getMoney))).get();
        System.out.println(max.getMoney());
        System.out.println(min.getMoney());

        //每组数量
        Map<Integer, Long> countMap = appleList.stream().collect(Collectors.groupingBy(Apple::getId, Collectors.counting()));
        System.out.println(countMap);

        //每组某个字段的最大值
        Map<Integer, Optional<Apple>> maxMap = appleList.stream().collect(Collectors.groupingBy(Apple::getId, Collectors.maxBy(Comparator.comparing(Apple::getMoney))));
        System.out.println(maxMap);
    }
}

