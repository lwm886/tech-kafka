package cn.spring.tech.producer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerCallBackClient {

    public void sendMsg(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.211.133:9091");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,1);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        for(int i=0;i<10;i++){
            producer.send(new ProducerRecord("first", "【msgaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa------】"+i), (recordMetadata, e) -> {
                if(e==null){
                    System.out.println(recordMetadata.topic()+recordMetadata.partition()+"----------"+recordMetadata.offset());
                }
            });
        }
        producer.close();
        System.out.println("oks");

    }

    public static void main(String[] args) {
        new ProducerCallBackClient().sendMsg();
    }
}
