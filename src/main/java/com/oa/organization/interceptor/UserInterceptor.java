package com.oa.organization.interceptor;

import com.oa.organization.entity.SyUser;
import com.oa.organization.service.impl.UserServiceImpl;
import jdk.nashorn.internal.objects.Global;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 拦截器，对未登录的用户实现拦截
 */
public class UserInterceptor implements HandlerInterceptor {
    /**
     * 预处理回调方法，实现处理器的预处理，如登录前验证。第三个参数为响应的处理器（如我们上一章的Controller实现）；
     * 返回值：true表示继续流程（如调用下一个拦截器或处理器）；false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应；
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取登录ur,已在spring-web.xml中进行拦截处理
        String url = httpServletRequest.getContextPath();
        //允许登录的url
        String[] allowUrls = new String[]{"login", "html", "css", "js"};
        //如果请求的是登录界面放行
        for (String urlStr : allowUrls) {
            if (url.contains(urlStr)) {
                return true;
            }
        }
        //如果是已登录用户放行
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("token") != null) {
            return true;
        }
        //其他不放行
        return false;
    }

    /**
     * 后处理回调方法，实现处理器的后处理（但在渲染视图之前），
     * 此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null。
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，如性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理，
     * 类似于try-catch-finally中的finally，但仅调用处理器执行链中preHandle返回true的拦截器的afterCompletion。
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
