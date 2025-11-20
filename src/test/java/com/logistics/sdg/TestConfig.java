package com.logistics.sdg;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@TestConfiguration
@EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
public class TestConfig {
}