package cn.spring.tech.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CounterInterceptor implements ProducerInterceptor<String,String> {

    private  int sc;
    private int ec;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        return producerRecord;

    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if(e!=null){
            ec++;
        }else {
            sc++;
        }
    }

    @Override
    public void close() {
        System.out.println("success:"+sc);
        System.out.println("error:"+ec);
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
