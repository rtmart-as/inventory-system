package com.ychs.web.consumer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ychs.web.consumer.entity.Consumer;
import com.ychs.web.consumer.mapper.ConsumerMapper;
import com.ychs.web.consumer.service.ConsumerService;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl extends ServiceImpl<ConsumerMapper, Consumer> implements ConsumerService {
}