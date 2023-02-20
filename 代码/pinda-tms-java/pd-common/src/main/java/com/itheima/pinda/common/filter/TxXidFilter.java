package com.itheima.pinda.common.filter;

import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 创建一个Http Rest请求过滤器，用于把当前上下文中获取到的XID放到RootContext
 */
@Component
@Slf4j
public class TxXidFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String xid = RootContext.getXID();
        String txXid = request.getHeader("TX_XID");
        boolean bind = false;
        if (StringUtils.isBlank(xid) && StringUtils.isNotBlank(txXid)) {
            RootContext.bind(txXid);
            bind = true;
            log.debug("绑定 [" + txXid + "]  RootContext");
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            if (bind) {
                String unbindXid = RootContext.unbind();
                log.debug("解绑 [" + unbindXid + "]  RootContext");
                if (!txXid.equalsIgnoreCase(unbindXid)) {
                    log.warn("变更 [" + txXid + "] ---> [" + unbindXid + "]");
                    if (unbindXid != null) {
                        RootContext.bind(unbindXid);
                        log.warn("绑定 [" + unbindXid + "] 回 RootContext");
                    }
                }
            }
        }
    }
}
