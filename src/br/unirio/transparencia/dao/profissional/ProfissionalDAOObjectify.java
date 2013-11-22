package br.unirio.transparencia.dao.profissional;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import br.unirio.transparencia.model.Profissional;

import com.googlecode.objectify.Key;

public class ProfissionalDAOObjectify implements Serializable, ProfissionalDAO {

/**
 * Implementa o contrato de persistência da entidade <code>Profissional</code>.
 * 
 * <p>
 *   Nessa aplicação resolvemos a persistência utilizando o Objectify, um framework de persistência para o App Engine.<br/>
 *   A proposta do Objetify é denifir uma API mais alto-nível para manipular dados no <code>DataStore</code> do App Engine.
 * </p>
 * 
 
*/
	private static final long serialVersionUID = 1L;

	@Override
	public Long save(Profissional profissional) {
		ofy().save().entity(profissional).now();
		return profissional.getId();
	}
	
	@Override
	public List<Profissional> getAll() {
		
	 return	ofy().load().type(Profissional.class).list();
	// return  ofy().load().type(Profissional.class).offset(3).limit(10).list();
	}
	
	@Override
	public Boolean remove(Profissional profissional) {
		ofy().delete().entity(profissional).now();
		return true;
	}
	
	@Override
	public Profissional findById(Long id) {
		Key<Profissional> k = Key.create(Profissional.class, id);
		return ofy().load().key(k).get();
	}

	@Override
	public Profissional findByEmail(String email) {
		Key<Profissional> k = ofy().load().type(Profissional.class).filter("email",email).first().getKey();
		Profissional profissional= ofy().load().key(k).get();
		return profissional;
		/*
		List<Profissional> profissionais =getAll();
		for (Profissional prof:profissionais)
		{
			if (prof.getEmail().compareTo(email)==0)
				return prof;
		}	
		return null;	
		*/
		
	}


}
