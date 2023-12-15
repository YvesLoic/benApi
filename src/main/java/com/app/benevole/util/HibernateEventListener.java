package com.app.benevole.util;

import com.app.benevole.model.Category;
import com.app.benevole.model.Horaire;
import com.app.benevole.model.Magasin;
import com.app.benevole.model.Team;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;

public class HibernateEventListener implements PostInsertEventListener {
    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        Object entity = postInsertEvent.getEntity();
        if (entity instanceof Horaire) {
            Horaire horaire = (Horaire) entity;
            //TODO handle logic...
        } else if (entity instanceof Team) {
            Team team = (Team) entity;
            //TODO handle logic...
        } else if (entity instanceof Category) {
            Category category = (Category) entity;
            //TODO handle logic...
        } else if (entity instanceof Magasin) {
            Magasin magasin = (Magasin) entity;
            //TODO handle logic...
        }

    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }
}