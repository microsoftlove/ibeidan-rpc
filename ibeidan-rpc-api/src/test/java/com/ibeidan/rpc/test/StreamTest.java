package com.ibeidan.rpc.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * @author lee
 * DATE 2020/4/22 16:41
 */
public class StreamTest {

    /**
     * @author libeibei
     * 2020/4/22 16:52
     * StreamSupport.stream() test
     *通过查看Collection.stream()的方法，我们可以看出来，
     * Colleciton.stream()其实是调用了StreamSupport.stream()来实现的。
     * 所以我们也可以使用StreamSupport.stream()来创建一个Stream。
     * 当我们面对的是一个迭代器的时候，
     * 使用StreamSupport.stream()就可以创建一个Stream。
     * 第一个参数是传入一个迭代器，
     * 第二个参数是true代表使用并行来进行处理。
     * false代表串行来处理Stream。
     **/
    @Test
    public void testStream(){

        List<Integer> numbers = new ArrayList<>();
        numbers.add(3);
        numbers.add(4);
        numbers.add(8);
        numbers.add(16);
        numbers.add(19);
        numbers.add(27);
        numbers.add(23);
        Spliterator<Integer> integers = numbers.spliterator();
        StreamSupport.stream(integers,false)
                .forEach(System.out::println);

    }

    /**
     * @author libeibei
     * 2020/4/22 17:17
     *
     * 从名字上就能看出来，这是一个Stream的过滤转换，
     * 此方法会生成一个新的流，其中包含符合某个特定条件的所有元素。
     * 过滤掉小于50的
     * 输出[232, 56]
     **/
    @Test
    public void testStreamSpliter(){
        List<Integer> integerList = new ArrayList<>();
        integerList.add(15);
        integerList.add(32);
        integerList.add(5);
        integerList.add(232);
        integerList.add(56);
        List<Integer> after = StreamSupport.stream(integerList.spliterator(),false)
                .filter(i->i>50)
                .collect(Collectors.toList());

        System.out.println(after);
    }


    /**
     * @author libeibei
     * 2020/4/22 17:19
     * map方法指对一个流中的值进行某种形式的转换。
     * 需要传递给它一个转换的函数作为参数。
     * 将Integer类型转换成String类型
     **/
    @Test
    public void testStreamMap(){

        List<Integer> integerList = new ArrayList<>();
        integerList.add(15);
        integerList.add(32);
        integerList.add(5);
        integerList.add(232);
        integerList.add(56);
       //将Integer类型转换成String类型
        List<String> afterString =
                StreamSupport.stream(integerList.spliterator(),true)
                .map(i->String.valueOf(i))
                .collect(Collectors.toList());

        System.out.println(afterString);
    }


    @Test
    public void testGetCanonicalName(){
        System.out.println(StreamSupport.class.getCanonicalName());
    }


}
