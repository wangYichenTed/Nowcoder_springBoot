package com.wyc.community.controller;

import com.wyc.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
//访问名
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "Hello Spring boot";
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());

        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        System.out.println(request.getParameter("code"));

        //向浏览器做出相应，返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try(PrintWriter writer = response.getWriter();) {//java7新语法特性
            writer.write("<h1>牛客网</h1>");
            //网页一行一行write
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //请求数据如何处理
    //GET请求，一般用于从服务器获取某些数据，一般默认就是GET请求

    //比如说查询学生 路径为:/students?current=1&limit=20 传送后服务器处理方法
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getstudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit" , required = false, defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    //只根据一个学生的ID查询一个学生
    //路径设置 /student/123(直接把参数当成路径的一部分)
    @RequestMapping(path = "/student/{id}" , method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return id +" is a student";
    }

    //如果浏览器向服务器提交数据，那么采用Post请求
    //只用GET和POST请求就可以解决所有问题，其他可以辅助
    //后面POST实例
    //首先要提交一个带有表单的网页才能传递数据，下面就是在static放静态网页
    //其实GET也可以传参数，但传递的参数是在明面上的，会被看到，且路径长度有限制，GET不一定能传递得下，数据量有限，因此常用POST提交请求

    @RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //接下来向浏览器返回响应数据，前面都是简单的字符串，下面向浏览器响应动态的HTML数据
    //假设查询一个老师，服务器查询到老师相关的数据响应给浏览器，以网页的形式
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        //返回的是MODEL VIEW,提交给DISPACHER返回的MODEL和VIEW两份数据
        ModelAndView mav = new ModelAndView();
        mav.addObject("name","张三");
        mav.addObject("age",30);
        //下面设置模板的位置和名字，这个路径在templates下面
        mav.setViewName("/demo/view");
        return mav;
    }

    //另一种方式返回动态网页，查询学校
    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","北大附小");
        model.addAttribute("age",100);
        return "/demo/view";
    }

    //服务器还能响应其他任何格式的数据，例如JSON数据
    //通常在异步请求中需要响应JSON数据，例如注册时，输入昵称、密码，当文本框失去焦点时，需要判断昵称是否被占用，那此时一定是查询了数据库，此时是异步请求
    //当前网页不刷新，整个网页不变但确实有请求，则叫异步请求
    //如何响应这样的访问呢
    //JSON是将JAVA对象转换为JS对象的中间件，JAVA对象->JSON字符串->JS对象，是一种跨语言环境下的一种字符串形式，尤其是异步请求，局部验证的结果是否成功
    @RequestMapping(path = "/employee", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmplyee(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000.00);
        return emp;
    }

    //返回的是一组员工，是多个相似数据的结构，例如查询所有的员工
    @RequestMapping(path = "/employees", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmplyees(){
        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","李四");
        emp.put("age",24);
        emp.put("salary",9000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","王五");
        emp.put("age",25);
        emp.put("salary",10000.00);
        list.add(emp);

        return list;
    }
}
