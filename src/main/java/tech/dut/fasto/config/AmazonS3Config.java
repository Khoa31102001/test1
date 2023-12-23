package tech.dut.fasto.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.dut.fasto.config.properties.FastoProperties;


@Configuration
@RequiredArgsConstructor
public class AmazonS3Config {

    private final FastoProperties fastoProperties;

    public AWSCredentialsProvider credential() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(this.fastoProperties.getS3().getAws().getCredentials().getAccessKey(), this.fastoProperties.getS3().getAws().getCredentials().getSecretKey());
        return new AWSStaticCredentialsProvider(basicAWSCredentials);
    }

    @Bean
    public AmazonS3 s3client() {
        return AmazonS3ClientBuilder.standard().withCredentials(credential()).withRegion(this.fastoProperties.getS3().getAws().getRegion()).build();
    }

}
