package com.meicilly;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HelloServlet implements Servlet {
    public HelloServlet(){
        System.out.println("构造器");
    }
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init初始化方法");
        // TODO: 2023/6/4 获取Serlet程序的别名servlet-name的值
        //System.out.println("HelloServlet程序的别名："+servletConfig.getServletName());
        // TODO: 2023/6/4 获取初始化参数init-param
        //System.out.println("初始化username的值是："+ servletConfig.getInitParameter("username"));
        // TODO: 2023/6/4
        //System.out.println("初始化参数url的值是：" + servletConfig.getInitParameter("url"));
        // TODO: 2023/6/4 获取ServletContext对象
        //System.out.println(servletConfig.getServletContext());
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * 访问这个方法的时候 就会调用这个方法
     * @param servletRequest
     * @param servletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("访问了servlet方法");
        // TODO: 2023/6/3 类型转换(因为它有getMethod()方法)
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // TODO: 2023/6/3 获取请求方式
        String method = httpServletRequest.getMethod();
        System.out.println(method);
        if("GET".equals(method)){
            doGet(servletRequest,servletResponse);
        }else {
            doPost();
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
    public void doGet(ServletRequest servletRequest, ServletResponse servletResponse){
        /**
         * 1.Servlet是一个接口 它表示Servlet上下文对象
         * 2.一个web工程 只有一个Servlet对象实例
         * 3.Servlet对象是一个域对象
         * 4.Servlet是在web工程部署启动的时候创建 在web工程停止的时候销毁
         *
         * 域对象 是可以像Map一样存取数据的对象 叫域对象
         * 存数据 取数据 删除 数据
         * Map      put()           get()       remove()
         * 域对象 setAttribute() getAttribute() removeAttribute();
         *
         * 1、获取 web.xml 中配置的上下文参数 context-param
         * 2、获取当前的工程路径，格式: /工程路径
         * 3、获取工程部署后在服务器硬盘上的绝对路径
         * 4、像 Map 一样存取数据
         */
        System.out.println("执行了doGet方法");
        // TODO: 2023/6/4 获取web.xml中配置的上下文参数context-param
        //System.out.println(getServletConfig().getServletContext());
//        ServletContext context = getServletConfig().getServletContext();
//        String username = context.getInitParameter("username");
//        System.out.println("context-param 参数 username 的值是:" + username);
//        System.out.println("context-param 参数 password 的值是:" +
//                context.getInitParameter("password"));
    }

    public void doPost(){
        System.out.println("post 请求");
        System.out.println("post 请求");
    }
}
