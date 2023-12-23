package tech.dut.fasto.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.web.cors.CorsConfiguration;

@Getter
@Setter
@ConfigurationProperties(prefix = "fasto", ignoreUnknownFields = false)
public class FastoProperties {
    private final CorsConfiguration cors = new CorsConfiguration();

    private final Security security = new Security();
    private final Mail mail = new Mail();
    private final S3 s3 = new S3();
    private final Redis redis = new Redis();
    private final Firebase firebase = new Firebase();

    private final VNPay vnPay = new VNPay();
    @Getter
    @Setter
    public static class Security {

        private final Authentication authentication = new Authentication();

        @Getter
        @Setter
        public static class Authentication {

            private final Jwt jwt = new Jwt();

            @Getter
            @Setter
            public static class Jwt {

                private String secret = FastoDefaults.Security.Authentication.Jwt.secret;

                private long tokenValidityInSeconds = FastoDefaults.Security.Authentication.Jwt
                        .tokenValidityInSeconds;

                private long tokenValidityInSecondsForRememberMe = FastoDefaults.Security.Authentication.Jwt
                        .tokenValidityInSecondsForRememberMe;
            }
        }
    }
    @Getter
    @Setter
    public static class Mail {
        private String from;
    }

    @Getter
    @Setter
    public static class S3 {
        private final Aws aws = new Aws();

        @Getter
        @Setter
        public static class Aws {
            private String bucketName = FastoDefaults.S3.Aws.bucketName;
            private String region = FastoDefaults.S3.Aws.region;
            private int preSignedExpired = FastoDefaults.S3.Aws.preSignedExpired;
            private final Aws.Credentials credentials = new Aws.Credentials();

            @Getter
            @Setter
            public static class Credentials {

                private String accessKey = FastoDefaults.S3.Aws.Credentials.accessKey;

                private String secretKey = FastoDefaults.S3.Aws.Credentials.secretKey;
            }
        }
    }

    @Getter
    @Setter
    public static class Redis {
        private int port;
        private String password;
        private String host;
        private Long expireTime;
    }

    @Getter
    @Setter
    public static class Firebase {
        Resource serviceAccount = FastoDefaults.Firebase.serviceAccount;
    }

    @Getter
    @Setter
    public static class VNPay {
        private String version;
        private String command;
        private String tmnCode;
        private String currCode;
        private String hashSecret;
        private String payUrl;
        private String apiUrl;
    }

}
