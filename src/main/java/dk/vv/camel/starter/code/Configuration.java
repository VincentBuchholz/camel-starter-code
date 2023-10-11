package dk.vv.camel.starter.code;


import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "camel.starter.code", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
public interface Configuration {
    String contextName();
    QueueConfig mq();

    interface QueueConfig {
        String host();
        int port();
        String username();
        String password();
        String vhost();
        boolean useSsl();
        int redeliveryBaseDelaySec();
        int redeliveryMultiplier();
        int redeliveryMaxDelaySec();

    }
}



