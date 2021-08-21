package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.model.Items;
import ru.job4j.storage.HibernateStore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AddServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Items> items = new ArrayList<>(HibernateStore.instOf().findAll());
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter pw = new PrintWriter(resp.getOutputStream(), true, StandardCharsets.UTF_8);
        String json = GSON.toJson(items);
        pw.println(json);
        pw.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        String id = req.getParameter("id");
        String des = req.getParameter("des");
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = time.format(formatter);
        Timestamp created = Timestamp.valueOf(date);
        boolean done = Boolean.parseBoolean(req.getParameter("done"));
        if (Integer.parseInt(id) != 0) {
            Items items = new Items(Integer.parseInt(id), des, done);
            HibernateStore.instOf().update(items);
            return;
        }
        Items items = new Items(Integer.parseInt(id), des, created, true);
        HibernateStore.instOf().add(items);
    }
}
