package cn.spring.tech.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("consumer_offset")
public class ConsumerOffsetEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("consumer_group")
    private String consumerGroup;

    @TableField("consumer_topic")
    private String consumerTopic;

    @TableField("consumer_partition")
    private String consumerPartition;

    @TableField("consumer_offset")
    private String consumerOffset;

    @TableField("create_time")
    private Date createTime;

    @Override
    public String toString() {
        return "ConsumerOffsetEntity{" +
                "id=" + id +
                ", consumerGroup='" + consumerGroup + '\'' +
                ", consumerTopic='" + consumerTopic + '\'' +
                ", consumerPartition='" + consumerPartition + '\'' +
                ", consumerOffset='" + consumerOffset + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
