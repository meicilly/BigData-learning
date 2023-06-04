package com.meicilly;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet2 extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("初始化了hello2");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        ServletConfig servletConfig = getServletConfig();
        System.out.println(servletConfig);

        //        2、获取初始化参数init-param
        System.out.println("初始化参数username的值是;" + servletConfig.getInitParameter("username"));
        //System.out.println("初始化参数url的值是;" + servletConfig.getInitParameter("url"));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
