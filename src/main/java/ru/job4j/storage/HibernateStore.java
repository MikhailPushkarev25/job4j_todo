package ru.job4j.storage;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.model.Items;
import java.util.List;
import java.util.function.Function;

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

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Items add(Items items) {
        return this.tx(session -> {
            session.save(items);
            return items;
        });
    }

    @Override
    public List<Items> findAll() {
       return this.tx(
               session -> session.createQuery("from Items").getResultList()
       );
    }
       @Override
        public void update(Items item) {
        this.tx( session -> {
            Query query = session.createQuery("UPDATE Items SET id = :id, des = :des, created = :created, done = :done")
                    .setParameter("id", item.getId())
                    .setParameter("des", item.getDes())
                    .setParameter("created", item.getCreated())
                    .setParameter("done", item.getDone());
            query.executeUpdate();
            return query;
        });
    }

}
