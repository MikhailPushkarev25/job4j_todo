package ru.job4j.storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Items;

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
    public void replace(Items items) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(items);
        session.getTransaction().commit();
        session.close();
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

        public void update(Items item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public Items findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Items items = session.get(Items.class, id);
        session.getTransaction().commit();
        session.close();
        return items;
    }
}
