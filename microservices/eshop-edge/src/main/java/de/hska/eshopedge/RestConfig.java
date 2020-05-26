package de.hska.eshopedge;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Configuration
public class RestConfig {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        RestTemplate rt = new RestTemplate();
        rt.setErrorHandler(new RestTemplateResponseErrorHandler());
        return rt;
    }


    private class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().series() == CLIENT_ERROR
                    || response.getStatusCode().series() == SERVER_ERROR;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            if (response.getStatusCode()
                    .series() == HttpStatus.Series.SERVER_ERROR) {
                // handle SERVER_ERROR
            } else if (response.getStatusCode()
                    .series() == HttpStatus.Series.CLIENT_ERROR) {
                // handle CLIENT_ERROR
                if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new NotFoundException();
                }
            }
        }

        private class NotFoundException extends IOException {
            public NotFoundException() throws IOException {
                super("Response 404");
            }
        }
    }
}
