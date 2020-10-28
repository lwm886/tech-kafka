package cn.spring.tech.dao;

import cn.spring.tech.model.ConsumerOffsetEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsumerOffsetDao extends BaseMapper<ConsumerOffsetEntity> {
}
