package cn.study.web.servlet;

import cn.study.service.ProvinceService;
import cn.study.service.impl.ProvinceServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/provinceServlet")
public class ProvinceServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ProvinceService provinceService = new ProvinceServiceImpl();

       /* List<Province> provinces = provinceService.findAll();
        //序列化成json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(provinces);
        System.out.println(json);*/

       String json = provinceService.findAllJson();

        //响应结果
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
