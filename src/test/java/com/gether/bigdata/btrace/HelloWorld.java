package com.gether.bigdata.btrace;

import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Duration;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;
import com.sun.btrace.annotations.Return;
import com.sun.btrace.annotations.Self;

import java.lang.reflect.Field;

import static com.sun.btrace.BTraceUtils.*;
@BTrace
public class HelloWorld {

    private static Field rabbitmqFiled = null;
    private static Field aFiled = null;
    private static Field bFiled = null;
    private static Field cFiled = null;

    @OnMethod(
            clazz = "com.gether.research.mq.RabbitMqSender",
            method = "getUserStr",
            location = @Location(Kind.RETURN)
    )
    // duration的单位是纳秒，要除以 1,000,000 才是毫秒。
    public static void returnMethod(@Self Object self, @ProbeClassName String probeClass, @ProbeMethodName String probeMethod, String password, @Return String result, @Duration long duration) {
        initFiled();

        println("enter class: " + probeClass + " method: " + probeMethod);
        println("getUser params: " + password + " result: " + result);

        Object rabbitObj = get(rabbitmqFiled, self);
        printFields(rabbitObj);

        Object aobj = get(aFiled, rabbitObj);
        println("this is aFiled:" + aobj);
        Object bobj = get(bFiled, rabbitObj);
        println("this is bFiled:" + bobj);
        Object cobj = get(cFiled, rabbitObj);
        println("this is cFiled:" + cobj);
        println("cost time(ms): " + duration);

        println("Heap:");

        println(Sys.Memory.heapUsage());

        println("Non-Heap:");

        println(Sys.Memory.nonHeapUsage());
    }


    @OnMethod(
            clazz = "com.gether.research.mq.RabbitMqSender",
            method = "getUserStr",
            location = @Location(Kind.CATCH)
    )
    public static void exceptionMethod(@ProbeClassName String probeClass, @ProbeMethodName String probeMethod, Exception e) {
        println(probeClass + "." + probeMethod + "cacth Exception" + e);
    }

    private static void initFiled() {
        if (null == rabbitmqFiled) {
            rabbitmqFiled = field(classForName("com.gether.research.mq.RabbitMqSender", contextClassLoader()), "rabbitTemplate");
        }
        if (null == aFiled) {
            aFiled = field(classForName("org.springframework.amqp.rabbit.core.RabbitTemplate", contextClassLoader()), "uuid");
        }
        if (null == bFiled) {
            bFiled = field(classForName("org.springframework.amqp.rabbit.core.RabbitTemplate", contextClassLoader()), "replyAddress");
        }
        if (null == cFiled) {
            cFiled = field(classForName("org.springframework.amqp.rabbit.core.RabbitTemplate", contextClassLoader()), "encoding");
        }
    }
}