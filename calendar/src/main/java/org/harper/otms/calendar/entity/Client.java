package org.harper.otms.calendar.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.harper.otms.auth.entity.User;

@Entity
@Table(name="client")
public class Client extends User {

}
