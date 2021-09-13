package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    public static final String API_POSTS = "/api/posts";
    public static final String API_POSTS_REGEX = "/api/posts/\\d+";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    private PostController controller;

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {

            final var method = req.getMethod();
            // primitive routing
            switch (method) {
                case GET:
                    doGet(req, resp);
                    break;
                case POST:
                    doPost(req, resp);
                    break;
                case DELETE:
                    doDelete(req, resp);
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getRequestURI().equals(API_POSTS)) {
            controller.all(resp);
            return;
        }
        if (req.getRequestURI().matches(API_POSTS_REGEX)) {
            // easy way
            controller.getById(getIdFromPath(req.getRequestURI()), resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals(API_POSTS)) {
            controller.save(req.getReader(), resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().matches(API_POSTS_REGEX)) {
            // easy way
            controller.removeById(getIdFromPath(req.getRequestURI()), resp);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    private long getIdFromPath(String str) {
        return Long.parseLong(str.substring(str.lastIndexOf("/") + 1));
    }

}

