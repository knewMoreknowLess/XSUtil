package com.cs.util.xsutil.common.config;

import com.cs.util.xsutil.common.util.StringUtil;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//请求支持跨域
@Configuration
public class CrosFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        //不包含 origin请求头
        String origin = req.getHeader("origin");
        if(StringUtil.isNotEmpty(origin)){
            res.addHeader("Access-Control-Allow-Origin",origin);
        }
        res.addHeader("Access-Control-Allow-Methods","*");

        String headers = req.getHeader("Access-Control-Allow-headers");
        //支持所有定义头
        if(StringUtil.isNotEmpty(headers)){
            res.addHeader("Access-Control-Allow-headers",headers);
        }
        //响应头信息缓存，浏览器非简单请求不会 进行预检，提高请求效率
        res.addHeader("Access-Control-Max-Age","3600");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
