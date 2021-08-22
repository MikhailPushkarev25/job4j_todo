package ru.job4j.storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.hibernate.query.Query;
import ru.job4j.model.Items;

import java.sql.Timestamp;
import java.util.List;

public class MainHiber {

    public static void main(String[] args) {

         StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .configure().build();

        try {

            SessionFactory sf = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();

            Items items = add(new Items(1, "Сходить в магазин", new Timestamp(1382479274L), false), sf);
             System.out.println(items);
             items.setId(9);
            replace(items, sf);
             System.out.println(items);
             Items rsl = findById(items.getId(), sf);
            System.out.println(rsl);
            List<Items> list = findAll(sf);
             for (Items it : list) {
                 System.out.println(it);
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             StandardServiceRegistryBuilder.destroy(registry);
         }

    }

    public static Items add(Items items, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(items);
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public static void replace(Items items, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("UPDATE Items SET id = :id, des = :des, created = :created, done = :done")
                .setParameter("id", items.getId())
                .setParameter("des", items.getDes())
                .setParameter("created", items.getCreated())
                .setParameter("done", items.getDone());
        query.executeUpdate();
        session.close();
    }

    public static List<Items> findAll(SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from ru.job4j.model.Items").list();
        session.getTransaction();
        session.close();
        return result;
    }

    public static Items findById(int id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Items items = session.get(Items.class, id);
        session.getTransaction().commit();
        session.close();
        return items;
    }
}
