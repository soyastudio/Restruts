package soya.framework.action.mvc;

import soya.framework.commons.util.ReflectUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StateMachineServlet extends HttpServlet {
    private MvcMappings mappings;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.mappings = new MvcMappings();

        ReflectUtils.scanForAnnotation(MvcDefinition.class).forEach(e -> {
            mappings.add((Class<? extends MvcAction>) e);
        });

        config.getServletContext().setAttribute(MvcMappings.ATTRIBUTE_NAME, mappings);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    protected void dispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        MvcMapping mapping = mappings.get(req.getMethod(), req.getServletPath());
        System.out.println("=============== " + mapping);

        //req.getServletContext().getRequestDispatcher(mapping.).forward(req, resp);

        resp.sendRedirect("/index.html");

    }
}
