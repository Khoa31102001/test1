package tech.dut.fasto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import tech.dut.fasto.aop.logging.FilterRequestAspect;
import tech.dut.fasto.aop.logging.LoggingAspect;
import tech.dut.fasto.common.repository.ShopRepository;
import tech.dut.fasto.common.service.impl.MessageService;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {

    @Bean
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }

    @Bean
    public FilterRequestAspect filterRequestAspect(Environment env, ShopRepository shopRepository, MessageService messageService) {
        return new FilterRequestAspect(shopRepository, messageService);
    }
}
