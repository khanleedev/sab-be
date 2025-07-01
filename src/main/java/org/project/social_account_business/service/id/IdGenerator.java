package org.project.social_account_business.service.id;

import org.project.social_account_business.model.ReuseId;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class IdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        try {
            ReuseId reuseId = (ReuseId) o;
            if (reuseId.getId() != null) {
                return reuseId.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextId();
    }

    public Long nextId() {
        return SnowFlakeIdService.getInstance().nextId();
    }
}
