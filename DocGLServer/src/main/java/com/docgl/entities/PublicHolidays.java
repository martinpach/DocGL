package com.docgl.entities;

import com.docgl.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Rasťo on 10.5.2017.
 */
@Entity
@Table(name = "Public_Holidays")
@NamedQueries({
       @NamedQuery(name = "getAllPublicHolidays", query = "from PublicHolidays")
})
public class PublicHolidays {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;


    public PublicHolidays() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
