package com.ars.commons.interceptor;

import com.ars.commons.dto.UserDTO;
import com.ars.commons.utils.LoginUtil;
import com.ars.commons.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 〈一句话介绍功能〉<br>
 * 统一登录拦截
 *
 * @author jierui on 2019-12-20.
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate<String, UserDTO> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contextPath = request.getContextPath();
        String prePath = request.getScheme() + "://" + request.getServerName();
        String basePath = prePath + ":" + request.getServerPort() + contextPath + "/#/login";
        try {
            if (!isLogin(request, response)) {
                response.sendRedirect(basePath);
                return false;
            }
        } catch (Exception e) {
            log.error("系统登录异常", e);
            request.setAttribute("errorMessage", "系统登录异常，请稍后重试！");
            return false;
        }
        return true;
    }

    /**
     * 用户是否登录
     *
     * @param request
     * @param response
     * @return
     */
    private boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
        //获取cookie中用户登录信息
        String userKey = LoginUtil.getUserKey(request);
        if (StringUtils.hasText(userKey)) {
            return false;
        }
        //从redis中获取用户信息
        UserDTO userVo = redisTemplate.opsForValue().get(LoginUtil.generateRedisKey(userKey));
        //用户信息存在，该用户已登录
        if (null != userVo) {
            //用户信息放到请求中
            request.setAttribute(LoginUtil.REQUEST_ATTRIBUTE_USER_LEY, userVo);
            //重新生成cookie放到响应信息中去
            response.addCookie(LoginUtil.generateCookie(userKey));
            //刷新用户信息在redis中的缓存
            redisTemplate.opsForValue().set(LoginUtil.generateRedisKey(userKey), userVo, LoginUtil.COOKIE_TIME_OUT);
        } else {
            return false;
        }
        UserHolder.add(userVo);
        return true;
    }

}
