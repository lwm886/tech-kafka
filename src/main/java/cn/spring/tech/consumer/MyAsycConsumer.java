package cn.spring.tech.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class MyAsycConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.211.134:9091");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "a");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("a3"));
        while (true) {
            System.out.println("###############"+Thread.currentThread().getId());
            ConsumerRecords<String, String> records = consumer.poll(100000);
            for(ConsumerRecord<String,String> consumerRecord:records){
                System.out.println("=====================|"+consumerRecord.key()+"---"+consumerRecord.value()+"---"+consumerRecord.offset());
            }
            consumer.commitAsync((map, e) -> {
                if(e!=null){
                    System.out.println("###########commit error");
                }else{
                    System.out.println(Thread.currentThread().getId()+"|#####提交成功");
                }
            });

        }

    }
}
