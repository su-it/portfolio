package com.shkim.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * TloFilter
 * : 접속 요청에 대한 TLO로그 생성(Transaction Log, 모든 정상 요청에 대한 기록)
 *
 * @author shkim
 */
@Slf4j
@Component
public class TloFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tid = UUID.randomUUID().toString();
        request.setAttribute("tid", tid);

        ReadableRequsetWrapper wrappedRequest = new ReadableRequsetWrapper((HttpServletRequest) request);
        ServletInputStream inputStream = wrappedRequest.getInputStream();
        String requestBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        request.setAttribute("requestBody", requestBody);

        long reqTime = System.currentTimeMillis();
        chain.doFilter(wrappedRequest, response);
        long resTime = System.currentTimeMillis();

        if("POST".equals(((HttpServletRequest) request).getMethod())){
            log.info(LogMarker.TLO, "TLO_ID:{}|LOG_TIME:{}|DURATION_TIME:{}|REQUEST_BODY:{}", tid, String.valueOf(new Timestamp(System.currentTimeMillis())),  String.valueOf((resTime-reqTime)/1000), requestBody);
        }
    }

    @Override
    public void destroy() {
    }
}
