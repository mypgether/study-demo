package com.gether.bigdata.endpoint;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

public class MetricsAMvcEndPoint extends EndpointMvcAdapter {

    private MetricsAEndpoint delegate;

    public MetricsAMvcEndPoint(MetricsAEndpoint delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @RequestMapping(
            method = {RequestMethod.GET}
    )
    @ResponseBody
    public Object invoke() {
        if (!this.getDelegate().isEnabled()) {
            return new ResponseEntity(Collections.singletonMap("message", "This endpoint is disabled"), HttpStatus.NOT_FOUND);
        } else {
            return delegate.invoke();
        }
    }
}