package me.brlw.test1;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Created by vl on 06.08.16.
 */

@Entity
@Table(name="User")
@NamedQueries({
    @NamedQuery(name="User.findById",
            query="select distinct u from User u where u.id = :id")
})
public class User implements Serializable
{
    private long id;
    private int version;
    private String name;
    private Integer age;
    private boolean isAdmin;
    private Date createdDate = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @NotEmpty(message = "{validation.name.NotEmpty.message}")
    @Size(min = 3, max = 25, message = "{validation.name.Size.message}")
    @Column(name = "name", length = 25)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "{validation.age.NotEmpty.message}")
    @Range(min=1, max=100, message = "{validation.age.Range.message}")
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Type(type = "org.hibernate.type.NumericBooleanType")
    @Column(name = "isAdmin")
    public Boolean getisAdmin() {
        return isAdmin;
    }

    public void setisAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", updatable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", isAdmin=" + isAdmin +
                ", createdDate=" + createdDate +
                '}';
    }

}
