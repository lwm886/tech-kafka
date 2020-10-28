package cn.spring.tech.service.impl;

import cn.spring.tech.dao.ConsumerOffsetDao;
import cn.spring.tech.model.ConsumerOffsetEntity;
import cn.spring.tech.service.OffsetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OffsetServiceImpl extends ServiceImpl<ConsumerOffsetDao, ConsumerOffsetEntity> implements OffsetService {
}
