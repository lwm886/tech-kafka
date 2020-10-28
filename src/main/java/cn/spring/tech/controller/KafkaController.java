package cn.spring.tech.controller;

import cn.spring.tech.dao.ConsumerOffsetDao;
import cn.spring.tech.model.ConsumerOffsetEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private ConsumerOffsetDao consumerOffsetDao;

    @PostMapping("/s/{msg}")
    public String send(@PathVariable("msg") String msg){
        String topic="seven";

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.211.134:9091");
        properties.put(ProducerConfig.ACKS_CONFIG,"all");
        properties.put(ProducerConfig.RETRIES_CONFIG,1);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG,1);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<Object, Object> producer = new KafkaProducer<>(properties);
        producer.send(new ProducerRecord(topic,"key-"+msg.hashCode(),"val-"+msg));
        producer.close();
        log.info("oks");
        return "oks";
    }

    @PostMapping("/c")
    public String consumer(){
        String group="f10";
        String topic="seven";

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.211.134:9091");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                for(TopicPartition topicPartition:collection){
                    int partition = topicPartition.partition();
                    long offset = consumer.position(topicPartition);
                    commitOffset(group,topic,partition,offset);
                }
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                for(TopicPartition topicPartition:collection){
                    int partition = topicPartition.partition();
                    long offset = getOffset(group, topic, partition);
                    consumer.seek(topicPartition,offset);
                }
            }
        });
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            List<ConsumerOffsetEntity> list=new ArrayList<>();
            for(ConsumerRecord<String,String> consumerRecord:records){
                String topics=consumerRecord.topic();
                int p=consumerRecord.partition();
                log.info("{}**********消费到消息：partition="+topics+p+",key="+consumerRecord.key()+",val="+consumerRecord.value()+",offset="+consumerRecord.offset(),getThread());
                ConsumerOffsetEntity offsetEntity = new ConsumerOffsetEntity();
                offsetEntity.setConsumerGroup(group);
                offsetEntity.setConsumerTopic(consumerRecord.topic());
                offsetEntity.setConsumerPartition(consumerRecord.partition()+"");
                offsetEntity.setConsumerOffset(consumerRecord.offset()+"");
                offsetEntity.setCreateTime(new Date());
                list.add(offsetEntity);
            }
            if(!CollectionUtils.isEmpty(list)){
                ConsumerOffsetEntity consumerOffsetEntity = list.get(list.size() - 1);
                consumerOffsetDao.insert(consumerOffsetEntity);
                log.info("{}===偏移量提交成功==="+consumerOffsetEntity,getThread());
            }

        }
    }

    private long getOffset(String group, String topic, int partition) {
        QueryWrapper<ConsumerOffsetEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("consumer_group",group);
        wrapper.eq("consumer_topic",topic);
        wrapper.eq("consumer_partition",partition);
        wrapper.orderByDesc("create_time");
        List<ConsumerOffsetEntity> list = consumerOffsetDao.selectList(wrapper);
        if(CollectionUtils.isEmpty(list)){
            log.info("{}>>>>>>>>>重新均衡分组<<<<<<<<<读取偏移量"+0,getThread());
            return 0;
        }
        ConsumerOffsetEntity consumerOffsetEntity = list.stream().max((p1, p2) -> Integer.valueOf(p1.getConsumerOffset()) - Integer.valueOf(p2.getConsumerOffset())).get();
        String offset = consumerOffsetEntity.getConsumerOffset();
        log.info("{}>>>>>>>>>重新均衡分组<<<<<<<<<读取偏移量"+consumerOffsetEntity,getThread());
        return Long.valueOf(offset)+1;
    }

    private void commitOffset(String group, String topic, int partition, long offset) {
        ConsumerOffsetEntity consumerOffsetEntity = new ConsumerOffsetEntity();
        consumerOffsetEntity.setConsumerGroup(group);
        consumerOffsetEntity.setConsumerTopic(topic);
        consumerOffsetEntity.setConsumerPartition(String.valueOf(partition));
        consumerOffsetEntity.setConsumerOffset(String.valueOf(offset));
        consumerOffsetEntity.setCreateTime(new Date());
        consumerOffsetDao.insert(consumerOffsetEntity);
        log.info("{}>>>>>>>>>重新均衡分组>>>>>>>>>提交偏移量",getThread());

    }

    private String getThread(){
        return Thread.currentThread().getName();
    }
}
