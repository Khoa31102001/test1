package tech.dut.fasto.config.properties;

import org.springframework.core.io.Resource;

@SuppressWarnings("java:S2386")
public interface FastoDefaults {
    interface Security {

        interface Authentication {

            interface Jwt {

                String secret = null;
                long tokenValidityInSeconds = 1800; // 30 minutes
                long tokenValidityInSecondsForRememberMe = 2592000; // 30 days
            }
        }
    }

    interface S3 {
        interface Aws{
            interface Credentials{
                String accessKey = null;
                String secretKey = null;
            }
            String bucketName = null;
            String region = null;
            int preSignedExpired =10;
        }
    }

    interface Firebase{
        Resource serviceAccount = null;
    }

}
