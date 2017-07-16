package com.jachaofen.entities.extend;

import com.jachaofen.entities.basic.Position;
import com.jachaofen.entities.basic.User;

/**
 * Created by steve on 17-7-11.
 * users_in_photo的属性构成单例
 */
public class User_Position {

    private User user;
    private Position position;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
