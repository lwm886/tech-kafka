package cn.spring.tech.interceptor;

import cn.spring.tech.producer.ProducerClient;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InterceptorProducer {
    public void sendMsg(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.211.134:9091");
        properties.put(ProducerConfig.ACKS_CONFIG,"all");
        properties.put(ProducerConfig.RETRIES_CONFIG,1);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG,1);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        List<String> interceptors=new ArrayList<>();
        interceptors.add("cn.spring.tech.interceptor.TimerInterceptor");
        interceptors.add("cn.spring.tech.interceptor.CounterInterceptor");
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,interceptors);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<Object, Object> producer = new KafkaProducer<>(properties);
        for(int i=0;i<10;i++){
            producer.send(new ProducerRecord("a3","key"+Integer.toString(i),"val"+Integer.toString(i)));
        }
        producer.close();
        System.out.println("oks");

    }

    public static void main(String[] args) {
        new InterceptorProducer().sendMsg();
    }
}
