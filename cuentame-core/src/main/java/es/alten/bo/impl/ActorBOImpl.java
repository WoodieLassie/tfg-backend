package es.alten.bo.impl;

import es.alten.bo.ActorBO;
import es.alten.dao.ActorRepository;
import es.alten.domain.Actor;
import es.alten.domain.QActor;
import es.alten.dto.ActorFilterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorBOImpl
    extends ElvisGenericCRUDServiceImpl<Actor, Long, QActor, ActorFilterDTO, ActorRepository>
    implements ActorBO {

    private static final long serialVersionUID = -2327250805753457217L;
    private static final Logger LOG = LoggerFactory.getLogger(ActorBOImpl.class);

    public ActorBOImpl(ActorRepository repository) {
        super(repository);
    }
}
