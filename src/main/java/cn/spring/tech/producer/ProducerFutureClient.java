package cn.spring.tech.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProducerFutureClient {

    public void sendMsg() throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.211.134:9091");
        properties.put(ProducerConfig.ACKS_CONFIG,"all");
        properties.put(ProducerConfig.RETRIES_CONFIG,1);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG,1);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"cn.spring.tech.producer.MyPartition");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<Object, Object> producer = new KafkaProducer<>(properties);
        for(int i=0;i<10;i++){
            Future<RecordMetadata> future = producer.send(new ProducerRecord("first", "hello:" + Integer.toString(i)));
            RecordMetadata metadata = future.get();
            System.out.println(metadata.topic()+"---"+metadata.partition()+"---"+metadata.offset());
        }
        producer.close();
        System.out.println("oks");

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ProducerFutureClient().sendMsg();
    }
}
