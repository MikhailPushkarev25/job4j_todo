package ru.job4j.storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.model.Items;

import java.util.Date;
import java.util.List;

public class HibernateStore implements Store, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new HibernateStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Override
    public Items add(Items items) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(items);
        session.getTransaction().commit();
        session.close();
        return items;
    }

    @Override
    public List<Items> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Items").getResultList();
        session.getTransaction();
        session.close();
        return result;
    }
       @Override
        public void update(Items item) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("UPDATE Items SET id = :id, des = :des, created = :created, done = :done")
                .setParameter("id", item.getId())
                .setParameter("des", item.getDes())
                .setParameter("created", item.getCreated())
                .setParameter("done", item.getDone());
        query.executeUpdate();
        session.close();
    }

}
