package org.itkk.udf.starter.core.exception.handle;

import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;

@Slf4j
public class RmsResponseErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String responseBody = CoreUtil.getData(response.getBody());
        HttpStatus statusCode = HttpStatus.resolve(response.getRawStatusCode());
        switch (statusCode.series()) {
            case CLIENT_ERROR:
                throw new HttpClientErrorException(statusCode, response.getStatusText(),
                        response.getHeaders(), responseBody.getBytes(CoreConstant.CHARACTER_SET), getCharset(response));
            case SERVER_ERROR:
                throw new HttpServerErrorException(statusCode, response.getStatusText(),
                        response.getHeaders(), responseBody.getBytes(CoreConstant.CHARACTER_SET), getCharset(response));
            default:
                throw new RestClientException("Unknown status code [" + statusCode + "]");
        }

    }
}
