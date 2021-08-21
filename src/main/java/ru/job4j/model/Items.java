package ru.job4j.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "lists")
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String des;
    private Timestamp created;
    private Boolean done;

    public Items(Integer id, String des, Timestamp created, Boolean done) {
        this.id = id;
        this.des = des;
        this.created = created;
        this.done = done;
    }

    public Items() {

    }

    public Items(String des, Timestamp created, Boolean done) {
        this.des = des;
        this.created = created;
        this.done = done;
    }

    public Items(Integer id, String des, Boolean done) {
        this.id = id;
        this.des = des;
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Items items = (Items) o;
        return Objects.equals(id, items.id) &&
                Objects.equals(des, items.des) &&
                Objects.equals(created, items.created) &&
                Objects.equals(done, items.done);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, des, created, done);
    }

    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", des='" + des + '\'' +
                ", created=" + created +
                ", done=" + done +
                '}';
    }
}
